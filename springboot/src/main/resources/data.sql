-- users 테이블 INSERT (외국어 사용자 추가 포함)
INSERT INTO users (user_id, name, password, role, ship_yard_area, language_code, tts_enabled) VALUES
('1001', '김철수', '{noop}1234', 'ROLE_ADMIN', '제8조선소', 'ko', TRUE),
('1002', '이영희', '{noop}1234', 'ROLE_ADMIN', '제8조선소', 'en', FALSE),
('1003', '박민준', '{noop}1234', 'ROLE_USER', '제5조선소', 'ko', TRUE),
('1004', '최유나', '{noop}1234', 'ROLE_USER', '제9조선소', 'ja', TRUE),
('1005', 'Kenji Tanaka', '{noop}5678', 'ROLE_USER', '제9조선소', 'ja', TRUE),
('1006', 'Maria Garcia', '{noop}5678', 'ROLE_USER', '제5조선소', 'es', FALSE),
('1007', 'Alex Schmidt', '{noop}5678', 'ROLE_ADMIN', '제8조선소', 'de', TRUE),
('1008', 'Li Wei', '{noop}5678', 'ROLE_USER', '제5조선소', 'zh', FALSE),
('user2', '테스트2', '{noop}password2', 'ROLE_USER', '제8조선소', 'ko', TRUE); -- 💡 채팅 테스트 상대방 계정 추가

-- EQUIPMENT 테이블 INSERT (변동 없음)
INSERT INTO EQUIPMENT (equipment_id, name, qr_code) VALUES
(1, '테스트 펌프', 'QR-P-001'),
(2, '고성능 용접기', 'QR-W-002'),
(3, '대형 크레인', 'QR-C-003'),
(4, '자동 페인트 분사기', 'QR-P-004'),
(5, '배관 커팅 장비', 'QR-T-005'),
(6, '이동식 발전기', 'QR-G-006'),
(7, '산소/아세틸렌 절단기', 'QR-G-007'),
(8, '지게차 (5톤)', 'QR-V-008'),
(9, '고압 세척기', 'QR-C-009'),
(10, '이동식 비계', 'QR-S-010'),
(11, '유압 프레스', 'QR-P-011'),
(12, '측정 게이지 세트', 'QR-M-012'),
(13, '케이블 풀러', 'QR-E-013'),
(14, '송풍기', 'QR-V-014'),
(15, '레이저 레벨기', 'QR-M-015'),
(16, '드릴링 머신', 'QR-T-016');

-- work_logs 테이블 INSERT (기존 데이터)
INSERT INTO work_logs (user_id, title, log_text, equipment_id, created_at, updated_at) VALUES
('1001', '제8구역 펌프 교체 작업 일지', 'A-3 라인 펌프를 신형으로 교체 완료. 특이사항 없음.', 1, NOW(), NOW()),
('1002', '오전 안전 점검 결과 보고', '제8구역 전체 안전 장비 이상 없음 확인. 추가 보강 필요 지점: X-104 구역.', NULL, NOW(), NOW()),
('1003', '5구역 용접봉 재고 파악', '용접봉 재고 부족. 100개 긴급 주문 요청.', NULL, NOW(), NOW()),
('1004', '9구역 도장 작업 최종 완료', '야간 도장 작업 완료 후 건조 대기 중. 품질 양호.', NULL, NOW(), NOW());

-- work_logs 테이블 INSERT (기존 데이터 - 2차)
INSERT INTO work_logs (user_id, title, log_text, equipment_id, created_at, updated_at) VALUES
('1003', '제5구역 배관 교체 작업', '새로 도입된 배관 커팅 장비(ID:5)를 사용한 작업 효율 20% 상승 확인.', 5, NOW(), NOW()),
('1004', '9구역 크레인 정기 점검', '대형 크레인(ID:3) 와이어 장력 점검 완료. 다음 점검 일자 3주 후로 지정.', 3, NOW(), NOW()),
('1001', '오후 용접 교육 일지', '신입 사원 대상 고성능 용접기(ID:2) 사용법 교육 진행. 숙련도 향상 필요.', 2, NOW(), NOW()),
('1002', '발전기 예비 가동 테스트', '이동식 발전기(ID:6) 30분 예비 가동 테스트. 출력 안정성 양호.', 6, NOW(), NOW()),
('1003', '5구역 페인트 분사 작업 준비', '자동 페인트 분사기(ID:4) 필터 교체 완료. 내일 오전 도장 작업 준비 완료.', 4, NOW(), NOW());

