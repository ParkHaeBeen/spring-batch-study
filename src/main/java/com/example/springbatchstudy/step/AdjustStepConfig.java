package com.example.springbatchstudy.step;

import static com.example.springbatchstudy.domain.pay.PayStatus.PAY_DONE;

import com.example.springbatchstudy.domain.adjust.WalkerAdjust;
import com.example.springbatchstudy.domain.adjust.WalkerAdjustDetail;
import com.example.springbatchstudy.domain.adjust.WalkerAdjustDetailRepository;
import com.example.springbatchstudy.domain.adjust.WalkerAdjustRepository;
import com.example.springbatchstudy.domain.pay.PayHistory;
import com.example.springbatchstudy.domain.pay.PayHistoryRepository;
import com.example.springbatchstudy.domain.pay.PayStatus;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
public class AdjustStepConfig {

  private final EntityManagerFactory entityManagerFactory;


  @Bean(name = "Adjust")
  @JobScope
  public Step adjustStep(JobRepository jobRepository , PlatformTransactionManager transactionManager) {
    return new StepBuilder("adjustStep" , jobRepository)
        .<Long,WalkerAdjust>chunk(10,transactionManager)
        .reader(payHistoryItemReader())
        .processor(payHistoryItemProcessor())
        .writer(walkerAdjustDetailItemWriter())
        .build();
  }

  @Bean
  public ItemReader<Long> payHistoryItemReader() {
    Map<String, Object> parameters = new HashMap <>();
    parameters.put("status", PAY_DONE);
    JpaPagingItemReader <Long> reader = new JpaPagingItemReader <>(){
      @Override
      public int getPage() {
        return 0;
      }
    };
    reader.setName("payHistoryReader");
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setQueryString("SELECT p.userId FROM PayHistory p WHERE p.payStatus = :status GROUP BY p.userId ORDER BY p.userId");

    reader.setPageSize(10);
    reader.setParameterValues(parameters);
    return reader;
  }

  @Bean
  public ItemProcessor <Long, WalkerAdjust> payHistoryItemProcessor() {
    return payHistoryId -> {

      return WalkerAdjust.builder()
          .userId(payHistoryId)
          .walkerAdjustDate(LocalDate.now())
          .build();
    };
  }

  @Bean
  public ItemWriter<WalkerAdjust> walkerAdjustDetailItemWriter(){
    return new JpaItemWriterBuilder <WalkerAdjust>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }



}
