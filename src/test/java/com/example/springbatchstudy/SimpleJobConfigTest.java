package com.example.springbatchstudy;

import static org.junit.jupiter.api.Assertions.*;

import com.example.springbatchstudy.domain.adjust.WalkerAdjustRepository;
import com.example.springbatchstudy.domain.pay.PayHistory;
import com.example.springbatchstudy.domain.pay.PayHistoryRepository;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@SpringBatchTest
class SimpleJobConfigTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private PayHistoryRepository payHistoryRespository;
  @Autowired
  private WalkerAdjustRepository walkerAdjustRepository;


  @Test
  void test() throws Exception {
    JobParameters jobParameters=new JobParametersBuilder()
      .addLong("time",2L)
      .toJobParameters();
    jobLauncherTestUtils.launchJob(jobParameters);

  }

  @Test
  void test2(){
    payHistoryRespository.deleteAll();
    for(int i =0;i<80;i++){
      payHistoryRespository.save(PayHistory.builder()
          .payPrice((long) i)
          .userId(1L+i/10)
          .payMethod("CARD")
          .build());

    }
    System.out.println(payHistoryRespository.findAll().size());
  }
}