package com.jjw.tboard._core.session;

import com.jjw.tboard.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionUser {
    private Long id;
    private String memberId;
    private String memberName;
    private String role;
    private String profileImage;

    public static SessionUser fromMember(Member member) {
        return new SessionUser(member.getMemberIdx() ,member.getMemberId(), member.getMemberName(), "MEMBER",member.getProfileImage());
    }

}
