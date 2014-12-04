/* 권한 설정 테이블 */
CREATE TABLE grant_auth (
  employee_id number(6) NOT NULL UNIQUE,
  authority_id number(2, 0) NOT NULL,
  CONSTRAINT fk_empno FOREIGN KEY (employee_id)
  REFERENCES employees (employee_id),
  CONSTRAINT fk_authno FOREIGN KEY (authority_id)
  REFERENCES authority (authority_id)
);

/* 몇 가지 주요 사원들 권한 설정 */

/* 이사진들 */
INSERT INTO grant_auth VALUES (100, 1);		
INSERT INTO grant_auth VALUES (101, 1);
INSERT INTO grant_auth VALUES (102, 1);

/* 인사부 직원 */
INSERT INTO grant_auth VALUES (203, 2);

/* 판매 관련 부서의 직원들에게 PURCHASE 권한 주기 */
INSERT INTO grant_auth 
  SELECT employee_id, 3 FROM employees WHERE department_id IN (30, 80, 50);
