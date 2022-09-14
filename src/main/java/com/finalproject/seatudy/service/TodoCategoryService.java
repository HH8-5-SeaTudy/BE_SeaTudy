package com.finalproject.seatudy.service;

import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.domain.entity.TodoList;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.response.MemberCateDto;
import com.finalproject.seatudy.service.dto.request.TodoCategoryRequestDto;
import com.finalproject.seatudy.service.dto.response.TodoCategoryResponseDto;
import com.finalproject.seatudy.domain.repository.TodoCategoryRepository;
import com.finalproject.seatudy.domain.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.finalproject.seatudy.service.dto.response.TodoListResponseDto.*;


@RequiredArgsConstructor
@Transactional
@Service
public class TodoCategoryService {

    private final TodoCategoryRepository todoCategoryRepository;
    private final TodoListRepository todoListRepository;
    public ResponseDto<?> createTodoCategory(UserDetailsImpl userDetails, TodoCategoryRequestDto todoCategoryRequestDto){
        Member member = userDetails.getMember();
            TodoCategory todoCategory = TodoCategory.builder()
                    .member(member)
                    .categoryName(todoCategoryRequestDto.getCategoryName())
                    .selectDate(todoCategoryRequestDto.getSelectDate())
                    .build();
            todoCategoryRepository.save(todoCategory);

            TodoCategoryResponseDto todoCategoryResponseDto = TodoCategoryResponseDto.builder()
                    .categoryId(todoCategory.getCategoryId())
                    .categoryName(todoCategory.getCategoryName())
                    .selectDate(todoCategory.getSelectDate())
                    .memberCateDto(MemberCateDto.builder().memberId(member.getMemberId()).email(member.getEmail()).build())
                    .build();

        System.out.println(todoCategoryResponseDto.getCategoryId());
            return ResponseDto.success(todoCategoryResponseDto);
    }

    public ResponseDto<?> getAllTodoCategory(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<TodoCategory> todoCategories= todoCategoryRepository.findAllByMember(member);
        List<TodoCategoryResponseDto> todoCategoryResponseDtos = new ArrayList<>();

        for (TodoCategory todoCategory : todoCategories) {
            todoCategoryResponseDtos.add(TodoCategoryResponseDto.builder()
                    .categoryId(todoCategory.getCategoryId())
                    .categoryName(todoCategory.getCategoryName())
                    .selectDate(todoCategory.getSelectDate())
                    .memberCateDto(MemberCateDto.builder().memberId(member.getMemberId()).email(member.getEmail()).build())
                    .todoList(todoCategory.getTodoList().stream().map(TodoListResDto::new).collect(Collectors.toList()))
                    .build());
        }
        return ResponseDto.success(todoCategoryResponseDtos);
    }

    public ResponseDto<?> updateTodoCategory(UserDetailsImpl userDetails, Long todoCategoryId, TodoCategoryRequestDto todoCategoryRequestDto) {
        Member member = userDetails.getMember();
        TodoCategory todoCategory = todoCategoryRepository.findById(todoCategoryId).orElseThrow(
                () -> new NullPointerException("해당 카테고리가 없습니다.")
        );

        if(!member.getEmail().equals(todoCategory.getMember().getEmail())){
            return ResponseDto.fail("UNAUTHENTICATED","사용자만 조회 가능합니다.");
        }
        todoCategory.update(todoCategoryRequestDto);

        TodoCategoryResponseDto todoCategoryResponseDto = TodoCategoryResponseDto.builder()
                .categoryId(todoCategory.getCategoryId())
                .categoryName(todoCategory.getCategoryName())
                .selectDate(todoCategory.getSelectDate())
                .memberCateDto(MemberCateDto.builder().memberId(member.getMemberId()).email(member.getEmail()).build())
                .todoList(todoCategory.getTodoList().stream().map(TodoListResDto::new).collect(Collectors.toList()))
                .build();

        return ResponseDto.success(todoCategoryResponseDto);
    }

    public ResponseDto<?> getTodoCategory(UserDetailsImpl userDetails, String selectDate) {
        Member member = userDetails.getMember();
        List<TodoCategory> todoCategories= todoCategoryRepository.findAllBySelectDateContaining(selectDate);
        List<TodoList> todoLists = todoListRepository.findAllBySelectDateContaining(selectDate);
        List<TodoCategoryResponseDto> todoCategoryResponseDtos = new ArrayList<>();

        for (TodoCategory todoCategory : todoCategories){
            todoCategoryResponseDtos.add(TodoCategoryResponseDto.builder()
                    .categoryId(todoCategory.getCategoryId())
                    .categoryName(todoCategory.getCategoryName())
                    .selectDate(todoCategory.getSelectDate())
                    .memberCateDto(MemberCateDto.builder().memberId(member.getMemberId()).email(member.getEmail()).build())
                    .todoList(todoLists.stream().filter(todoList -> todoList.getTodoCategory().equals(todoCategory))
                            .map(TodoListResDto::new).collect(Collectors.toList()))
                    .build());

        }
        return ResponseDto.success(todoCategoryResponseDtos);
    }

    public ResponseDto<?> deleteTodoCategory(UserDetailsImpl userDetails, Long todoCategoryId) {
        Member member = userDetails.getMember();
        TodoCategory todoCategory = todoCategoryRepository.findById(todoCategoryId).orElseThrow(
                () -> new RuntimeException("해당 카테고리가 없습니다.")
        );
        if(!member.getEmail().equals(todoCategory.getMember().getEmail())){
            throw new RuntimeException("사용자 권한이 없습니다.");
        }
        todoCategoryRepository.delete(todoCategory);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }
}
