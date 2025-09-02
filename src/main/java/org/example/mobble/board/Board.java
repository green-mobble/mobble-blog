package org.example.mobble.board;

import jakarta.persistence.*;
import lombok.*;
import org.example.mobble.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 테이블 설정 후 추가

    @Column(nullable = false)
    private String title;

    // HTML 본문: 대용량 텍스트 + 지연 로딩
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    // 조회수: 기본값 0, NOT NULL
    @Column(nullable = false)
    private Integer views = 0;

    // 카테고리
    private Integer categoryId;
    
    // 글 작성 시간
    @CreationTimestamp
    private Timestamp createdAt;
    
    // 글 수정 시간
    @UpdateTimestamp
    private Timestamp updatedAt;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    void prePersist() {
        if (views == null) views = 0;
    }

    @Builder
    public Board(Integer id, String title, String content, User user, Integer views, Integer categoryId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.views = (views == null) ? 0 : views;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 더티 체킹용 update 함수 예시
    void update (String title, String content) {
        this.title = title;
        this.content = content;
    }
}
