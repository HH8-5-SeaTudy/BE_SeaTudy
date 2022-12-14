package com.finalproject.seatudy.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "RANKS_ID")
    private Rank rank;

}
