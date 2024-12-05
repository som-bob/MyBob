package com.my.bob.board.service.mock;

import com.my.bob.core.domain.board.dto.BoardSearchDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.v1.board.repository.BoardQueryRepository;
import com.my.bob.v1.board.repository.BoardRepository;
import com.my.bob.v1.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardQueryRepository boardQueryRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("게시물 저장 테스트")
    void testSaveBoard(){
        // given
        Board board = new Board("제목", "내용");

        // when
        boardService.save(board);

        // then
        // save 메서드가 정확히 한 번 호출 되었는지 검증
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("ID로 게시물 검색 테스트")
    void getById(){
        // given
        long boardId = 1L;
        Board board = new Board("제목", "내용");
        when(boardRepository.findById(boardId)).thenReturn(java.util.Optional.of(board));

        // when
        Board result = boardService.getById(boardId);

        // then
        assertThat(result).isEqualTo(board);
    }

    @Test
    @DisplayName("검색 조선으로 게시물 검색 테스트")
    void testGetBySearch(){
        // given
        BoardSearchDto searchDto = new BoardSearchDto();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Board> page = new PageImpl<>(Collections.singletonList(new Board("제목", "내용")));

        // boardQueryRepository의 search 호출 시 특정 값을 반환하도록 설정
        when(boardQueryRepository.getBoardList(searchDto, pageable)).thenReturn(page);

        // when
        Page<Board> result = boardService.getBySearch(searchDto, pageable);

        // then
        assertThat(result).isEqualTo(page);
    }
}