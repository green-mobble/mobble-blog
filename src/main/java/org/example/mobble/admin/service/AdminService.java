package org.example.mobble.admin.service;

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
import org.example.mobble.user.domain.User;
import org.example.mobble.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.example.mobble._util.error.ErrorEnum.*;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    //전체 신고 리스트 보기
    public List<AdminResponse.ReportDTO> getList() {
        List<Report> reportList = reportRepository.findAll();
        List<AdminResponse.ReportDTO> resDTO = new ArrayList<>();
        for (Report report : reportList) {
          Board boardPS =   getBoard(report.getBoardId());
          User boardUser = getUser(report.getBoardId());
          User reportUser = getUser(report.getUserId());
            resDTO.add(new AdminResponse.ReportDTO(boardPS,reportUser,report,boardUser));
        }
        return resDTO;
    }
    public Board getBoard(Integer boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new Exception400(BAD_REQUEST)
        );
    }
    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new Exception404(NOT_FOUND)
        );
    }

    //상태 변경
    public ReportStatus updateStatus(Integer reportId, AdminRequest.ReportUpateDTO reqDTO) {
        Report reportPS = reportRepository.findById(reportId)
                .orElseThrow(() -> new Exception404(NOT_FOUND));

        reportPS.updateStauts(reqDTO.getStatus());
        return reportPS.getStatus();
    }

    public AdminResponse.ReportDetailDTO getReport(Integer reportId) {
        return null;
    }
}
