package com.finalproject.seatudy.todoCategory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberCateDto {
    private Long memberId;
    private String email;
}
