
--민정님 노션에 있는거 긁어옴--
-- 1. 사용자
CREATE TABLE users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  password VARCHAR(256) NOT NULL,
  role ENUM('worker', 'admin') NOT NULL,
  language_code VARCHAR(10) DEFAULT 'ko',
  tts_enabled BOOLEAN DEFAULT TRUE,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
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
CREATE TABLE work_logs (
  log_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  equipment_id INT,
  log_text TEXT,
  audio_file_url VARCHAR(255),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
);

-- 4. 장비 메모
CREATE TABLE equipment_memos (
  memo_id INT PRIMARY KEY AUTO_INCREMENT,
  equipment_id INT,
  memo_text TEXT,
  created_by INT,
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
  user_id INT,
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
  target_user_id INT,
  due_at DATETIME,
  sent BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (target_user_id) REFERENCES users(user_id)
);

-- 9. 사용자 언어/음성 설정
CREATE TABLE language_settings (
  user_id INT PRIMARY KEY,
  preferred_language VARCHAR(10) DEFAULT 'ko',
  tts_enabled BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 10. 시스템 설정 (예: 카메라 옵션)
CREATE TABLE system_settings (
  setting_id INT PRIMARY KEY AUTO_INCREMENT,
  setting_key VARCHAR(100) UNIQUE,
  setting_value VARCHAR(255)
);

CREATE TABLE authority(
id INTEGER AUTO_INCREMENT PRIMARY KEY,
authority VARCHAR(256),
member_id INTEGER,
FOREIGN KEY(user_id) REFERENCES users(user_id)
);
