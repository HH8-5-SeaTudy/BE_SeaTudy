package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.TodoList;
import lombok.*;

public class TodoListResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoListResDto {
        private Long todoId;
        private String content;
        private String selectDate;
        private int done;

        public TodoListResDto(TodoList todoList) {
            this.todoId = todoList.getTodoId();
            this.content = todoList.getContent();
            this.selectDate = todoList.getSelectDate();
            this.done = todoList.getDone();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoCateResDto {
        private TodoCateShortResDto todoCateShortResDto;
        private Long todoId;
        private String content;
        private String selectDate;
        private int done;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoListCateResDto {
        private Long todoId;
        private String content;
        private int done;
        private Long categoryId;
        private String selectDate;

        public static TodoListCateResDto fromEntity(TodoList todoList) {
            return new TodoListCateResDto(
                    todoList.getTodoId(),
                    todoList.getContent(),
                    todoList.getDone(),
                    todoList.getTodoCategory().getCategoryId(),
                    todoList.getSelectDate());
        }
    }
}
