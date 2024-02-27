package com.example.springbatchstudy.domain.adjust;

import com.example.springbatchstudy.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "walker_adjust")
public class WalkerAdjust extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "walker_adjust_id")
  private Long walkerAdjustId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "walker_adjust_date", nullable = false)
  private LocalDate walkerAdjustDate;

  @Column(name = "walker_ttlprice", nullable = false)
  @Builder.Default
  private Long walkerTtlPrice=0L;

  @Column(name = "walker_adjust_status",nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private AdjustStatus walkerAdjustStatus=AdjustStatus.ADJUST_NOT_YET;


  @OneToMany(mappedBy = "walkerAdjust",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
  @Builder.Default
  private List <WalkerAdjustDetail> adjustDetailList=new ArrayList <>();

  public void setPrice(Long price){
    this.walkerTtlPrice = price;
  }

  public void addAdjustDetail(WalkerAdjustDetail adjustDetail){
    this.adjustDetailList.add(adjustDetail);
  }
}
