SET REFERENTIAL_INTEGRITY FALSE;

-- 자식 먼저
TRUNCATE TABLE board_tb;

-- 그 다음 부모들
TRUNCATE TABLE category_tb;
TRUNCATE TABLE user_tb;

SET REFERENTIAL_INTEGRITY TRUE;

-- 샘플 데이터 (컬럼은 실제 스키마에 맞춰 조정)
INSERT INTO user_tb (username, password, email, profile_image)
VALUES ('ssar', '1234', 'ssar@test.com', 'default.png');

INSERT INTO category_tb (category, user_id)
VALUES ('개발', 1), ('공지', 1);

-- 필요하면 board_tb INSERT도 추가
