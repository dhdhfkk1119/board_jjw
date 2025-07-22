package com.jjw.tboard.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jjw.tboard._core.session.SessionUser;
import com.jjw.tboard.member.MemberRequest.SaveDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 회원 가입 페이지
    @GetMapping("/member/save-form")
    public String saveForm() {
        return "member/member";
    }

    // 회원 가입 페이지
    // 회원가입 처리 POST
    @PostMapping("/member/save")
    public String save(@Valid @ModelAttribute MemberRequest.SaveDTO saveDTO,
            BindingResult bindingResult, Model model) {
        System.out.println("Controller 넘어오는 값 : " + saveDTO.toString());

        // 일반 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            model.addAttribute("errors", errorMap);
            model.addAttribute("saveDTO", saveDTO); // 입력값 유지용
            return "member/member"; // 머스테치 파일명
        }

        // 아이디 중복 검사
        if (memberService.isCheckId(saveDTO.getMemberId())) {
            bindingResult.rejectValue("memberId", "duplicate", "이미 사용 중인 아이디입니다.");
            model.addAttribute("saveDTO", saveDTO);
            return "member/member";
        }

        // 비밀번호 유호성 검사(일치하는 지)
        if (!saveDTO.getPassword().equals(saveDTO.getRePassword())) {
            model.addAttribute("saveDTO", saveDTO);
            Map<String, String> errors = new HashMap<>();
            errors.put("rePassword", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("errors", errors);
            return "member/member";
        }

        // 정상 저장
        memberService.save(saveDTO);
        return "redirect:/";
    }

    @PostMapping("/member/login")
    public String login(@Valid MemberRequest.LoginDTO loginDTO, BindingResult bindingResult,
                        HttpSession httpSession, Model model) {


        // 아이디 및 비밀번호 입력하지 않으면 오류 발생
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>(); 
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errorMap);
            return "index";
        }
        
        // 아이디 비밀번호 있는지 찾기
        Member member = memberService.login(loginDTO);
        if(member == null) {
            model.addAttribute("isErrors","비밀번호 또는 아이디가 틀렸습니다"); // 오류 메세지를 담음
            return "index";
        }
        // 세션 저장
        SessionUser sessionUser = SessionUser.fromMember(member);
        System.out.println("세션 저장됨 1: " + sessionUser.getMemberName());
        httpSession.setAttribute("session", sessionUser);
        System.out.println("세션 저장됨 2: " + sessionUser.getMemberName());

        return "redirect:/";
    }


    @PostMapping("/member/logout")
    public String logout(HttpSession httpSession){

        httpSession.invalidate();
        return "redirect:/";
    }
}
