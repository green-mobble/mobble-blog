package org.example.mobble.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "category_tb",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_category",
                columnNames = {"user_id", "category"} // 같은 유저 내 중복 카테고리명 금지
        )
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 만든 사람
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // 카테고리명
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Builder
    public Category(Integer id, Integer userId, String category) {
        this.id = id;
        this.userId = userId;
        this.category = category;
    }

    // 이름 변경 도메인 메서드
    public void rename(String category) {
        this.category = category;
    }

    @PrePersist @PreUpdate
    private void onPersistUpdate() {
        if (category != null) category = category.trim();
    }
}
