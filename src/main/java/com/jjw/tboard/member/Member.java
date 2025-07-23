package com.jjw.tboard.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_tb")
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberIdx;
    private String memberId;
    private String memberName;
    private String password;
    private String memberEmail;
    private String sex;
    private String addressNumber; // 지번
    private String addressDefault; // 기본주소
    private String addressDetail; // 상세 주소
    private LocalDate brithDay;
    private String profileImage;
    private String phoneNumber;

    @CreationTimestamp
    private Timestamp createdAt;
}
