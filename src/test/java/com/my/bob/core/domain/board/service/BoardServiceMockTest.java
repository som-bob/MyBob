package com.my.bob.core.domain.board.service;

import com.my.bob.core.domain.board.dto.request.BoardSearchDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.domain.board.repository.BoardQueryRepositoryImpl;
import com.my.bob.core.domain.board.repository.BoardRepository;
import com.my.bob.v1.board.service.BoardServiceImpl;
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

@DisplayName("Mock 게시판 테스트")
@ExtendWith(MockitoExtension.class)
class BoardServiceMockTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardQueryRepositoryImpl boardQueryRepositoryImpl;

    // spring boot test 아니기 때문에 실제 객체 필요
    @InjectMocks
    private BoardServiceImpl boardServiceImpl;

    @Test
    @DisplayName("Mock 게시물 저장 테스트")
    void testSaveBoard(){
        // given
        Board board = new Board("제목", "내용");

        // when
        boardServiceImpl.save(board);

        // then
        // save 메서드가 정확히 한 번 호출 되었는지 검증
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("Mock ID로 게시물 검색 테스트")
    void getById(){
        // given
        long boardId = 1L;
        Board board = new Board("제목", "내용");
        when(boardRepository.findById(boardId)).thenReturn(java.util.Optional.of(board));

        // when
        Board result = boardServiceImpl.getById(boardId);

        // then
        assertThat(result).isEqualTo(board);
    }

    @Test
    @DisplayName("Mock 검색 조선으로 게시물 검색 테스트")
    void testGetBySearch(){
        // given
        BoardSearchDto searchDto = new BoardSearchDto();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Board> page = new PageImpl<>(Collections.singletonList(new Board("제목", "내용")));

        // boardQueryRepository의 search 호출 시 특정 값을 반환하도록 설정
        when(boardQueryRepositoryImpl.getBoardList(searchDto, pageable)).thenReturn(page);

        // when
        Page<Board> result = boardServiceImpl.getBySearch(searchDto, pageable);

        // then
        assertThat(result).isEqualTo(page);
    }
}