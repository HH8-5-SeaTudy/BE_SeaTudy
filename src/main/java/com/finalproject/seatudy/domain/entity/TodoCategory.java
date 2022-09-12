package com.finalproject.seatudy.domain.entity;

import com.finalproject.seatudy.service.dto.request.TodoCategoryRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    private String selectDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @OneToMany(mappedBy = "todoCategory",fetch = FetchType.EAGER ,cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TodoList> todoList = new ArrayList<>();

    public void update(TodoCategoryRequestDto todoCategoryRequestDto){
        this.categoryName = todoCategoryRequestDto.getCategoryName();
    }

}
