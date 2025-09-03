package org.example.mobble.report.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.mobble.report.dto.ReportResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReportRepository {
    private final EntityManager em;

    public void save(Report report) {
        em.persist(report);
    }


    public List<Report> findAll() {
        return em.createQuery("select r FROM Report r", Report.class)
                .getResultList();
    }

    public Report findById(Integer reportId) {
        return em.find(Report.class, reportId);
    }

    public void delete(Integer reportId) {
        Report report = em.find(Report.class, reportId);
        if (report != null) {
            em.remove(report);
        }
    }
}
