package com.my.bob.board.controller;

import com.my.bob.board.dto.*;
import com.my.bob.board.service.BoardConvertService;
import com.my.bob.board.service.BoardDeleteService;
import com.my.bob.board.service.BoardSaveService;
import com.my.bob.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public CommonResponse getBoardList(@ModelAttribute BoardSearchDto dto,
                                       Pageable pageable){
        CommonResponse commonResponse = new CommonResponse();

        Page<BoardTitleDto> pageDtoList = boardConvertService.convertBoardList(dto, pageable);
        commonResponse.setData(pageDtoList);
        return commonResponse;
    }

    @PostMapping
    public CommonResponse addBoard(@RequestBody BoardCreateDto dto) {
        long boardId = boardSaveService.saveNewBoard(dto);

        CommonResponse commonResponse = new CommonResponse(HttpStatus.CREATED.value());
        commonResponse.setData(boardId);
        return commonResponse;
    }

    @GetMapping("/{boardId}")
    public CommonResponse getBoard(@PathVariable long boardId) {
        BoardDto dto = boardConvertService.convertBoardDto(boardId);

        return new CommonResponse(dto);
    }

    @PutMapping("/{boardId}")
    public CommonResponse updateBoard(@PathVariable long boardId,
                                      @RequestBody BoardUpdateDto dto, Principal principal) {
        String userName = principal.getName();
        boardSaveService.updateBoard(boardId, userName, dto);

        return new CommonResponse(HttpStatus.NO_CONTENT.value());
    }

    @DeleteMapping("/{boardId}")
    public CommonResponse deleteBoard(@PathVariable long boardId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteBoard(boardId, requestUser);

        return new CommonResponse(HttpStatus.NO_CONTENT.value());
    }



    /* 댓글 */
    @PostMapping("/{boardId}/comment")
    public CommonResponse addComment(@PathVariable long boardId,
                                     @RequestBody BoardCommentCreateDto dto) {
        boardSaveService.saveNewComment(boardId, dto);

        return new CommonResponse(HttpStatus.NO_CONTENT.value());
    }

    @PutMapping("/comment/{commentId}")
    public CommonResponse updateComment(@PathVariable long commentId,
                                        @RequestBody BoardCommentUpdateDto dto, Principal principal) {
        String requestUser = principal.getName();
        boardSaveService.updateComment(commentId, requestUser, dto);

        return new CommonResponse(HttpStatus.NO_CONTENT.value());
    }

    @DeleteMapping("/comment/{commentId}")
    public CommonResponse deleteComment(@PathVariable long commentId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteComment(commentId, requestUser);

        return new CommonResponse(HttpStatus.NO_CONTENT.value());
    }

}
