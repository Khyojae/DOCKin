INSERT INTO member(name,email,password) VALUES(
'윤1서준',
'1SeojunYoon@hanbit.co.kr',
'$2a$12$fH0/xw2h54H4xWZrL8xYbekfnNQhSewIZeYVCTIkC7D4QOeodYlCC'
);

INSERT INTO member(name,email,password) VALUES(
'윤2서준',
'2SeojunYoon@hanbit.co.kr',
'$2a$12$VgJcYUyagF0VzG8maS19COBuqKTGB2auoTnTHpb9mc3A69PGV/MsC'
);

INSERT INTO member(name,email,password) VALUES(
'윤3서준',
'3SeojunYoon@hanbit.co.kr',
'$2a$12$iLHOtUeD3hJJeqo2189uVOc4Nj1BmxarM4gWBtXCMSr5vbMijqvKq'
);

INSERT INTO member(name,email,password) VALUES(
'윤4서준',
'4SeojunYoon@hanbit.co.kr',
'$2a$12$jYeEOaHI7sC7yuRFBuLL.urIYbk8pK5A6XRU1TMgshNxMtl4t7AYe'
);

INSERT INTO authority(authority,member_id) VALUES('ROLE_ADMIN',2);

INSERT INTO article(title, description, created, updated ,member_id)
VALUES('첫번째 게시글 제목','첫번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);

INSERT INTO article(title, description, created, updated ,member_id)
VALUES('두번째 게시글 제목','두번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,2);

INSERT INTO article(title, description, created, updated ,member_id)
VALUES('세번째 게시글 제목','세번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,3);

INSERT INTO article(title, description, created, updated ,member_id)
VALUES('네번째 게시글 제목','네번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,4);