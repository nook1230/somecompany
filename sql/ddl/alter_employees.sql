/* employees에 비밀번호와 프로필 코멘트 칼럼 추가 */
ALTER TABLE employees ADD password varchar2(50);
ALTER TABLE employees ADD profile_comment nvarchar2(200);