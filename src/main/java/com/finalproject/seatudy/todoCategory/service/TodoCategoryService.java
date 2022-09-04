package com.finalproject.seatudy.todoCategory.service;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.entity.TodoCategory;
import com.finalproject.seatudy.entity.TodoList;
import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.login.MemberRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.todoCategory.dto.MemberCateDto;
import com.finalproject.seatudy.todoCategory.dto.TodoCategoryRequestDto;
import com.finalproject.seatudy.todoCategory.dto.TodoCategoryResponseDto;
import com.finalproject.seatudy.todoCategory.repository.TodoCategoryRepository;
import com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto;
import com.finalproject.seatudy.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class TodoCategoryService {

    private final TodoCategoryRepository todoCategoryRepository;
    private final TodoListRepository todoListRepository;
    private final MemberRepository memberRepository;
    public ResponseDto<?> createTodoCategory(UserDetailsImpl userDetails, TodoCategoryRequestDto todoCategoryRequestDto){

        Member member = userDetails.getMember();
        TodoCategory todoCategory = TodoCategory.builder()
                .member(member)
                .categoryName(todoCategoryRequestDto.getCategoryName())
                .build();
        todoCategoryRepository.save(todoCategory);

        TodoCategoryResponseDto todoCategoryResponseDto = TodoCategoryResponseDto.builder()
                .id(todoCategory.getId())
                .categoryName(todoCategory.getCategoryName())
                .memberCateDto(MemberCateDto.builder().memberId(member.getMemberId()).email(member.getEmail()).build())
                .build();

        return ResponseDto.success(todoCategoryResponseDto);
    }

    public ResponseDto<?> getTodoCategory(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<TodoCategory> todoCategories= todoCategoryRepository.findAllByMember(member);
        List<TodoCategoryResponseDto> todoCategoryResponseDtos = new ArrayList<>();
//        for (TodoList todoList : todoCategories.get().getTodoList()){
//
//        }

        for (TodoCategory todoCategory : todoCategories) {
            todoCategoryResponseDtos.add(TodoCategoryResponseDto.builder()
                    .id(todoCategory.getId())
                    .categoryName(todoCategory.getCategoryName())
                    .memberCateDto(MemberCateDto.builder().memberId(member.getMemberId()).email(member.getEmail()).build())
                    .todoList(todoCategory.getTodoList().stream().map(TodoListResponseDto::new).collect(Collectors.toList()))
                    .build());
        }
        return ResponseDto.success(todoCategoryResponseDtos);
    }

    public ResponseDto<?> updateTodoCategory(UserDetailsImpl userDetails, Long todoCategoryId, TodoCategoryRequestDto todoCategoryRequestDto) {
        todoCategoryRepository.findById(todoCategoryId).orElseThrow(
                () -> new NullPointerException("해당 카테고리가 없습니다.")
        );

        return ResponseDto.success("ok");
    }
}
