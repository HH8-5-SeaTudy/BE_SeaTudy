package com.finalproject.seatudy.entity;

import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.todolist.dto.request.TodoListUpdateDto;
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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "todo_category_id")
    private TodoCategory todoCategory;

    public void update(TodoListUpdateDto todoListUpdateDto){
        this.content = todoListUpdateDto.getContent();

    }

    public void done() {
        this.done = 1;
    }
    public void cancelDone() {
        this.done = 0;
    }
}
