package org.example.mobble.category;

import jakarta.persistence.*;
import lombok.*;
import org.example.mobble.board.Board;
import org.example.mobble.user.User;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category_tb")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 만든 사람
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 카테고리명
    private String category;



    @Builder
    public Category(Integer id, User user, String category) {
        this.id = id;
        this.user = user;
        this.category = category;

    }
}
