package com.project.homeFinder.service.job;

import com.project.homeFinder.batch.job.StanReginCdInsertJobConfig;
import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlResponseRaw;
import com.project.homeFinder.dto.response.raw.xml.StanReginCdXmlResponseRaw;
import com.project.homeFinder.service.api.OpenDataApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Map;

@Import(StanReginCdInsertJobConfig.class)
@SpringBootTest
class StanReginCdInsertJobTest {

    private final StanReginCdInsertJobConfig stanReginCdInsertJobConfig;
    private final JobLauncher jobLauncher;

    @Autowired
    public StanReginCdInsertJobTest(StanReginCdInsertJobConfig stanReginCdInsertJobConfig, JobLauncher jobLauncher) {
        this.stanReginCdInsertJobConfig = stanReginCdInsertJobConfig;
        this.jobLauncher = jobLauncher;
    }

    @Disabled
    @Test
    void aptBasicInfo() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        jobLauncher.run(stanReginCdInsertJobConfig.stanReginCdInsertJoLaunch(),
                new JobParameters(
                        Map.of(String.format("launchDateAndTime"),
                                new JobParameter<>(LocalDateTime.now().toString(), String.class)
                        )
                ));
    }

}