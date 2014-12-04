/* customers 테이블에 user_name과 password 추가 */
ALTER TABLE customers ADD cust_user_name varchar(20) UNIQUE;
ALTER TABLE customers ADD password varchar(50);

/* 기존 고객들의 비밀번호는 하나로 통일 */
UPDATE customers SET password = 'skrhror@!';

/* 기존 고객들의 user_name 생성(번호와 이름을 이용하여 유일성 유지) */
UPDATE customers SET cust_user_name = 
(cust_last_name || '_' || customer_id)
WHERE customer_id IN (SELECT customer_id FROM customers);
