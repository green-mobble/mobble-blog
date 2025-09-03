package org.example.mobble.report.dto;

import lombok.Data;
import org.example.mobble.board.domain.Board;
import org.example.mobble.report.domain.Report;
import org.example.mobble.report.domain.ReportCase;
import org.example.mobble.report.domain.ReportStatus;
import org.example.mobble.user.domain.User;

import java.sql.Timestamp;

public class ReportResponse {

    @Data
    public static class ReportDTO {
        private Integer id; //신고 id
        private ReportCase result; //신고 유형
        private ReportStatus status; //신고 처리 상태
        private String reportingUsername; // 신고자
        private String reportedUsername; // 게시글 작성자
        private String boardTitle; //게시물 제목
        private Timestamp createdAt; //생성일자


        public ReportDTO(Board boardPS, User reportUser, Report report,User BoardUser) {
            this.id = report.getId();
            this.result = report.getResult();
            this.status = report.getStatus();
            this.reportingUsername = reportUser.getUsername();
            this.reportedUsername = BoardUser.getUsername();
            this.boardTitle = boardPS.getTitle();
             this.createdAt = report.getCreatedAt();
        }
    }
}
