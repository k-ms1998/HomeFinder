package com.project.homeFinder.batch.tasklet;

import com.project.homeFinder.repository.BjdCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. sidoCode로 BjdCode 테이블에 접근해서 모든 튜플들 가져와서 List<String> bjdCodes에 저장
 * 2. ReadBjdCodeTasklet이 호출될떄 마다, bjdCodes를 하나씩 가져와서 ChunkContext에 저장
 * 3. ItemReader에서 ChunckContext에 저장된 법정동 코드를 가져와서 아파트 정보 가져오기
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReadBjdCodeTasklet implements Tasklet {

    private String SEOUL_CODE = "11";
    private String BJD_CODE_LIST = "bjdCodeList";
    private String BJD_CODE = "bjdCode";
    private String ITEM_COUNT = "itemCount";

    private List<String> bjdCodes = new ArrayList<>();
    private int itemCount = 0;

    private final BjdCodeRepository bjdCodeRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext jobExecutionContext = getExecutionContext(chunkContext);

        initExecutionContext(jobExecutionContext);
        initItemCount(jobExecutionContext);

        /**
         * 더이상 읽을 데이터가 없을때
         */
        if (itemCount == 0) {
            contribution.setExitStatus(ExitStatus.COMPLETED);

            return RepeatStatus.FINISHED;
        }

        itemCount--;

        /**
         * ExecutionContext 는 Key-Value 로 값을 저장함
         */
        log.info("BJD_CODE = {}", bjdCodes.get(itemCount));
        jobExecutionContext.putString(BJD_CODE, bjdCodes.get(itemCount));
        jobExecutionContext.putInt(ITEM_COUNT, itemCount);

        /**
         * 데이터가 있으면 다음 Step 실행. 없으면 종료.
         * 데이터가 있으면 -> CONTINUABLE
         */
        contribution.setExitStatus(new ExitStatus("CONTINUABLE"));

        return RepeatStatus.FINISHED;
    }

    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        /**
         * ExecutionContext 를 가져오기 위해 먼저 현재 Step 의 StepExecution  가져오기
         */
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();

        /**
         * 현재 Job 에서의 ExecutionContext 를 가져오기
         * -> ExecutionContext 를 통해서 Step 끼리 데이터를 주고 받음
         */
        return stepExecution.getJobExecution().getExecutionContext();
    }

    private void initExecutionContext(ExecutionContext executionContext){
        if (executionContext.containsKey(BJD_CODE_LIST)) {
            bjdCodes = (List<String>) executionContext.get(BJD_CODE_LIST);
        } else {
            /**
             * Step 을 처음 실행하면 ExecutionContext 에 값들이 없기 때문에 값들을 저장 시켜주기
             */
            bjdCodes = bjdCodeRepository.findAllBySidoCode(SEOUL_CODE);
            executionContext.put(BJD_CODE_LIST, bjdCodes);
            executionContext.putInt(ITEM_COUNT, bjdCodes.size());
        }
    }

    private void initItemCount(ExecutionContext executionContext) {
        if (executionContext.containsKey(ITEM_COUNT)) {
            itemCount = executionContext.getInt(ITEM_COUNT);
        } else {
            itemCount = bjdCodes.size();
        }
    }
}