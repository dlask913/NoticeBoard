# spring 게시판 만들기

### 1. 기능 설명


    - Member : 회원 가입 / 수정 / 탈퇴
    - MemberImg : 프로필 이미지 등록 / 수정 / 삭제
    - Notice : 게시글 등록 / 수정 / 삭제 ( 본인이 쓴 글만 수정/삭제 O )
    - Comment : 댓글 등록 / 수정 / 삭제 ( 본인이 쓴 댓글만 수정/삭제 O )



### 2. 연관관계 매핑
<div align="center">
  <img src="https://user-images.githubusercontent.com/79985588/209905811-b6a16e9b-2bdd-455b-8c8a-7f10a9252278.png" width="600" height="400">
    <br/>
</div>


    - 회원과 게시글 -> 1:N 양방향 ( 회원 탈퇴 시 연관된 엔티티 삭제 ) 
    - 회원과 프로필이미지 -> 1:1 양방향 ( 회원 탈퇴 시 연관된 엔티티 삭제 )
    - 게시글과 댓글 -> 1:N 양방향 ( 게시글 삭제 시 연관된 엔티티 삭제 )
    - 회원과 댓글 -> 1:N 양방향 ( 회원 탈퇴 시 연관된 엔티티 삭제 )



### 3. 실행결과
- 로그인했을 때 초기 화면 ( 본인이 쓴 Notices/Comments 확인 )
<div align="center">
    <img src="https://user-images.githubusercontent.com/79985588/209906227-5332a4fd-d389-49c8-8f54-7bc2b14c02fc.png" 
    width="800" height="400">
</div>
