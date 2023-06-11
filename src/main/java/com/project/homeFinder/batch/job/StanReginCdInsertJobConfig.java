package com.project.homeFinder.batch.job;

import com.project.homeFinder.batch.job.step.reader.OpenDataApiApartmentBasicInfoReader;
import com.project.homeFinder.batch.job.step.reader.StanReginCdReader;
import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.domain.ApartmentToSubway;
import com.project.homeFinder.domain.BjdCode;
import com.project.homeFinder.dto.Point;
import com.project.homeFinder.dto.response.KakaoSearchByAddressResponse;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.dto.response.raw.xml.StanReginCdXmlResponseRaw;
import com.project.homeFinder.repository.ApartmentRepository;
import com.project.homeFinder.repository.ApartmentToSubwayRepository;
import com.project.homeFinder.repository.BjdCodeRepository;
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
 * 
 * 법정동 코드 저장하기
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StanReginCdInsertJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final OpenDataApi openDataApi;

    private final BjdCodeRepository bjdCodeRepository;

    public Job stanReginCdInsertJoLaunch() {
        return stanReginCdInsertJob(
                this.stanReginCdInsertStep(
                        this.stanReginCdInsertInsertStepItemReader(),
                        this.stanReginCdInsertInsertStepItemProcessor(),
                        this.stanReginCdInsertInsertStepItemWriter(bjdCodeRepository)
                )
        );
    }

    @Bean
    public Job stanReginCdInsertJob(Step stanReginCdInsertStep) {

        return new JobBuilder("stanReginCdInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stanReginCdInsertStep)
                .build();
    }

    @Bean
    @JobScope
    public Step stanReginCdInsertStep(ItemReader<StanReginCdXmlResponseRaw> stanReginCdInsertInsertStepItemReader,
                                        ItemProcessor<StanReginCdXmlResponseRaw, List<BjdCode>> stanReginCdInsertInsertStepItemProcessor,
                                        ItemWriter<List<BjdCode>> stanReginCdInsertInsertStepItemWriter) {

        return new StepBuilder("stanReginCdInsertStep", jobRepository)
                .<StanReginCdXmlResponseRaw, List<BjdCode>>chunk(1, platformTransactionManager)
                .reader(stanReginCdInsertInsertStepItemReader)
                .processor(stanReginCdInsertInsertStepItemProcessor)
                .writer(stanReginCdInsertInsertStepItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<StanReginCdXmlResponseRaw> stanReginCdInsertInsertStepItemReader() {
        return new StanReginCdReader(openDataApi);
    }

    @Bean
    @StepScope
    public ItemProcessor<StanReginCdXmlResponseRaw, List<BjdCode>> stanReginCdInsertInsertStepItemProcessor(){

        return item -> item.toEntity().stream()
                .filter(i -> !i.getUmdCode().equals("000"))
                .filter(i -> bjdCodeRepository.countByRegionCode(i.getRegionCode()) == 0L)
                .collect(Collectors.toList());
    }

    @Bean
    @StepScope
    public ItemWriter<List<BjdCode>> stanReginCdInsertInsertStepItemWriter(BjdCodeRepository bjdCodeRepository) {

        return items -> items.forEach(item -> bjdCodeRepository.saveAllAndFlush(item));
    }


}
