package com.example.springbatchstudy.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
public class SimpleTasklet implements Tasklet , InitializingBean {

  @Override
  public RepeatStatus execute(StepContribution contribution , ChunkContext chunkContext)
      throws Exception {

    return RepeatStatus.FINISHED;
  }

  @Override
  public void afterPropertiesSet() throws Exception {

  }
}
