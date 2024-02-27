package com.example.springbatchstudy.step;

import static com.example.springbatchstudy.domain.pay.PayStatus.ADJUST_DONE;
import static com.example.springbatchstudy.domain.pay.PayStatus.PAY_DONE;

import com.example.springbatchstudy.domain.adjust.WalkerAdjust;
import com.example.springbatchstudy.domain.adjust.WalkerAdjustDetail;
import com.example.springbatchstudy.domain.adjust.WalkerAdjustRepository;
import com.example.springbatchstudy.domain.pay.PayHistory;
import com.example.springbatchstudy.domain.pay.PayStatus;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class AdjustDetailConfig {
  private final WalkerAdjustRepository walkerAdjustRepository;
  private final EntityManagerFactory entityManagerFactory;

  @Bean (name = "AdjustDetail")
  @JobScope
  public Step adjustStep(JobRepository jobRepository , PlatformTransactionManager transactionManager) {
    return new StepBuilder("adjustStep" , jobRepository)
        .<PayHistory, WalkerAdjustDetail>chunk(10,transactionManager)
        .reader(adjustDetailReader())
        .processor(adjustDetailProcessor())
        .writer(adjustDetailItemWriter())
        .build();
  }

  @Bean
  public ItemReader <PayHistory> adjustDetailReader() {
    Map <String, Object> parameters = new HashMap <>();
    parameters.put("status", PAY_DONE);
    JpaPagingItemReader <PayHistory> reader = new JpaPagingItemReader <>(){
      @Override
      public int getPage() {
        return 0;
      }
    };
    reader.setName("adjustReader");
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setParameterValues(parameters);
    reader.setPageSize(10);
    reader.setQueryString("select p from PayHistory p WHERE p.payStatus = :status");
    return reader;
  }

  @Bean
  public ItemProcessor <PayHistory, WalkerAdjustDetail> adjustDetailProcessor() {
    return payHistory -> {

      WalkerAdjust adjust = walkerAdjustRepository.findByUserIdAndAndWalkerAdjustDate(
          payHistory.getUserId() , LocalDate.now()).get();
      adjust.setPrice(adjust.getWalkerTtlPrice()+payHistory.getPayPrice());
      payHistory.setStatus(ADJUST_DONE);
      return WalkerAdjustDetail.builder()
          .walkerAdjust(adjust)
          .payHistory(payHistory)
          .walkerAdjustPrice(payHistory.getPayPrice())
          .build();

    };
  }

  @Bean
  public ItemWriter <WalkerAdjustDetail> adjustDetailItemWriter(){
    return new JpaItemWriterBuilder <WalkerAdjustDetail>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }
}
