
/* 비밀번호 설정 SQL문 */
/* 암호화를 위해서는 암호화 패키지를 사용해야 하기 때문에 */
/* 일단 암호화 없이 설정함. 차후에 다시 암호화하기를 권장 */

/* 일반사원들 */
UPDATE employees SET password = 'sktkdnjs@!';

/* 인사부서 */
UPDATE employees SET password = 'dlstkqn@!' WHERE department_id = 40;

/* 판매 관련 부서들: 마케팅, 판매, 운송, 회계 */
UPDATE employees SET password = 'vksao@!' WHERE department_id IN (20, 30, 50, 110);

/* 스티븐(사장) */
UPDATE employees SET password = 'sktkwkd@!' WHERE employee_id = 100;