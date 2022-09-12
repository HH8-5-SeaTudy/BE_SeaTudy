package com.finalproject.seatudy.service.dto.request;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoCategoryRequestDto {

    private String categoryName;
    private String selectDate;

    public TodoCategory toEntity(Member member) {

        return TodoCategory.builder()
                .categoryName(categoryName)
                .selectDate(selectDate)
                .member(member)
                .build();
    }
}
