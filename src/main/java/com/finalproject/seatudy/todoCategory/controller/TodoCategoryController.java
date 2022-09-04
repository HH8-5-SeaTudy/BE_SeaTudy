package com.finalproject.seatudy.todoCategory.controller;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.todoCategory.dto.TodoCategoryRequestDto;
import com.finalproject.seatudy.todoCategory.service.TodoCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoCategoryController {

    private final TodoCategoryService todoCategoryService;

    //todo 카테고리 생성
    @PostMapping("/api/v1/todoCategories")
    public ResponseDto<?> createTodoCategory (@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoCategoryRequestDto todoCategoryRequestDto) {
        return todoCategoryService.createTodoCategory(userDetails,todoCategoryRequestDto);
    }

    //todo 카테고리 조회
    @GetMapping("/api/v1/todoCategories")
    public ResponseDto<?> getTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoCategoryService.getTodoCategory(userDetails);
    }

    //todo 카테고리 수정
    @PutMapping("/api/v1/todoCategories{todoCategoryId}")
    public ResponseDto<?> updateTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long todoCategoryId, @RequestBody TodoCategoryRequestDto todoCategoryRequestDto) {
        return todoCategoryService.updateTodoCategory(userDetails,todoCategoryId,todoCategoryRequestDto);
    }
}
