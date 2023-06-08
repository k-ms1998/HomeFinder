package com.project.homeFinder.batch.job;

import com.project.homeFinder.batch.job.step.reader.OpenDataApiApartmentBasicInfoReader;
import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.domain.ApartmentToSubway;
import com.project.homeFinder.domain.Subway;
import com.project.homeFinder.domain.SubwayTravelTime;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.response.KakaoSearchByAddressResponse;
import com.project.homeFinder.dto.response.KakaoSearchByCategoryResponse;
import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlItem;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.repository.ApartmentRepository;
import com.project.homeFinder.repository.ApartmentToSubwayRepository;
import com.project.homeFinder.repository.SubwayRepository;
import com.project.homeFinder.service.SubwayService;
import com.project.homeFinder.service.api.KakaoApi;
import com.project.homeFinder.service.api.OpenDataApi;
import com.project.homeFinder.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * https://docs.spring.io/spring-batch/docs/current/reference/html/step.html
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApartmentInfoInsertJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final OpenDataApi openDataApi;
    private final KakaoApi kakaoApi;

    private final SubwayService subwayService;

    private final ApartmentRepository apartmentRepository;
    private final ApartmentToSubwayRepository apartmentToSubwayRepository;
    private final SubwayRepository subwayRepository;

    private static String BJD_CODE = "";

    public Job apartmentInfoInsertJobLaunch(String bjdCode) {
        BJD_CODE = bjdCode;
        return apartmentInfoInsertJob(
                this.apartmentInfoInsertStep(
                        this.apartmentBasicInfoXmlResponseRawItemReader(),
                        this.apartmentBasicInfoXmlResponseRawItemProcessor(),
                        this.apartmentBasicInfoXmlResponseRawItemWriter(apartmentRepository)
                )
        );
    }

    @Bean
    public Job apartmentInfoInsertJob(Step apartmentInfoInsertStep) {

        return new JobBuilder("apartmentInfoInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(apartmentInfoInsertStep)
                .build();
    }

    @Bean
    @JobScope
    public Step apartmentInfoInsertStep(ItemReader<ApartmentListResponseRaw> apartmentBasicInfoXmlResponseRawItemReader,
                                        ItemProcessor<ApartmentListResponseRaw, List<Apartment>> apartmentBasicInfoXmlResponseRawItemProcessor,
                                        ItemWriter<List<Apartment>> apartmentBasicInfoXmlResponseRawItemWriter) {

        return new StepBuilder("apartmentInfoInsertStep", jobRepository)
                .<ApartmentListResponseRaw, List<Apartment>>chunk(1, platformTransactionManager)
                .reader(apartmentBasicInfoXmlResponseRawItemReader)
                .processor(apartmentBasicInfoXmlResponseRawItemProcessor)
                .writer(apartmentBasicInfoXmlResponseRawItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<ApartmentListResponseRaw> apartmentBasicInfoXmlResponseRawItemReader() {
        OpenDataApiApartmentBasicInfoReader itemReader = new OpenDataApiApartmentBasicInfoReader(openDataApi);
        itemReader.updateBjdCode(BJD_CODE);

        return itemReader;
    }

    @Bean
    @StepScope
    public ItemProcessor<ApartmentListResponseRaw, List<Apartment>> apartmentBasicInfoXmlResponseRawItemProcessor(){

        return item -> item.fetchItems().stream()
                .filter(i -> apartmentRepository.countByName(i.getKaptName()) == 0L)
                .map(i -> openDataApi.openDataAptBasicInfo(i.getKaptCode()).toItem().toEntity())
                .collect(Collectors.toList());
    }

    @Bean
    @StepScope
    public ItemWriter<List<Apartment>> apartmentBasicInfoXmlResponseRawItemWriter(ApartmentRepository apartmentRepository) {

        return items -> items.forEach(item -> item.stream()
                .filter(info -> checkIfAddressExists(info.getAddress()))
                .forEach(info -> {
                    String roadAddress = info.getAddress();

                    KakaoSearchByAddressResponse byAddress = kakaoApi.findByAddress(roadAddress).get(0);
                    info.updateXY(byAddress.getX(), byAddress.getY());

                    Apartment apartment = apartmentRepository.saveAndFlush(info);

                    subwayService.findToNearestSubway(new Point(byAddress.getX(), byAddress.getY())).stream()
                            .forEach(ksbcr -> {
                                Long distance = Long.parseLong(ksbcr.getDistance()); // 가장 가까운 지하철역까지 거리(m)
                                Long time = distance / 80;
                                String subwayName = ServiceUtils.checkAndRemoveSubwayNameSuffix(ksbcr.getName());
                                subwayRepository.findFirstByName(subwayName)
                                        .ifPresentOrElse(subway -> {
                                            apartmentToSubwayRepository.save(ApartmentToSubway.of(apartment, subway, distance, time));
                                        }, () -> {
                                            return;
                                        });
                            });
                }));
    }

    private boolean checkIfAddressExists(String roadAddress) {
        if(roadAddress == null){
            return false;
        }
        if(roadAddress == "" || roadAddress.isBlank()){
            return false;
        }

        return true;
    }

}
