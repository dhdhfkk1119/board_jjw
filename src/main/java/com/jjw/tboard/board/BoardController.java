package com.jjw.tboard.board;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jjw.tboard._core.session.SessionUser;
import com.jjw.tboard.member.Member;
import com.jjw.tboard.member.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    // 게시글 리스트
    @GetMapping("/list")
    public String boardList(Model model){
        List<Board> boardList = boardService.boardList();
        model.addAttribute("boardList", boardList);
        return "board/board-list";
    }

    @GetMapping("/save-form")
    public String boardForm(){
        return "board/board-form";
    }

    // 게시글 수정 업데이트 폼
    @GetMapping("/update-form/{boardIdx}")
    public String updateForm(@PathVariable(name = "boardIdx") Long boardIdx,HttpSession httpSession,Model model){
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("session");
        Board board = boardService.findById(boardIdx);
        boardService.checkBoardOwner(boardIdx,sessionUser.getId());
        model.addAttribute("board", board);
        return "board/board-update";
    }

    // 게시글 수정 업데이트 폼
    @PostMapping("/update/{boardIdx}")
    public String update(@PathVariable(name = "boardIdx") Long boardIdx,HttpSession httpSession,BoardRequest.UpdateDTO updateDTO){
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("session");
        boardService.updateBoard(boardIdx, updateDTO, sessionUser.getId());
        return "redirect:/";
    }

    // 게시글 상세 정보
    @GetMapping("/{boardIdx}")
    public String boardDetail(@PathVariable(name = "boardIdx") Long boardIdx,Model model,HttpSession httpSession){

        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("session");

        Board board = boardService.getBoard(boardIdx);
        BoardResponse.BoardDTO boardDTO = BoardResponse.BoardDTO.from(board);
        boolean isBoardOwner = boardService.isBoardOwner(boardIdx,sessionUser.getId());
        model.addAttribute("board", boardDTO);
        model.addAttribute("isOwner", isBoardOwner);
        return "board/board-detail";
    }

    // 게시글 저장
    @PostMapping("/save")
    public String save(@Valid BoardRequest.SaveDTO saveDTO,BindingResult bindingResult,HttpSession httpSession){
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("session");
        Member member = memberService.findById(sessionUser.getId());
        boardService.save(saveDTO,member);

        return "redirect:/";
    }

    // 게시글 삭제
    @PostMapping("/delete/{boardIdx}")
    public String delete(@PathVariable(name = "boardIdx") Long boardIdx,HttpSession httpSession){
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("session");
        Member member = memberService.findById(sessionUser.getId());
        boardService.delete(boardIdx,member);
        return "redirect:/";
    }
}
