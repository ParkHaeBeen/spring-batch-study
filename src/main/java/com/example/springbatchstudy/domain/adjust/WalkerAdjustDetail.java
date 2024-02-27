package com.example.springbatchstudy.domain.adjust;


import static com.example.springbatchstudy.domain.adjust.AdjustDetailStatus.ADJUST_NOT_YET;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
  @JoinColumn(name = "walker_adjust_id", nullable = false)
  private WalkerAdjust walkerAdjust;


  @Column(name = "walker_adjust_price",nullable = false)
  private Long walkerAdjustPrice;

  private Long payhistoryId;
  @Column(name = "walker_adjust_status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private AdjustDetailStatus adjustDetailStatus= ADJUST_NOT_YET;

  public void setWalkerAdjust(final WalkerAdjust walkerAdjust){
    this.walkerAdjust=walkerAdjust;
  }
}
