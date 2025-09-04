package org.example.mobble.report.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mobble.report.dto.ReportRequest;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "report_tb")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 테이블 설정 후 추가

    // 신고할 게시글
    private Integer boardId;

    // 신고하는 유저
    private Integer userId;

    // 신고 사유
    private ReportCase result;

    // 기타 신고 사유
    private String resultEtc;

    // 신고 내용
    private String content;

    // 신고 상태
    private ReportStatus status;

    //작성 시간
    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Report(Integer id, Integer boardId, Integer userId, ReportCase result, String resultEtc, String content) {
        this.id = id;
        this.boardId = boardId;
        this.userId = userId;
        this.result = result;
        this.resultEtc = resultEtc;
        this.content = content;
    }

    // 신고 서비스에서 처리
//    public Report(User user, Integer boardId, BoardRequest.BoardReportDTO reqDTO) {
//        this.boardId = boardId;
//        this.userId = user.getId();
//        try {
//            this.result = ReportCase.valueOf(reqDTO.getResult());
//        } catch (IllegalArgumentException e) {
//            this.result = ReportCase.ETC;
//            this.resultEtc = reqDTO.getResult();
//        }
//        this.content = reqDTO.getContent();
//
//    }
    public void updateResultEtc(String resultEtc) {
        this.resultEtc = resultEtc;
    }

    public void updateStauts(ReportStatus status) {
        this.status = status;
    }

    public void updateInfo(ReportRequest.ReportUpateDTO reqDTO) {

        this.result =reqDTO.getResult();
        this.resultEtc = reqDTO.getResultEtc();
        this.content = reqDTO.getContent();
    }
}

