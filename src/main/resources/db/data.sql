INSERT INTO user_tb (username, password, email) VALUES ('ssar', '1234', 'ssar@nate.com');
INSERT INTO user_tb (username, password, email) VALUES ('cos', '1234', 'cos@nate.com');
INSERT INTO user_tb (username, password, email) VALUES ('love', '1234', 'love@nate.com');

INSERT INTO category_tb (user_id, category)
VALUES
    (1, '공지사항'),
    (1, '자유게시판'),
    (2, 'Q&A');

INSERT INTO board_tb
(title, content, user_id, views, category_id, created_at, updated_at)
VALUES
    ('첫 번째 글', 'ssar이 작성한 공지사항입니다.', 1, 15, 1, NOW(), NOW()),
    ('두 번째 글', 'ssar이 작성한 자유게시판 글입니다.', 1, 5, 2, NOW(), NOW()),
    ('세 번째 글', 'ssar이 작성한 Q&A 글입니다.', 1, 23, 1, NOW(), NOW()),
    ('네 번째 글', 'love가 작성한 자유게시판 글입니다.', 3, 8, 2, NOW(), NOW());