package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.domain.entity.TodoList;
import com.finalproject.seatudy.domain.repository.TodoCategoryRepository;
import com.finalproject.seatudy.domain.repository.TodoListRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.service.dto.request.TodoListUpdateDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.service.dto.response.TodoListResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class TodoListServiceTest {
    @InjectMocks
    private TodoListService todoListService;
    @Mock
    private TodoListRepository todoListRepository;
    @Mock
    private TodoCategoryRepository todoCategoryRepository;

    @Test
    @DisplayName("todo 리스트 등록 테스트")
    void createTodoList_test() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        TodoCategory todoCategory = TodoCategory.builder()
                .categoryId(1L)
                .categoryName("항해99")
                .selectDate("2022-09-10")
                .todoList(new ArrayList<>())
                .member(member)
                .build();

        TodoListRequestDto requestDto = TodoListRequestDto.builder()
                .selectDate("2022-09-10")
                .content("단위테스트 작성중입니다.")
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        when(todoCategoryRepository.findById(todoCategory.getCategoryId())).thenReturn(Optional.of(todoCategory));
//        when(todoListRepository.save(any())).thenReturn(todoList);

        //when
        ResponseDto<?> responseDto = todoListService.createTodoList(userDetails, todoCategory.getCategoryId(), requestDto);
        TodoListResponseDto.TodoListResDto todoListResDto = (TodoListResponseDto.TodoListResDto) responseDto.getData();

        //then
        assertEquals(todoListResDto.getSelectDate(), requestDto.getSelectDate());
        assertEquals(todoListResDto.getContent(), requestDto.getContent());
        assertEquals(todoListResDto.getDone(), 0);
    }

    @Test
    @DisplayName("todo 리스트 수정 테스트")
    void updateTodoList() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        TodoCategory todoCategory = TodoCategory.builder()
                .categoryId(1L)
                .categoryName("항해99")
                .selectDate("2022-09-10")
                .todoList(new ArrayList<>())
                .member(member)
                .build();

        TodoListUpdateDto updateDto = TodoListUpdateDto.builder()
                .content("오류 해결중입니다.")
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);

        TodoList todoList = TodoList.builder()
                .todoId(1L)
                .selectDate("2022-09-10")
                .content("단위테스트 작성중입니다.")
                .todoCategory(todoCategory)
                .member(member)
                .build();

        when(todoListRepository.findById(todoList.getTodoId())).thenReturn(Optional.of(todoList));

        //then
        ResponseDto<?> responseDto = todoListService.updateTodoList(userDetails, todoList.getTodoId(), updateDto);
        TodoListResponseDto.TodoListResDto todoListResDto = (TodoListResponseDto.TodoListResDto) responseDto.getData();

        //then
        assertEquals(todoList.getMember().getEmail(), userDetails.getMember().getEmail());
        assertEquals(todoListResDto.getTodoId(), todoList.getTodoId());
        assertEquals(todoListResDto.getContent(), updateDto.getContent());
        assertEquals(todoListResDto.getSelectDate(), todoList.getSelectDate());
        assertEquals(todoListResDto.getSelectDate(), todoCategory.getSelectDate());
    }

    @Test
    @DisplayName("todo 리스트 삭제 테스트")
    void deleteTodoList() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        TodoCategory todoCategory = TodoCategory.builder()
                .categoryId(1L)
                .categoryName("항해99")
                .selectDate("2022-09-10")
                .todoList(new ArrayList<>())
                .member(member)
                .build();

        TodoList todoList = TodoList.builder()
                .todoId(1L)
                .selectDate("2022-09-10")
                .content("단위테스트 작성중입니다.")
                .todoCategory(todoCategory)
                .member(member)
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        when(todoListRepository.findById(todoList.getTodoId())).thenReturn(Optional.of(todoList));

        //when
        ResponseDto<?> responseDto = todoListService.deleteTodoList(userDetails, todoList.getTodoId());

        //then
        assertEquals(responseDto.getData(), "삭제가 완료되었습니다.");

    }

    @Test
    @DisplayName("todo 리스트 완료 테스트")
    void completeTodoList() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        TodoCategory todoCategory = TodoCategory.builder()
                .categoryId(1L)
                .categoryName("항해99")
                .selectDate("2022-09-10")
                .todoList(new ArrayList<>())
                .member(member)
                .build();

        TodoList todoList1 = TodoList.builder()
                .todoId(1L)
                .selectDate("2022-09-10")
                .content("단위테스트 작성중입니다.")
                .done(0)
                .todoCategory(todoCategory)
                .member(member)
                .build();

        TodoList todoList2 = TodoList.builder()
                .todoId(2L)
                .selectDate("2022-09-10")
                .content("단위테스트 작성중입니다.")
                .done(1)
                .todoCategory(todoCategory)
                .member(member)
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        when(todoListRepository.findById(todoList1.getTodoId())).thenReturn(Optional.of(todoList1));
        when(todoListRepository.findById(todoList2.getTodoId())).thenReturn(Optional.of(todoList2));

        //when
        ResponseDto<?> responseDto1 = todoListService.completeTodoList(userDetails, todoList1.getTodoId());
        ResponseDto<?> responseDto2 = todoListService.completeTodoList(userDetails, todoList2.getTodoId());

        //then
        assertEquals(responseDto1.getData(), "할일을 완료하였습니다.");
        assertEquals(responseDto2.getData(), "취소하였습니다");
    }

    @Test
    @DisplayName("todo 리스트 조회 테스트")
    void getTodoList() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        TodoCategory todoCategory = TodoCategory.builder()
                .categoryId(1L)
                .categoryName("항해99")
                .selectDate("2022-09-10")
                .todoList(new ArrayList<>())
                .member(member)
                .build();

        String selectDate = "2022-09-10";

        List<TodoList> todoLists = new ArrayList<>();
        todoLists.add(TodoList.builder()
                .todoId(1L)
                .selectDate("2022-09-10")
                .content("단위테스트 작성중입니다.")
                .todoCategory(todoCategory)
                .member(member)
                .build());
        todoLists.add(TodoList.builder()
                .todoId(2L)
                .selectDate("2022-09-15")
                .content("오류 해결중입니다.")
                .todoCategory(todoCategory)
                .member(member)
                .build());
        todoLists.add(TodoList.builder()
                .todoId(3L)
                .selectDate("2022-09-10")
                .content("강의 시청중입니다.")
                .todoCategory(todoCategory)
                .member(member)
                .build());


        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        when(todoListRepository.findAllBySelectDateContaining(selectDate)).thenReturn(todoLists.stream()
                .filter(todoList -> todoList.getSelectDate().equals(selectDate))
                .collect(Collectors.toList())
        );

        //when
        ResponseDto<?> responseDto = todoListService.getTodoList(userDetails, selectDate);
        List<TodoListResponseDto.TodoCateResDto> todoCateResDtos = (List<TodoListResponseDto.TodoCateResDto>) responseDto.getData();

        //then
        assertEquals(todoCateResDtos.get(0).getTodoCateShortResDto().getSelectDate(), todoLists.get(0).getTodoCategory().getSelectDate());
        assertEquals(todoCateResDtos.get(0).getSelectDate(), todoLists.get(0).getSelectDate());
        assertEquals(todoCateResDtos.get(1).getTodoCateShortResDto().getSelectDate(), todoLists.get(2).getTodoCategory().getSelectDate());
        assertEquals(todoCateResDtos.get(1).getSelectDate(), todoLists.get(2).getSelectDate());
        assertEquals(todoCateResDtos.size(), todoLists.stream().filter(todoList -> todoList.getSelectDate().equals(selectDate))
                .collect(Collectors.toList()).size());
    }

}