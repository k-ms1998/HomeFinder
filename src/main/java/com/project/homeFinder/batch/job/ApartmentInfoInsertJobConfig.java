package com.project.homeFinder.batch.job;

import com.project.homeFinder.batch.job.step.reader.OpenDataApiApartmentBasicInfoReader;
import com.project.homeFinder.domain.Apartment;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListXmlItem;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListResponseRaw;
import com.project.homeFinder.repository.ApartmentRepository;
import com.project.homeFinder.service.api.OpenDataApi;
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

    private final OpenDataApi openDataApi;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ApartmentRepository apartmentRepository;

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
                                        ItemProcessor<ApartmentListResponseRaw, List<ApartmentListXmlItem>> apartmentBasicInfoXmlResponseRawItemProcessor,
                                        ItemWriter<List<ApartmentListXmlItem>> apartmentBasicInfoXmlResponseRawItemWriter) {

        return new StepBuilder("apartmentInfoInsertStep", jobRepository)
                .<ApartmentListResponseRaw, List<ApartmentListXmlItem>>chunk(1, platformTransactionManager)
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
    public ItemProcessor<ApartmentListResponseRaw, List<ApartmentListXmlItem>> apartmentBasicInfoXmlResponseRawItemProcessor(){

        return item -> item.fetchItems().stream()
                .map(i -> ApartmentListXmlItem.of(
                        i.getAs1(),
                        i.getAs2(),
                        i.getAs3(),
                        i.getAs4(),
                        i.getBjdCode(),
                        i.getKaptCode(),
                        i.getKaptName()
                )).collect(Collectors.toList());
    }

    @Bean
    @StepScope
    public ItemWriter<List<ApartmentListXmlItem>> apartmentBasicInfoXmlResponseRawItemWriter(ApartmentRepository apartmentRepository) {

        return items -> {
            items.forEach(item -> item.stream()
                    .map(Apartment::fromXml).forEach(info -> apartmentRepository.save(info)));
        };
    }
}
