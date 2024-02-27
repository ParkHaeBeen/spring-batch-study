package com.example.springbatchstudy;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleJobConfig {

  @Bean
  public Job adjustJob(JobRepository jobRepository ,@Qualifier("Adjust") Step adjustStep, @Qualifier("AdjustDetail")
      Step adjustDetailStep) {
    return new JobBuilder("adjust" , jobRepository)
        .start(adjustStep)
        .next(adjustDetailStep)
        .build();
  }

}