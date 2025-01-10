package com.my.bob.core.domain.board.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.board.dto.request.BoardSearchDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional  // 테스트 후 롤백 처리
@DisplayName("게시판 테스트")
@ActiveProfiles("test")
@WithAccount("system@system.com")   // 자동으로 해당 계정으로 들어가도록 세팅
class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Test
    @DisplayName("게시물 저장 테스트")
    void testSaveBoard() {
        // given
        Board board = new Board("제목", "내용");

        // when
        boardService.save(board);

        // then
        Board savedBoard = boardRepository.findById(board.getBoardId()).orElse(null);
        assertThat(savedBoard).isNotNull();
        assertThat(board.getBoardId()).isEqualTo(savedBoard.getBoardId());
        assertThat(savedBoard.getBoardTitle()).isEqualTo("제목");
        assertThat(savedBoard.getBoardContent()).isEqualTo("내용");
        assertThat(savedBoard.getRegId()).isEqualTo("system@system.com");
    }

    @Test
    @DisplayName("ID로 게시물 검색 테스트")
    void testGetById() {
        // given
        Board board = new Board("제목", "내용");
        boardService.save(board);

        // when
        Board result = boardService.getById(board.getBoardId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBoardId()).isEqualTo(board.getBoardId());
        assertThat(result.getBoardTitle()).isEqualTo("제목");
        assertThat(result.getBoardContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("검색 조건으로 게시글 검색 테스트")
    void testGetBySearch() {
        // given
        Board board = new Board("검색 제목", "검색 내용");
        PageRequest pageable = PageRequest.of(0, 1);
        boardService.save(board);
        BoardSearchDto searchDto = new BoardSearchDto();
        searchDto.setBoardTitle("검색 제목");
        searchDto.setBoardContent("검색 내용");

        // when
        Page<Board> result = boardService.getBySearch(searchDto, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getBoardTitle()).isEqualTo("검색 제목");
        assertThat(result.getContent().get(0).getBoardContent()).isEqualTo("검색 내용");
    }
}
