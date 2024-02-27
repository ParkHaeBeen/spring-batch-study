package com.example.springbatchstudy.domain.adjust;


import static com.example.springbatchstudy.domain.adjust.AdjustDetailStatus.ADJUST_NOT_YET;

import com.example.springbatchstudy.domain.pay.PayHistory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "walker_adjust_detail")
public class WalkerAdjustDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "walker_adjust_detail_id")
  private Long walkerAdjustDetailId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "walker_adjust_id", nullable = false)
  private WalkerAdjust walkerAdjust;


  @Column(name = "walker_adjust_price",nullable = false)
  private Long walkerAdjustPrice;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "pay_history_id")
  private PayHistory payHistory;

  @Column(name = "walker_adjust_status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private AdjustDetailStatus adjustDetailStatus= ADJUST_NOT_YET;

  public void setWalkerAdjust(final WalkerAdjust walkerAdjust){
    this.walkerAdjust=walkerAdjust;
  }
}
