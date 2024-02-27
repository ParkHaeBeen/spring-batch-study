package com.example.springbatchstudy.domain.pay;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PayHistoryRepository extends JpaRepository<PayHistory,Long> {

  List <PayHistory> findByPayStatusOrderByUserId(PayStatus payStatus);
}
