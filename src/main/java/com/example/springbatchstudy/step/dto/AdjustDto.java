package com.example.springbatchstudy.step.dto;

import com.example.springbatchstudy.domain.adjust.WalkerAdjust;
import com.example.springbatchstudy.domain.pay.PayHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdjustDto {

  private PayHistory payHistory;
  private WalkerAdjust walkerAdjust;
}
