package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.service.dto.request.TodoListUpdateDto;
import com.finalproject.seatudy.service.TodoListService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TodoListController {

    private final TodoListService todolistService;

    @PostMapping("/{todoCategoryId}/todoLists")
    @ApiOperation(value = "할일 리스트 생성")
    @ApiImplicitParam(name = "todoCategoryId", value = "할일 카테고리 아이디")
    public ResponseDto<?> createTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long todoCategoryId,
                                         @RequestBody TodoListRequestDto todoListRequestDto){
        return todolistService.createTodoList(userDetails,todoCategoryId,todoListRequestDto);
    }

    @PutMapping("/todoLists/{todoId}")
    @ApiOperation(value = "할일 리스트 수정")
    @ApiImplicitParam(name = "todoId", value = "할일 아이디")
    public ResponseDto<?> updateTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long todoId,
                                         @RequestBody TodoListUpdateDto todoListUpdateDto){
        return todolistService.updateTodoList(userDetails,todoId,todoListUpdateDto);
    }

    @DeleteMapping("/todoLists/{todoId}")
    @ApiOperation(value = "할일 리스트 삭제")
    @ApiImplicitParam(name = "todoId", value = "할일 아이디")
    public ResponseDto<?> deleteTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long todoId){
        return todolistService.deleteTodoList(userDetails,todoId);
    }

    @PostMapping("/todoLists/{todoId}")
    @ApiOperation(value = "할일 리스트 완료")
    @ApiImplicitParam(name = "todoId", value = "할일 아이디")
    public ResponseDto<?> completeTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long todoId){
        return todolistService.completeTodoList(userDetails,todoId);
    }
}
