package com.finalproject.seatudy.todolist.service;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.entity.TodoCategory;
import com.finalproject.seatudy.entity.TodoList;
import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.login.MemberRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.todoCategory.dto.response.TodoCateShortResDto;
import com.finalproject.seatudy.todoCategory.repository.TodoCategoryRepository;
import com.finalproject.seatudy.todolist.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.todolist.dto.request.TodoListUpdateDto;
import com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto;
import com.finalproject.seatudy.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto.*;


@Service
@RequiredArgsConstructor
@Transactional
public class TodoListService {

    private final TodoListRepository todolistRepository;
    private final TodoCategoryRepository todoCategoryRepository;
    private final MemberRepository memberRepository;

    //todo 리스트 생성
    public ResponseDto<?> createTodoList(UserDetailsImpl userDetails,Long todoCategoryId,TodoListRequestDto todoListRequestDto){
        Member member = userDetails.getMember();
        TodoCategory todoCategory = getTodoCategory(todoCategoryId);

        if(!member.getEmail().equals(todoCategory.getMember().getEmail())){
            throw new RuntimeException("사용자 권한이 없습니다.");
        }

        TodoList todoList = TodoList.builder()
            .selectDate(todoListRequestDto.getSelectDate())
            .content(todoListRequestDto.getContent())
            .todoCategory(todoCategory)
            .member(member)
            .build();
        todolistRepository.save(todoList);
        TodoListResDto todoListResDto = TodoListResDto.builder()
            .todoId(todoList.getTodoId())
            .content(todoList.getContent())
            .selectDate(todoList.getSelectDate())
            .build();

    return ResponseDto.success(todoListResDto);

    }

    //todo 리스트 수정
    public ResponseDto<?> updateTodoList(UserDetailsImpl userDetails, Long todoId, TodoListUpdateDto todoListUpdateDto){
        Member member = userDetails.getMember();
        TodoList todoList = todolistRepository.findById(todoId).orElseThrow(
                () -> new RuntimeException("해당 todolist가 없습니다")
        );

        if(!member.getEmail().equals(todoList.getMember().getEmail())){
            throw new RuntimeException("사용자 권한이 없습니다.");
        }

        todoList.update(todoListUpdateDto);

        TodoListResDto todoListResDto = TodoListResDto.builder()
                .todoId(todoList.getTodoId())
                .content(todoList.getContent())
                .selectDate(todoList.getSelectDate())
                .done(todoList.getDone())
                .build();
        return ResponseDto.success(todoListResDto);

    }

    //todo 리스트 삭제
    public ResponseDto<?> deleteTodoList(UserDetailsImpl userDetails,Long todoId){
//        Member member1 = memberRepository.findByEmail(userDetails.getMember().getEmail()).orElsethrow(
//                () -> new RuntimeException("NON_EXISTENT_USER"));
//
        Member member = userDetails.getMember();
        TodoList todolist = todolistRepository.findById(todoId).orElseThrow(
                () -> new RuntimeException("해당 todolist가 없습니다.")
        );
        if(!member.getEmail().equals(todolist.getMember().getEmail())){
            throw new RuntimeException("사용자 권한이 없습니다.");
        }
        todolist.getTodoCategory().getCategoryId();
        todolistRepository.delete(todolist);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

    //todo 리스트 완료
    public ResponseDto<?> completeTodoList(UserDetailsImpl userDetails,Long todoId) {
        Member member = userDetails.getMember();
        TodoList todoListDone = todolistRepository.findById(todoId).orElseThrow(
                () -> new RuntimeException("해당 todolist가 없습니다")
        );
        if(!member.getEmail().equals(todoListDone.getMember().getEmail())){
            throw new RuntimeException("사용자 권한이 없습니다.");
        }
        if(todoListDone.getDone()==1){
            todoListDone.cancelDone();
            return ResponseDto.success("취소하였습니다");
        }
        todoListDone.done();
        return ResponseDto.success("할일을 완료하였습니다.");
    }
    //선택한 연 월 todolist 조회
    public ResponseDto<?> getTodoList(UserDetailsImpl userDetails,String selectDate) {
        List<TodoList> todoLists = todolistRepository.findAllBySelectDateContaining(selectDate);
        List<TodoCateResDto> todoCateResDtos = new ArrayList<>();

        Member member1 = memberRepository.findByEmail(userDetails.getMember().getEmail()).orElseThrow(
                () -> new RuntimeException("NOT_EXISTENT_USER")
        );

        for (TodoList todoList : todoLists){
            todoCateResDtos.add(TodoCateResDto.builder()
                    .todoCateShortResDto(TodoCateShortResDto.builder().todoCategoryId(todoList.getTodoCategory().getCategoryId()).todoCategoryName(todoList.getTodoCategory().getCategoryName()).build())
                    .todoId(todoList.getTodoId())
                    .content(todoList.getContent())
                    .selectDate(todoList.getSelectDate())
                    .done(todoList.getDone())
                    .build()
            );

        }
        return ResponseDto.success(todoCateResDtos);
    }

    private TodoCategory getTodoCategory(Long todoCategoryId){
        return todoCategoryRepository.findById(todoCategoryId).orElseThrow(
                () -> new RuntimeException("카테고리가 존재하지 않습니다.")
        );
    }
}
