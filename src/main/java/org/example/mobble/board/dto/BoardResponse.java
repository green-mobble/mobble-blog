package org.example.mobble.board.dto;

import lombok.Builder;
import lombok.Data;
import org.example.mobble.board.domain.Board;
import org.example.mobble.report.domain.ReportCase;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class BoardResponse {

    // 팀원 간 컨벤션 토의
    @Data
    public static class DTO {
        Integer id;
        String title;
        String content;
        String imageUrl;
        Integer views;
        Integer bookmarkCount;
        Integer categoryId;
        Integer userId;
        Timestamp createAt;
        Timestamp updateAt;

        @Builder
        public DTO(Board board, Integer bookmarkCount, String imageUrl) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.imageUrl = imageUrl;
            this.views = board.getViews();
            this.bookmarkCount = bookmarkCount;
            this.categoryId = board.getCategoryId();
            this.userId = board.getUser().getId();
            this.createAt = board.getCreatedAt();
            this.updateAt = board.getUpdatedAt();
        }
    }

    @Data
    public static class ReportSaveFormDTO {


        private List<ReportCase> resultList ;

        private Integer boardId;

        @Builder
        public ReportSaveFormDTO(List<ReportCase> resultList, Integer boardId) {
            this.resultList = resultList;
            this.boardId = boardId;
        }
    }

}
