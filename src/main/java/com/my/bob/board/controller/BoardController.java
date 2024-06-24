package com.my.bob.board.controller;

import com.my.bob.board.dto.*;
import com.my.bob.board.service.BoardConvertService;
import com.my.bob.board.service.BoardDeleteService;
import com.my.bob.board.service.BoardSaveService;
import com.my.bob.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public CommonResponse addBoard(@RequestBody BoardCreateDto dto) {
        long boardId = boardSaveService.saveNewBoard(dto);

        return new CommonResponse(boardId);
    }

    @GetMapping("/{boardId}")
    public CommonResponse getBoard(@PathVariable long boardId) {
        BoardDto dto = boardConvertService.convertBoardDto(boardId);

        return new CommonResponse(dto);
    }

    @PutMapping("/{boardId}")
    public CommonResponse updateBoard(@PathVariable long boardId,
                                      @RequestBody BoardUpdateDto dto, Principal principal) {
        String requestUser = principal.getName();
        boardSaveService.updateBoard(boardId, requestUser, dto);

        return new CommonResponse();
    }

    @DeleteMapping("/{boardId}")
    public CommonResponse deleteBoard(@PathVariable long boardId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteBoard(boardId, requestUser);

        return new CommonResponse();
    }

    // TODO 조회 조건까지 해서 추가
    @GetMapping("/list")
    public CommonResponse getBoardList(Principal principal){
        CommonResponse commonResponse = new CommonResponse();

        commonResponse.setData("ok");

        return commonResponse;
    }

    /* 댓글 */
    @PostMapping("/{boardId}/comment")
    public CommonResponse addComment(@PathVariable long boardId,
                                     @RequestBody BoardCommentCreateDto dto) {
        boardSaveService.saveNewComment(boardId, dto);

        return new CommonResponse();
    }

    @PutMapping("/comment/{commentId}")
    public CommonResponse updateComment(@PathVariable long commentId,
                                        @RequestBody BoardCommentUpdateDto dto, Principal principal) {
        String requestUser = principal.getName();
        boardSaveService.updateComment(commentId, requestUser, dto);

        return new CommonResponse();
    }

    @DeleteMapping("/comment/{commentId}")
    public CommonResponse deleteComment(@PathVariable long commentId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteComment(commentId, requestUser);

        return new CommonResponse();
    }

}
