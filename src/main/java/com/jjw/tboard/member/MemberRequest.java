package com.jjw.tboard.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

public class MemberRequest {

    @Data
    public static class SaveDTO{

        @NotBlank(message = "아이디를 입력해 주시기 바랍니다")
        private String memberId;

        @NotBlank(message = "이름을 입력해 주시기 바랍니다")
        private String memberName;

        @NotBlank(message = "비밀번호를 입력 해주시기 바랍니다")
        private String password;

        @NotBlank(message = "비밀번호 확인를 입력 해주시기 바랍니다")
        private String rePassword;

        @NotBlank(message = "이메일을 입력해주시기 바랍니다")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|co\\.kr|org|ac\\.kr)$",
                message = "이메일 형식이 올바르지 않거나 .com 또는 .co.kr 도메인만 허용됩니다"
        )
        private String memberEmail;

        @NotBlank(message = "지번을 입력해 주시기 바랍니다")
        private String addressNumber; // 지번

        @NotBlank(message = "기본 주소를 입력해 주시기 바랍니다")
        private String addressDefault; // 주소

        @NotBlank(message = "상세 주소를 입력해 주시기 바랍니다")
        private String addressDetail; // 상세 주소

        @NotNull(message = "생년월일 선택해 주시기 바랍니다")
        private LocalDate brithDay;

        @NotBlank(message = "시작하는 번호를 입력해주세요")
        @Pattern(regexp = "010", message = "전화번호는 010만 입력 가능합니다.")
        private String phone1;

        @NotBlank(message = "전화번호 앞 4자리를 입력해주세요")
        private String phone2;

        @NotBlank(message = "전화번호 뒷 4자리를 입력해주세요")
        private String phone3;

        public Member toEntity(){
            String phoneNumber = phone1 + phone2 + phone3;

            return Member.builder()
                    .memberId(this.memberId)
                    .memberName(this.memberName)
                    .password(this.password)
                    .memberEmail(this.memberEmail)
                    .addressNumber(this.addressNumber)
                    .addressDefault(this.addressDefault)
                    .addressDetail(this.addressDetail)
                    .profileImage("basic.png")
                    .phoneNumber(phoneNumber)
                    .brithDay(this.brithDay)
                    .build();
                    
        }
    }

    // 이미지 등록
    @Data
    public static class ProfileImageDTO {
        // file 정보가 다 담겨 있게 된다.
        private MultipartFile profileImage;

        public void validate() {
            if(profileImage == null || profileImage.isEmpty()) {
                throw new RuntimeException("프로필 이미지를 선택해주세요");
            }
            // 파일 크기 검증( 20MB 제한)
            if(profileImage.getSize() > 20 * 1024 * 1024) {
                throw new RuntimeException("파일 크기는 20MB 이하여야 합니다");
            }
            // 파일 타입 검증 (보안)
            String contentType = profileImage.getContentType();
            if(contentType == null || contentType.startsWith("image/") == false) {
                throw new RuntimeException("이미지 파일만 업로드 가능합니다");
            }
        }
    }

    @Data
    public static class LoginDTO{
        
        @NotBlank(message = "아이디를 입력해주시기 바랍니다")
        private String memberId;
        @NotBlank(message = "비밀번호를 입력해주시기 바랍니다")
        private String password;

    }
}
