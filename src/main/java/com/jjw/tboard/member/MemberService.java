package com.jjw.tboard.member;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jjw.tboard._core.errors.exception.Exception404;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProfileUploadService profileUploadService;

    @Transactional
    public void save(MemberRequest.SaveDTO saveDTO) {
        String encodedPassword = passwordEncoder.encode(saveDTO.getPassword());


        try {
            String newImagePath = profileUploadService.uploadProfileImage(saveDTO.getProfileImage());
            Member member = saveDTO.toEntity(newImagePath);
            member.setPassword(encodedPassword);
            memberRepository.save(member);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // 아이디 중복 검사
    public boolean isCheckId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    public Member login(MemberRequest.LoginDTO loginDTO) {
        Member member = memberRepository.findByMemberId(loginDTO.getMemberId())
                .orElseThrow(() -> new Exception404("존재하지 않는 아이디입니다") );

        if(!passwordEncoder.matches(loginDTO.getPassword(),member.getPassword())){
            throw new Exception404("비밀번호 또는 아이디가 틀렸습니다");
        }
        return member;
    }

    // 회원 번호로 찾기
    public Member findById(Long memberidx){
        return memberRepository.findById(memberidx).orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다"));
    }
}
