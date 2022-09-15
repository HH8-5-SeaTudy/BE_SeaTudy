package com.finalproject.seatudy.service;


import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.domain.repository.TodoCategoryRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.request.TodoCategoryRequestDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.service.dto.response.TodoCategoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoCategoryServiceTest {
    @InjectMocks
    private TodoCategoryService todoCategoryService;
    @Mock
    private TodoCategoryRepository todoCategoryRepository;


//    @BeforeEach
//        //테스트 시작전에 한번씩 실행
//    void setup() {
//        Member member = Member.builder()
//                .memberId(1L)
//                .email("user@naver.com")
//                .password("user12345")
//                .build();
//    }

    @Test
    @DisplayName("todo 카테고리 등록 테스트")
    void createTodoCategory_test() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        TodoCategoryRequestDto requestDto = TodoCategoryRequestDto.builder()
                .categoryName("항해99")
                .selectDate("2022-09-10")
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
//        when(todoCategoryRepository.save(any())).thenReturn(todoCategory);

        //when
        ResponseDto<?> responseDto = todoCategoryService.createTodoCategory(userDetails, requestDto);
        TodoCategoryResponseDto todoCategoryResponseDto = (TodoCategoryResponseDto) responseDto.getData();

        //then
        assertEquals(todoCategoryResponseDto.getMemberCateDto().getMemberId(), userDetails.getMember().getMemberId());
        assertEquals(todoCategoryResponseDto.getMemberCateDto().getEmail(), userDetails.getMember().getEmail());
        assertEquals(todoCategoryResponseDto.getCategoryName(), requestDto.getCategoryName());
        assertEquals(todoCategoryResponseDto.getSelectDate(), requestDto.getSelectDate());
    }

    @Test
    @DisplayName("todo 카테고리 조회 테스트")
    void getAllTodoCategory_test() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        List<TodoCategory> todoCategories = new ArrayList<>();
        todoCategories.add(TodoCategory.builder()
                .categoryId(1L)
                .categoryName("항해99")
                .selectDate("2022-09-10")
                .member(member)
                .todoList(new ArrayList<>())
                .build());
        todoCategories.add(TodoCategory.builder()
                .categoryId(2L)
                .categoryName("학교99")
                .selectDate("2022-09-15")
                .member(member)
                .todoList(new ArrayList<>())
                .build());

        todoCategories.stream().forEach((category) -> {
            System.out.println(category.getCategoryId());
            System.out.println(category.getCategoryName());
            System.out.println(category.getSelectDate());
            System.out.println(category.getMember());
            System.out.println("============");
        });

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        when(todoCategoryRepository.findAllByMember(member)).thenReturn(todoCategories);

        //when
        ResponseDto<?> responseDtos = todoCategoryService.getAllTodoCategory(userDetails);
        List<TodoCategoryResponseDto> todoCategoryResponseDtos = (List<TodoCategoryResponseDto>) responseDtos.getData();

        //then
        assertEquals(todoCategoryResponseDtos.get(0).getMemberCateDto().getMemberId(), todoCategories.get(0).getMember().getMemberId());
        assertEquals(todoCategoryResponseDtos.get(0).getMemberCateDto().getEmail(), todoCategories.get(0).getMember().getEmail());
        assertEquals(todoCategoryResponseDtos.get(0).getCategoryId(), todoCategories.get(0).getCategoryId());
        assertEquals(todoCategoryResponseDtos.get(0).getCategoryName(), todoCategories.get(0).getCategoryName());
        assertEquals(todoCategoryResponseDtos.get(0).getSelectDate(), todoCategories.get(0).getSelectDate());
        assertEquals(todoCategoryResponseDtos.get(1).getMemberCateDto().getMemberId(), todoCategories.get(1).getMember().getMemberId());
        assertEquals(todoCategoryResponseDtos.get(1).getMemberCateDto().getEmail(), todoCategories.get(1).getMember().getEmail());
        assertEquals(todoCategoryResponseDtos.get(1).getCategoryId(), todoCategories.get(1).getCategoryId());
        assertEquals(todoCategoryResponseDtos.get(1).getCategoryName(), todoCategories.get(1).getCategoryName());
        assertEquals(todoCategoryResponseDtos.get(1).getSelectDate(), todoCategories.get(1).getSelectDate());
        assertEquals(todoCategoryResponseDtos.size(), todoCategories.size());
    }

    @Test
    @DisplayName("todo 카테고리 수정 테스트")
    void updateTodoCategory_test() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("user@naver.com")
                .password("user12345")
                .build();

        TodoCategoryRequestDto requestDto = TodoCategoryRequestDto.builder()
                .categoryName("학교99")
                .selectDate("2022-09-15")
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);

        TodoCategory todoCategory = TodoCategory.builder()
                .categoryId(1L)
                .categoryName("항해99")
                .selectDate("2022-09-15")
                .member(member)
                .todoList(new ArrayList<>())
                .build();

        when(todoCategoryRepository.findById(todoCategory.getCategoryId())).thenReturn(Optional.of(todoCategory));

        //when
        ResponseDto<?> responseDto = todoCategoryService.updateTodoCategory(userDetails, todoCategory.getCategoryId(), requestDto);
        TodoCategoryResponseDto todoCategoryResponseDto = (TodoCategoryResponseDto) responseDto.getData();

        //then
        assertEquals(todoCategoryResponseDto.getMemberCateDto().getMemberId(), userDetails.getMember().getMemberId());
        assertEquals(todoCategoryResponseDto.getMemberCateDto().getEmail(), userDetails.getMember().getEmail());
        assertEquals(todoCategoryResponseDto.getCategoryId(), todoCategory.getCategoryId());
        assertEquals(todoCategoryResponseDto.getCategoryName(), requestDto.getCategoryName());
        assertEquals(todoCategoryResponseDto.getSelectDate(), requestDto.getSelectDate());
    }

    @Test
    @DisplayName("todo 카테고리 삭제 테스트")
    void deleteTodoCategory() {
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
                .member(member)
                .build();

        //stub
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        Optional<TodoCategory> todoCategoryOp = Optional.of(todoCategory);
        when(todoCategoryRepository.findById(todoCategory.getCategoryId())).thenReturn(todoCategoryOp);
        //when
        ResponseDto<?> responseDto = todoCategoryService.deleteTodoCategory(userDetails, todoCategory.getCategoryId());

        //then
        assertEquals(responseDto.getData(), "삭제가 완료되었습니다.");
    }
}