-- users 테이블 INSERT (users 테이블이 비어 있다면 실행)
INSERT INTO users (user_id, name, password, role, ship_yard_area, language_code, tts_enabled) VALUES
('1001', '김철수', '{noop}1234', 'ROLE_ADMIN', '제8조선소', 'ko', TRUE),
('1002', '이영희', '{noop}1234', 'ROLE_ADMIN', '제8조선소', 'en', FALSE),
('1003', '박민준', '{noop}1234', 'ROLE_USER', '제5조선소', 'ko', TRUE),
('1004', '최유나', '{noop}1234', 'ROLE_USER', '제9조선소', 'ja', TRUE);

-- work_logs 테이블 INSERT (데이터가 누락되었다면 실행)
INSERT INTO work_logs (user_id, title, log_text, equipment_id, created_at, updated_at) VALUES
('1001', '제8구역 펌프 교체 작업 일지', 'A-3 라인 펌프를 신형으로 교체 완료. 특이사항 없음.', NULL, NOW(), NOW()),
('1002', '오전 안전 점검 결과 보고', '제8구역 전체 안전 장비 이상 없음 확인. 추가 보강 필요 지점: X-104 구역.', NULL, NOW(), NOW()),
('1003', '5구역 용접봉 재고 파악', '용접봉 재고 부족. 100개 긴급 주문 요청.', NULL, NOW(), NOW()),
('1004', '9구역 도장 작업 최종 완료', '야간 도장 작업 완료 후 건조 대기 중. 품질 양호.', NULL, NOW(), NOW());