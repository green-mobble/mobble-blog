package org.example.mobble.report.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.mobble._util.error.ex.Exception401;
import org.example.mobble.board.dto.BoardRequest;
import org.example.mobble.report.domain.ReportStatus;
import org.example.mobble.report.dto.ReportRequest;
import org.example.mobble.report.dto.ReportResponse;
import org.example.mobble.report.service.ReportService;
import org.example.mobble.user.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ReportController {
    private final ReportService reportService;
    private final HttpSession session;

    //관리자 전체 신고 리스트
    @GetMapping("/admin/reports")
    public String list(Model model) {
        List<ReportResponse.ReportDTO> resDTO = reportService.findAll();
        model.addAttribute("resDTO", resDTO);
        return "admin/main";
    }

    //관리자 상태 변경
    @PutMapping("/admin/reports/{id}")
    public String statusUpdate(@PathVariable(name = "id") Integer reportId, ReportRequest.ReportUpateDTO reqDTO) {
        ReportStatus status = reportService.statusUpdate(reportId,reqDTO);
        return "admin/main";
    }

    // 어드민 신고 보기

    //내 신고 보기

    //내 신고 삭제
    //관리자 상태 변경
    @PutMapping("/reports/{id}")
    public String delete(@PathVariable(name = "id") Integer reportId) {
        reportService.delete(reportId);
        return "admin/main";
    }

    //내 신고 수정


}
