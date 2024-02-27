package com.example.springbatchstudy;

import static com.example.springbatchstudy.domain.pay.PayStatus.PAY_DONE;

import com.example.springbatchstudy.domain.adjust.WalkerAdjust;
import com.example.springbatchstudy.domain.adjust.WalkerAdjustDetail;
import com.example.springbatchstudy.domain.adjust.WalkerAdjustRepository;
import com.example.springbatchstudy.domain.pay.PayHistory;
import com.example.springbatchstudy.domain.pay.PayHistoryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfig {

  private final PayHistoryRepository payHistoryRespository;
  private final WalkerAdjustRepository walkerAdjustRepository;

  @Bean
  public Job adjustJob(JobRepository jobRepository , Step step1) {
    return new JobBuilder("endOfDay" , jobRepository)
        .start(step1)
        .build();
  }

  @Bean
  public Step adjustStep(JobRepository jobRepository , PlatformTransactionManager transactionManager) {
    return new StepBuilder("step1" , jobRepository)
        .tasklet(myTasklet() , transactionManager)
        .build();
  }


  @Bean
  public Tasklet myTasklet () {
    return ((contribution, chunkContext) -> {

      List <PayHistory> payHistoryList = payHistoryRespository.findByPayStatusOrderByUserId(PAY_DONE);
      for (PayHistory payHistory : payHistoryList) {
        Optional <WalkerAdjust> walkerAdjust = walkerAdjustRepository.findByUserIdAndAndWalkerAdjustDate(
            payHistory.getUserId() , LocalDate.now());
        if(walkerAdjust.isPresent()) {
          WalkerAdjust adjust = walkerAdjust.get();
          adjust.setPrice(adjust.getWalkerTtlPrice()+payHistory.getPayPrice());
          adjust.addAdjustDetail(WalkerAdjustDetail.builder()
                  .walkerAdjust(adjust)
                  .walkerAdjustPrice(payHistory.getPayPrice())
                  .payhistoryId(payHistory.getPayId())
                  .build());
          walkerAdjustRepository.save(adjust);

        } else {
          WalkerAdjust adjust = WalkerAdjust.builder()
              .userId(payHistory.getUserId())
              .walkerAdjustDate(LocalDate.now())
              .build();

          adjust.setPrice(payHistory.getPayPrice());
          adjust.addAdjustDetail(WalkerAdjustDetail.builder()
              .walkerAdjust(adjust)
              .walkerAdjustPrice(payHistory.getPayPrice())
              .payhistoryId(payHistory.getPayId())
              .build());

          walkerAdjustRepository.save(adjust);
        }
      }

      return RepeatStatus.FINISHED;
    });
  }

}