-- work_logs 테이블 INSERT (기존 데이터 - 3차)
INSERT INTO work_logs (user_id, title, log_text, equipment_id, created_at, updated_at) VALUES
('1004', '긴급 절단 작업 보고', '산소/아세틸렌 절단기(ID:7)를 이용해 손상된 철판 제거. 소화기 비치 완료.', 7, NOW(), NOW()),
('1001', '자재 운반 작업 일지', '지게차(ID:8)를 사용하여 블록 A-2 구역으로 운반 완료. 안전 수칙 준수.', 8, NOW(), NOW()),
('1002', '9구역 선체 청소 기록', '선체 표면 고압 세척기(ID:9) 사용 완료. 오염 물질 처리 기록 남김.', 9, NOW(), NOW()),
('1003', '고소 작업대 설치', '이동식 비계(ID:10) 설치 및 안전성 확인. 30m 높이 작업 예정.', 10, NOW(), NOW()),
('1004', '강판 성형 작업 결과', '유압 프레스(ID:11)를 이용한 강판 벤딩 작업 완료. 치수 오차 $ \pm 0.5mm $ 이내.', 11, NOW(), NOW()),
('1001', '정밀 측정 보고서', '측정 게이지 세트(ID:12) 교정 및 사용 완료. 특정 부위 두께 이상 없음.', 12, NOW(), NOW()),
('1002', '전기 케이블 인입 작업', '케이블 풀러(ID:13)를 사용하여 굵은 케이블 인입 성공. 작업 시간 단축.', 13, NOW(), NOW()),
('1003', '밀폐 구역 환기 작업', '송풍기(ID:14)를 가동하여 도장 작업 후 밀폐 구역 환기 실시. 가스 농도 정상.', 14, NOW(), NOW()),
('1004', '기준선 설정 및 검측', '레이저 레벨기(ID:15)를 이용해 용골 기준선 설정. 수평 오차 0.01% 이내.', 15, NOW(), NOW()),
('1001', '부품 가공 작업 일지', '드릴링 머신(ID:16)으로 특수 부품 50개 홀 가공 완료. 냉각유 보충.', 16, NOW(), NOW());

-- work_logs 테이블 INSERT (외국어 더미 데이터 추가)
INSERT INTO work_logs (user_id, title, log_text, equipment_id, created_at, updated_at) VALUES
('1005', '9エリア溶接点検レポート', '溶接機(ID:2)の定期点検を実施。トーチ交換済み。', 2, NOW(), NOW()), -- Japanese
('1006', 'Reporte de Inspección de Seguridad', 'Revisión completa de equipos de elevación. Grúa (ID:3) sin anomalías.', 3, NOW(), NOW()), -- Spanish
('1007', 'Pumpenwartung Protokoll', 'Routine-Wartung an der Testpumpe (ID:1) abgeschlossen. Filter gereinigt.', 1, NOW(), NOW()), -- German
('1008', '5区油漆喷涂准备', '自动喷枪(ID:4)の压力测试完成。油漆A-30库存充足。', 4, NOW(), NOW()), -- Chinese
('1005', 'Cutting Equipment Calibration', 'Checked pipe cutting machine (ID:5) laser alignment. Ready for production.', 5, NOW(), NOW()), -- English (Using Japanese user_id)
('1006', 'Prueba del Generador Móvil', 'El generador (ID:6) funcionó por 1 hora. Nivel de combustible revisado.', 6, NOW(), NOW()), -- Spanish
('1007', 'Schweißen und Schneiden Bericht', 'Sauerstoff-Acetylen-Schneidbrenner (ID:7) für Notfallarbeiten bereit.', 7, NOW(), NOW()), -- German
('1008', '叉车操作记录', '5吨叉车(ID:8)运送钢板到B-1区。记录操作时间15分钟。', 8, NOW(), NOW()); -- Chinese