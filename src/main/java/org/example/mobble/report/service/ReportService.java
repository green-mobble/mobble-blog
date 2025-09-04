package org.example.mobble.report.service;

import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception400;
import org.example.mobble._util.error.ex.Exception403;
import org.example.mobble._util.error.ex.Exception404;
import org.example.mobble.admin.dto.AdminRequest;
import org.example.mobble.admin.dto.AdminResponse;
import org.example.mobble.board.domain.Board;
import org.example.mobble.board.domain.BoardRepository;
import org.example.mobble.report.domain.Report;
import org.example.mobble.report.domain.ReportRepository;
import org.example.mobble.report.domain.ReportStatus;
import org.example.mobble.report.dto.ReportRequest;
import org.example.mobble.report.dto.ReportResponse;
import org.example.mobble.user.domain.User;
import org.example.mobble.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.example.mobble._util.error.ErrorEnum.*;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;




    //내 신고 글 삭제
    public void delete(Integer reportId,User user) {
        //유저 정보 조회
        User userPS = getUser(user.getId());
        //신고 권한이 있는지 확인
        Report reportPS = checkPermissions(reportId,userPS);
        //삭제
        reportRepository.delete(reportPS);
    }

    public ReportResponse.ReportUpateDTO update(Integer reportId, User user, ReportRequest.ReportUpateDTO reqDTO) {
        //유저 정보 조회
        User userPS = getUser(user.getId());
        //신고 권한이 있는지 확인
        Report reportPS = checkPermissions(reportId,userPS);
        //업데이트
        reportPS.updateInfo(reqDTO);
        return new ReportResponse.ReportUpateDTO(reportPS);

    }
    //권한 검증
    private Report checkPermissions(Integer reportId, User userPS) {
        Report reportPS = reportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404(NOT_FOUND));
        if (!userPS.getId().equals(reportPS.getUserId())) {
            throw new Exception403(FORBIDDEN);
        }
        return reportPS;
    }
    //유저 조회
    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new Exception404(NOT_FOUND)
        );
    }

    public List<ReportResponse.ReportDTO> getList(User user) {
        return null;
    }

    public ReportResponse.ReportDetailDTO getReport(Integer reportId) {
        return null;
    }
}
