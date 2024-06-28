package com.my.bob.board.controller;

import com.my.bob.board.dto.*;
import com.my.bob.board.service.BoardConvertService;
import com.my.bob.board.service.BoardDeleteService;
import com.my.bob.board.service.BoardSaveService;
import com.my.bob.common.dto.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardSaveService boardSaveService;
    private final BoardConvertService boardConvertService;
    private final BoardDeleteService boardDeleteService;

    @PostMapping
    public ResponseEntity addBoard(@RequestBody BoardCreateDto dto) {
        long boardId = boardSaveService.saveNewBoard(dto);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.CREATED.value());
        responseEntity.setData(boardId);
        return responseEntity;
    }

    @GetMapping("/{boardId}")
    public ResponseEntity getBoard(@PathVariable long boardId) {
        BoardDto dto = boardConvertService.convertBoardDto(boardId);

        return new ResponseEntity(dto);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity updateBoard(@PathVariable long boardId,
                                      @RequestBody BoardUpdateDto dto, Principal principal) {
        String userName = principal.getName();
        boardSaveService.updateBoard(boardId, userName, dto);

        return new ResponseEntity(HttpStatus.NO_CONTENT.value());
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable long boardId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteBoard(boardId, requestUser);

        return new ResponseEntity(HttpStatus.NO_CONTENT.value());
    }

    // TODO 조회 조건까지 해서 추가
    @GetMapping("/list")
    public ResponseEntity getBoardList(Principal principal){
        ResponseEntity responseEntity = new ResponseEntity();

        responseEntity.setData("ok");

        return responseEntity;
    }

    /* 댓글 */
    @PostMapping("/{boardId}/comment")
    public ResponseEntity addComment(@PathVariable long boardId,
                                     @RequestBody BoardCommentCreateDto dto) {
        boardSaveService.saveNewComment(boardId, dto);

        return new ResponseEntity(HttpStatus.NO_CONTENT.value());
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity updateComment(@PathVariable long commentId,
                                        @RequestBody BoardCommentUpdateDto dto, Principal principal) {
        String requestUser = principal.getName();
        boardSaveService.updateComment(commentId, requestUser, dto);

        return new ResponseEntity(HttpStatus.NO_CONTENT.value());
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable long commentId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteComment(commentId, requestUser);

        return new ResponseEntity(HttpStatus.NO_CONTENT.value());
    }

}
