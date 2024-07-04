# DevBabys Team 1st Project
## [DeviMall] (가제)
### *Use Env*
* Java Version : 21
* Gradle 8.5
* Spring Boot 3.2.5
* Port Number : 4001
* Branch : 은별 - eunbyeol

### Dev Env
* application.properties 임포트 추가 설정 파일 의도적으로 배제하였음. 참고.

### Reference
* Front-End (React Project) / 전예경
> Github : https://github.com/yek-j/shopping-front-app  
> HOST : https://shopping-front-app.vercel.app
* Back-End (Spring Boot Project) / 고은별
> Github : https://github.com/DevBabys/1st-shoppingmall-backend  
> HOST : https://project1.babychat.xyz
* DevBabys Team
> GitHub : https://github.com/DevBabys  
> Notion : https://www.notion.so/ShoppingMall-ERD-42cc7fb576274bffbd351d9d241e7c8b
* Project Information
> PPT : https://docs.google.com/presentation/d/1k21m5FauVM9W0OejN_bLNB3oxGLDfavWBdHiqaCZ_NE/edit#slide=id.p  
> API 기능 명세서 : https://docs.google.com/spreadsheets/d/1bSlyo5-Lb6NDKC9yH98G6WUEhLEeJteOSwbB86eL2vc/edit#gid=0  
> 데이터베이스 테이블 설계서 (ERD) : https://docs.google.com/document/d/1tydQcgZFXc3Rx_fnkfzR2DHmZTuw1eFjfNSMJKOgbQI/edit  


# 작업 히스토리
* 2024-05-31 : API 형식의 회원가입 기능 구현
* 2024-06-04 : 로그인/로그아웃 기능 구현
* 2024-06-07 : 회원 정보 보기 및 수정, 이메일 찾기, 비밀번호 변경하기 기능 구현
* 2024-06-10 : 상품 카테고리 목록 조회, 추가, 수정, 삭제 기능 구현
* 2024-06-13 : 로그인 토큰 관련 JWT 필터 예외 처리 추가
* 2024-06-17 : 상품 상세 조회 기능 구현
* 2024-06-19 : 상품 추가 및 수정 기능 구현 (파일 업로드 기능 포함)
* 2024-06-20 : 권한에 따른 액세스를 허용하는 인터셉터 추가 및 상품 카테고리, 상품 API 권한 확인 로직 추가
* 2024-06-24 : 카테고리, 상품 조회 시 프론트 엔드에서 보여줄 페이지의 수를 표현하기 위해 조회 API 응답 구조 변경, 조회 관련 API 액세스 오류에 따른 인터셉터 예외 URL 추가
* 2024-07-02 : 카트 추가 기능 구현
* 2024-07-03 : nginx를 이용한 웹서버 환경 구축
* 2024-07-04 : SSL 인증서를 추가한 백엔드용 임시 도메인 구축 및 API 도메인 주소 이전 (http://58.238.170.xxx:4001 -> https://project1.babychat.xyz) (이전 채팅 프로젝트의 서브 도메인 사용)
### 작성중