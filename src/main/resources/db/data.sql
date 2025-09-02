INSERT INTO user_tb (username, password, email) VALUES
('ssar', '1234', 'ssar@example.com'),
('cos', '1234', 'cos@example.com');

INSERT INTO category_tb (user_id, category) VALUES
(1, 'IT'),
(1, 'Daily'),
(2, 'Study'),
(2, 'Hobby'),
(1, 'Project');

INSERT INTO board_tb (title, content, user_id, views, bookmark,category_id, created_at, updated_at) VALUES
('첫 번째 글', '안녕하세요. 게시판 첫 글입니다!', 1, 10, 1,1, '2025-01-10 09:30:00', '2025-01-10 09:30:00'),
('두 번째 글', '오늘 날씨가 정말 좋네요 🌞', 2, 25, 2,2, '2025-02-15 14:10:00', '2025-02-15 14:10:00'),
('세 번째 글', 'JPA 공부하다가 작성한 테스트 글입니다.', 2, 7, 1,2, '2025-03-20 19:45:00', '2025-03-20 19:45:00'),
('네 번째 글', '이 프로젝트는 더미 데이터 덕분에 편해졌어요!', 1, 50, 3,4, '2025-04-05 08:20:00', '2025-04-05 08:20:00'),
('다섯 번째 글', '마지막 더미 글. 조회수 좀 올려주세요 👀', 2, 0, 2,5,'2025-06-12 21:00:00', '2025-06-12 21:00:00');