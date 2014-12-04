/* 프로필 사진 정보를 위한 테이블 */

CREATE TABLE profile_pic (
	profile_pic_id number(6) PRIMARY KEY,
	employee_id number(6) NOT NULL,
	pic_file_name VARCHAR2(256),
	CONSTRAINT fk_empno_pic FOREIGN KEY (employee_id)
	REFERENCES employees (employee_id)
);

/* 프로필 사진 정보 추가 시 사용될 시퀀스 */
CREATE SEQUENCE "HR"."PROFILE_PICTURE_SEQ"
	MINVALUE 1
	MAXVALUE 999999999
	INCREMENT BY 1
	START WITH 1;