package com.finalproject.seatudy.domain.entity;

import com.finalproject.seatudy.service.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.service.dto.request.TodoListUpdateDto;
import lombok.*;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private String content;

    private String selectDate;

    private int done;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_category_id")
    private TodoCategory todoCategory;

    public void update(TodoListUpdateDto todoListUpdateDto){
        this.content = todoListUpdateDto.getContent();

    }

    public void changeDone(int done) {
        this.done = done;
    }
}
