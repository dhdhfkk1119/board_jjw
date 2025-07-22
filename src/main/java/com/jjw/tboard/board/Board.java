package com.jjw.tboard.board;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.jjw.tboard._core.utils.MyDateUtil;
import com.jjw.tboard.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board_tb")
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    private String boardTitle;
    private String boardContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;
    
    @CreationTimestamp
    private Timestamp createdAt;

    @Transient
    private boolean isBoardOwner;

    // 게시글에 소유자를 직접 확인하는 기능을 만들자
    public boolean isOwner(Long checkUserId) {
        return this.member.getMemberIdx().equals(checkUserId);
    }

    public String getTime() {
        return MyDateUtil.timestampFormat(createdAt);
    }

    // 게시글 수정 더티 체킹
    public void updateFromDto(BoardRequest.UpdateDTO updateDTO ){
        this.boardTitle = updateDTO.getBoardTitle();
        this.boardContent = updateDTO.getBoardContent();
    }
}
