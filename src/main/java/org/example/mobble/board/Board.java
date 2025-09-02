package org.example.mobble.board;

import jakarta.persistence.*;
import lombok.*;
import org.example.mobble.category.Category;
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

    private String title;
    private String content;

    // 글 쓴 사람
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 조회수
    private Integer views;

    // 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    
    // 글 작성 시간
    @CreationTimestamp
    private Timestamp createdAt;
    
    // 글 수정 시간
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder
    public Board(Integer id, String title, String content, User user, Integer views, Category category, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.views = views;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateInfo(BoardRequest.BoardUpdateDTO reqDTO,Category category) {
        this.title = reqDTO.getTitle();
        this.content = reqDTO.getContent();
        this.category = category;
    }
}
