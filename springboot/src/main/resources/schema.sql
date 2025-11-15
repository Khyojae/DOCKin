-- 1. 모든 관련 외래 키 제약 조건을 먼저 제거합니다.
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS work_logs;

-- 2. 메인 테이블들을 삭제합니다.
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS equipment;

-- 3. (필요하다면) 시퀀스도 삭제합니다.
-- DROP SEQUENCE IF EXISTS hibernate_sequence;
-- DROP SEQUENCE IF EXISTS users_seq;
--민정님 노션에 있는거 긁어옴--
-- 1. 사용자
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY, -- String PK (사번)
    name VARCHAR(100),
    password VARCHAR(256) NOT NULL,
    role VARCHAR(50) NOT NULL, -- ENUM 대신 VARCHAR 사용
    language_code VARCHAR(10) DEFAULT 'ko',
    tts_enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    ship_yard_area VARCHAR(100) NOT NULL
);
-- 2. 장비 정보
CREATE TABLE equipment (
  equipment_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  qr_code VARCHAR(100) UNIQUE,
  nfc_tag VARCHAR(100) UNIQUE,
  location_x FLOAT,
  location_y FLOAT,
  location_z FLOAT,
  use_coordinates BOOLEAN DEFAULT FALSE, -- AR 좌표 사용 여부
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. 작업 일지 (음성 → 텍스트 저장)
-- 이제 work_logs 테이블의 외래 키 설정도 String 타입으로 맞춰야 합니다.
CREATE TABLE work_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50), -- FK 타입을 String으로 변경
    title VARCHAR(256),
    equipment_id INT,
    log_text TEXT,
    audio_file_url VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
);

-- 4. 장비 메모
CREATE TABLE equipment_memos (
  memo_id INT PRIMARY KEY AUTO_INCREMENT,
  equipment_id INT,
  memo_text TEXT,
  created_by VARCHAR(50),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id),
  FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- 5. 체크리스트 템플릿
CREATE TABLE checklists (
  checklist_id INT PRIMARY KEY AUTO_INCREMENT,
  equipment_id INT,
  title VARCHAR(100),
  type ENUM('pre', 'post'), -- 작업 전/후
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
);

-- 6. 체크리스트 항목
CREATE TABLE checklist_items (
  item_id INT PRIMARY KEY AUTO_INCREMENT,
  checklist_id INT,
  content VARCHAR(255),
  sequence INT,
  FOREIGN KEY (checklist_id) REFERENCES checklists(checklist_id)
);

-- 7. 체크리스트 결과
CREATE TABLE checklist_results (
  result_id INT PRIMARY KEY AUTO_INCREMENT,
  checklist_id INT,
  user_id VARCHAR(50),
  equipment_id INT,
  checked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (checklist_id) REFERENCES checklists(checklist_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
);

-- 8. 푸시 알림
CREATE TABLE notifications (
  notification_id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255),
  message TEXT,
  target_user_id VARCHAR(50),
  due_at DATETIME,
  sent BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (target_user_id) REFERENCES users(user_id)
);

-- 10. 시스템 설정 (예: 카메라 옵션)
CREATE TABLE system_settings (
  setting_id INT PRIMARY KEY AUTO_INCREMENT,
  setting_key VARCHAR(100) UNIQUE,
  setting_value VARCHAR(255)
);

CREATE TABLE Authority(
id INTEGER AUTO_INCREMENT PRIMARY KEY,
authority VARCHAR(256),
user_id VARCHAR(50),
FOREIGN KEY(user_id) REFERENCES users(user_id)
);

-- 11. 근태 관리 (Attendance) 테이블
CREATE TABLE attendance (
    -- 기본 키 (자동 증가)
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    -- 사용자 연관 관계 (users.user_id 참조)
    user_id VARCHAR(50) NOT NULL,

    -- 근태 시간 기록
    clock_in_time DATETIME NOT NULL,
    clock_out_time DATETIME, -- 퇴근 시간 (nullable)

    -- 근무 일자 및 상태
    work_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL, -- 'NORMAL', 'LATE', 'ABSENT' 등

    -- 위치 정보
    in_location VARCHAR(255),    -- 출근 시 위치/리더기 정보
    out_location VARCHAR(255),   -- 퇴근 시 위치/리더기 정보

    -- 외래 키 설정
    CONSTRAINT fk_attendance_member
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);

-- 12. 채팅방 정보
CREATE TABLE chat_rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_name VARCHAR(100), -- 단체방 이름 (1:1 방은 NULL 가능)
    is_group BOOLEAN DEFAULT FALSE, -- 단체방 여부 (TRUE: 단체, FALSE: 1:1)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 13. 채팅방 참여자
CREATE TABLE chat_members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_read_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 마지막 읽은 시간 (안읽은 메시지 수 계산용)
    FOREIGN KEY (room_id) REFERENCES chat_rooms(room_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 14. 채팅 메시지
CREATE TABLE chat_messages (
    message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id INT NOT NULL,
    sender_id VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_rooms(room_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 15. 근태 요청 (병결/휴가 서류 등록)
CREATE TABLE absence_requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    request_type VARCHAR(20) NOT NULL, -- 'SICK'(병결), 'VACATION'(휴가)
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    document_url VARCHAR(255), -- 서류 파일 (이미지/PDF) 저장 경로 (S3 등)
    status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING'(대기), 'APPROVED'(승인), 'REJECTED'(거절)
    requested_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    processed_by VARCHAR(50), -- 승인/거절 처리한 관리자
    processed_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (processed_by) REFERENCES users(user_id)
);

-- 16. 긴급 연락처
CREATE TABLE emergency_contacts (
    contact_id INT PRIMARY KEY AUTO_INCREMENT,
    team_name VARCHAR(50) NOT NULL, -- 예: 'Security Team', 'Fire Safety'
    contact_number VARCHAR(20) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 17. 안전 교육 과정 정보
CREATE TABLE safety_courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    video_url VARCHAR(255) NOT NULL,
    duration_minutes INT DEFAULT 0,
    is_mandatory BOOLEAN DEFAULT TRUE, -- 필수 이수 여부
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 18. 사용자별 안전 교육 이수 상태
CREATE TABLE safety_enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    course_id INT NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    completion_date DATETIME, -- 이수 완료 시각 (NULL이면 미이수)
    enrolled_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES safety_courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_course (user_id, course_id)
);

-- 19. 월별 근로 동의서 서명 기록
CREATE TABLE labor_agreements (
    agreement_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    agreement_month DATE NOT NULL, -- 해당 월의 1일 (YYYY-MM-01)
    is_signed BOOLEAN DEFAULT FALSE, -- 서명 여부
    signed_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_month (user_id, agreement_month)
);