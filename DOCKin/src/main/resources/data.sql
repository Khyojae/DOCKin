INSERT INTO users (user_id, name, password, role, language_code, tts_enabled) VALUES
(1001, '김철수', 'a3f789d0e1c2b4a5f6e7d8c9b0a1f2e3d4c5b6a7f8e9d0c1b2a3f4e5d6c7b8a9', 'admin', 'ko', TRUE),
(1002, '이영희', 'b4a5f6e7d8c9b0a1f2e3d4c5b6a7f8e9d0c1b2a3f4e5d6c7b8a9a3f789d0e1c2', 'worker', 'en', FALSE),
(1003, '박민준', 'c5b6a7f8e9d0c1b2a3f4e5d6c7b8a9a3f789d0e1c2b4a5f6e7d8c9b0a1f2e3d4', 'worker', 'ko', TRUE),
(1004, '최유나', 'd6c7b8a9a3f789d0e1c2b4a5f6e7d8c9b0a1f2e3d4c5b6a7f8e9d0c1b2a3f4e5', 'admin', 'ko', TRUE),
(1005, '정우진', 'e7d8c9b0a1f2e3d4c5b6a7f8e9d0c1b2a3f4e5d6c7b8a9a3f789d0e1c2b4a5f6', 'worker', 'ja', FALSE);
ALTER TABLE users AUTO_INCREMENT = 1006;
