package com.finalproject.seatudy.Dday;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DdayRequestDto {
    private String title;
    private String targetDay;

}
