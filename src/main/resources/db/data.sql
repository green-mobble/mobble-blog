INSERT INTO user_tb (username, password, email) VALUES ('ssar', '1234', 'ssar@nate.com');
INSERT INTO user_tb (username, password, email) VALUES ('cos', '1234', 'cos@nate.com');
INSERT INTO user_tb (username, password, email) VALUES ('love', '1234', 'love@nate.com');

INSERT INTO category_tb (user_id, category)
VALUES
    (1, '공지사항'),
    (1, '자유게시판'),
    (2, 'Q&A'),
    (3, '질문게시판');

INSERT INTO board_tb
(title, content, user_id, views, category_id, created_at, updated_at)
VALUES
    ('첫 번째 글', 'ssar이 작성한 공지사항입니다.', 1, 15, 1, NOW(), NOW()),
    ('두 번째 글', 'cos이 작성한 자유게시판 글입니다.', 2, 5, 3, NOW(), NOW()),
    ('세 번째 글', 'ssar이 작성한 Q&A 글입니다.', 1, 23, 2, NOW(), NOW()),
    ('네 번째 글', 'love가 작성한 질문게시판 글입니다.', 3, 8, 4, NOW(), NOW());

INSERT INTO report_tb
(board_id, user_id, result, result_etc, content, status, created_at)
VALUES
    -- 1. ssar(1)이 cos(2)의 자유게시판 글(2번 게시글)을 "부적절한 글 내용"으로 신고
    (2, 1, 'INAPPROPRIATE_BOARD_CONTENT', NULL, '비속어가 포함된 것 같습니다.', 'PENDING', NOW()),

    -- 2. cos(2)가 love(3)의 자유게시판 글(4번 게시글)을 "광고성 글"로 신고
    (4, 2, 'ADVERTISING_BOARD_CONTENT', NULL, '홍보성 링크가 많습니다.', 'PROCESSING', NOW()),

    -- 3. love(3)가 ssar(1)의 공지사항 글(1번 게시글)을 "부적절한 작성자명"으로 신고
    (1, 3, 'INAPPROPRIATE_AUTHOR_NAME', NULL, '닉네임이 부적절합니다.', 'COMPLETED', NOW()),

    -- 4. ssar(1)이 자신의 Q&A 글(3번 게시글)을 기타 사유로 신고
    (3, 1, 'ETC', '테스트 신고 사유', '테스트용 신고 데이터입니다.', 'PENDING', NOW());