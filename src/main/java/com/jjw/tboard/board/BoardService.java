package com.jjw.tboard.board;

import com.jjw.tboard._core.errors.exception.Exception403;
import com.jjw.tboard._core.errors.exception.Exception404;
import com.jjw.tboard.board.BoardRequest.UpdateDTO;
import com.jjw.tboard.member.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 작성
    @Transactional
    public Board save(BoardRequest.SaveDTO saveDTO,Member member){
        return boardRepository.save(saveDTO.toEntity(member));
    }

    // 모든 게시글 가져오기
    public List<Board> boardList(){
       return boardRepository.findAll();
    }

    public Board findById(Long boardIdx){
        return boardRepository.findById(boardIdx)
        .orElseThrow(() -> new Exception404("해당 게시판을 찾을 수없습니다"));
    }

    // 게시글 삭제하기 
    @Transactional
    public void delete(Long boardIdx, Member member) {
        Board board = findById(boardIdx);
        if(!board.isOwner(member.getMemberIdx())){
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
        boardRepository.deleteById(boardIdx);
    }

    // 게시글 상세보기 
    public Board getBoard(Long boardIdx){
        return boardRepository.findById(boardIdx).orElseThrow(() -> new Exception404("해당 게시판을 찾을 수 없습니다"));
    }

    public boolean isBoardOwner(Long boardId, Long userId) {
        Board board = findById(boardId);
        return board.isOwner(userId); // Board 내부에서 member.getId() == userId 확인
    }

    @Transactional
    public void updateBoard(Long boardIdx, UpdateDTO dto, Long memberId) {
        Board board = boardRepository.findById(boardIdx)
                        .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (!board.isOwner(memberId)) {
            throw new Exception403("본인 게시글만 수정할 수 있습니다.");
        }

        board.updateFromDto(dto); 
    }

    // 게시글 누가 작성 여부
    public void checkBoardOwner(Long boardId, Long userId) {
        Board board = findById(boardId);
        if(!board.isOwner(userId)) {
            throw new Exception403("본인 게시글만 수정할 수 있습니다.");
        }
    }
}
