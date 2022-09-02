package com.finalproject.seatudy.timeCheck.entity;

import com.finalproject.seatudy.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TimeCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkId;

    @Column
    private String date;

    @Column
    private String checkIn;

    @Column
    private String checkOut;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

}
