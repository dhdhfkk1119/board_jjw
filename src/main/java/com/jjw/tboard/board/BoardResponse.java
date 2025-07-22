package com.jjw.tboard.board;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

public class BoardResponse {
    
    @Data
    @Builder
    public static class BoardDTO {
        private Long boardIdx;
        private String boardTitle;
        private String boardContent;
        private String createAt;
        private String writerName;

        public static BoardDTO from(Board board) {
            return BoardDTO.builder()
                    .boardIdx(board.getBoardIdx())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .writerName(board.getMember().getMemberName())
                    .createAt(board.getTime())
                    .build();
        }
    }

}
