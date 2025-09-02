INSERT INTO USER_TB (USERNAME, PASSWORD, EMAIL, CREATED_AT) VALUES ('ssar', '1234', 'ssar@metacoding.com', NOW());
INSERT INTO USER_TB (USERNAME, PASSWORD, EMAIL, CREATED_AT) VALUES ('cos', '1234', 'cos@metacoding.com', NOW());
INSERT INTO USER_TB (USERNAME, PASSWORD, EMAIL, CREATED_AT) VALUES ('love', '1234', 'love@metacoding.com', NOW());

INSERT INTO board_tb (title, content, user_id, views, category_id, created_at, updated_at) VALUES
    ('제목1', '내용1', 1, 12, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO board_tb (title, content, user_id, views, category_id, created_at, updated_at) VALUES
    ('제목2', '내용2', 2, 34, 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO board_tb (title, content, user_id, views, category_id, created_at, updated_at) VALUES
    ('제목3', '내용3', 1, 7, 300, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO board_tb (title, content, user_id, views, category_id, created_at, updated_at) VALUES
    ('제목4', '내용4', 3, 19, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO board_tb (title, content, user_id, views, category_id, created_at, updated_at) VALUES
    ('제목5', '내용5', 2, 3, 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
