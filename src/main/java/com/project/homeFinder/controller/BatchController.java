package com.project.homeFinder.controller;

import com.project.homeFinder.batch.job.ApartmentInfoInsertJobConfig;
import com.project.homeFinder.batch.job.StanReginCdInsertJobConfig;
import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlItem;
import com.project.homeFinder.dto.response.raw.xml.ApartmentListXmlItem;
import com.project.homeFinder.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final StanReginCdInsertJobConfig stanReginCdInsertJobConfig;

    @GetMapping("/find/all/bjdCode")
    public void findAllAptInfoOpenDataApiBjdCode()
            throws URISyntaxException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        jobLauncher.run(stanReginCdInsertJobConfig.stanReginCdInsertJoLaunch(),
                new JobParameters(
                        Map.of(String.format("launchDateAndTime"),
                                new JobParameter<>(LocalDateTime.now().toString(), String.class)
                        )
                ));

    }

}
