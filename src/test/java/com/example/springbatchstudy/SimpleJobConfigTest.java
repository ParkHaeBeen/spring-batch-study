package com.example.springbatchstudy;

import static org.junit.jupiter.api.Assertions.*;

import com.example.springbatchstudy.domain.adjust.WalkerAdjustRepository;
import com.example.springbatchstudy.domain.pay.PayHistory;
import com.example.springbatchstudy.domain.pay.PayHistoryRepository;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

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
    jobLauncherTestUtils.launchJob();

  }

  @Test
  void test2(){
    payHistoryRespository.deleteAll();
    for(int i =0;i<550;i++){
      payHistoryRespository.save(PayHistory.builder()
          .payPrice((long) i)
          .userId(1L+i/100)
          .payMethod("CARD")
          .build());

    }
    System.out.println(payHistoryRespository.findAll().size());
  }
}