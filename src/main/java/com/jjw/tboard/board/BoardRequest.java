package com.jjw.tboard.board;

import com.jjw.tboard.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class BoardRequest {

    @Data
    public static class SaveDTO{

        @NotBlank(message = "게시판 제목을 입력해주시기 바랍니다")
        private String boardTitle;
        
        @NotBlank(message = "게시판 내용을 입력해주시기 바랍니다")
        private String boardContent;

        public Board toEntity(Member member){
            return Board.builder()
                    .boardTitle(this.boardTitle)
                    .boardContent(this.boardContent)
                    .member(member)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO{
        private String boardTitle;
        private String boardContent;
    }
}
