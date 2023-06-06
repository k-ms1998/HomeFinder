package com.project.homeFinder.controller;

import com.project.homeFinder.batch.job.ApartmentInfoInsertJobConfig;
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
@RequestMapping("/apartment")
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final JobLauncher jobLauncher;
    private final ApartmentInfoInsertJobConfig apartmentInfoInsertJobConfig;

    @GetMapping("/open_data/find/all")
    public ResponseEntity<List<ApartmentListXmlItem>> findAllAptInfoOpenDataApiBjdCode(@RequestParam(required = false, defaultValue = "") String bjdCode)
            throws URISyntaxException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        jobLauncher.run(apartmentInfoInsertJobConfig.apartmentInfoInsertJobLaunch(bjdCode),
                new JobParameters(
                        Map.of(String.format("%s_%s", "launchDateAndTime", bjdCode.equals("") ? "EMPTY" : bjdCode),
                                new JobParameter<>(LocalDateTime.now().toString(), String.class)
                        )
                ));

        if (bjdCode.equals("") || bjdCode.isBlank() || bjdCode == null) {
            return ResponseEntity.ok(apartmentService.findAllAptInfoOpenDataApi());
        } else {
            return ResponseEntity.ok(apartmentService.findAllAptInfoOpenDataApiBjdCode(bjdCode));
        }
    }

}
