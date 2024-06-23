package com.my.bob.board.controller;

import com.my.bob.board.dto.BoardCommentCreateDto;
import com.my.bob.board.dto.BoardCreateDto;
import com.my.bob.board.dto.BoardDto;
import com.my.bob.board.dto.BoardUpdateDto;
import com.my.bob.board.service.BoardConvertService;
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

    @PostMapping
    public CommonResponse addBoard(@RequestBody BoardCreateDto dto) {
        long boardId = boardSaveService.saveNewBoard(dto);

        return new CommonResponse(boardId);
    }

    @PutMapping("/{boardId}")
    public CommonResponse updateBoard(@PathVariable long boardId,
                                      @RequestBody BoardUpdateDto dto, Principal principal) {
        String userName = principal.getName();
        boardSaveService.updateBoard(boardId, userName, dto);

        return new CommonResponse();
    }

    @GetMapping("/{boardId}")
    public CommonResponse getBoard(@PathVariable long boardId) {
        BoardDto dto = boardConvertService.convertBoardDto(boardId);
        // TODO 코멘트도 할 것

        return new CommonResponse(dto);
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

}
