package com.project.homeFinder.controller;

import com.project.homeFinder.batch.job.ApartmentInfoInsertJobConfig;
import com.project.homeFinder.dto.response.raw.xml.ApartmentBasicInfoXmlItem;
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
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment")
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final JobLauncher jobLauncher;
    private final ApartmentInfoInsertJobConfig apartmentInfoInsertJobConfig;

    @GetMapping("/open_data/find/all")
    public ResponseEntity<List<ApartmentBasicInfoXmlItem>> findAllAptInfoOpenDataApi() throws URISyntaxException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(apartmentInfoInsertJobConfig.apartmentInfoInsertJobLaunch(),
                new JobParameters(Map.of("launchDate", new JobParameter<>(LocalDate.now().toString(), String.class))));

        return ResponseEntity.ok(apartmentService.findAllAptInfoOpenDataApi());
    }

}
