package com.finalproject.seatudy.service.dto.request;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.domain.entity.TodoList;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoListRequestDto {

    private String selectDate;
    private String content;

//    public TodoList toEntity(Member member, TodoCategory todoCategory){
//
//        return TodoList.builder()
//                .selectDate(selectDate)
//                .content(content)
//                .todoCategory(todoCategory)
//                .member(member)
//                .build();
//    }
}
