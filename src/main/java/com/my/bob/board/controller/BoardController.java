package com.my.bob.board.controller;

import com.my.bob.board.dto.BoardCreateDto;
import com.my.bob.board.dto.BoardUpdateDto;
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

    @PostMapping
    public CommonResponse createBoard(@RequestBody BoardCreateDto dto) {
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


    @GetMapping("/list")
    public CommonResponse getBoardList(Principal principal){
        CommonResponse commonResponse = new CommonResponse();

        commonResponse.setData("ok");

        return commonResponse;
    }

}
