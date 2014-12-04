/* 권한 테이블 */
CREATE TABLE authority (
  authority_id number(2, 0) NOT NULL,
  authority_title varchar2(20) not null,
  PRIMARY KEY(authority_id)
);

/* 5가지 권한을 추가 */
INSERT INTO authority VALUES (1, 'ALL');
INSERT INTO authority VALUES (2, 'HR');
INSERT INTO authority VALUES (3, 'PURCHASE');
INSERT INTO authority VALUES (4, 'GENERAL');
INSERT INTO authority VALUES (5, 'GUEST');

