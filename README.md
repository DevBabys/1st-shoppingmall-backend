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
> **application-database.properties** : 데이터베이스 설정 정보  
> **application-env.properties** : Front-End URL 주소, API Key 등 설정 정보

### Reference
* Front-End (React Project) / 전예경
> **Github** : https://github.com/yek-j/shopping-front-app  
> **HOST** : https://shopping-front-app.vercel.app
* Back-End (Spring Boot Project) / 고은별
> **Github** : https://github.com/DevBabys/1st-shoppingmall-backend  
> **HOST** : https://project1.babychat.xyz
* DevBabys Team
> **GitHub** : https://github.com/DevBabys  
> **Notion** : https://www.notion.so/ShoppingMall-ERD-42cc7fb576274bffbd351d9d241e7c8b
* Project Information
> **PPT** : https://docs.google.com/presentation/d/1k21m5FauVM9W0OejN_bLNB3oxGLDfavWBdHiqaCZ_NE/edit#slide=id.p  
> **API 기능 명세서** : https://docs.google.com/spreadsheets/d/1bSlyo5-Lb6NDKC9yH98G6WUEhLEeJteOSwbB86eL2vc/edit#gid=0  
> **데이터베이스 테이블 설계서 (ERD)** : https://docs.google.com/document/d/1tydQcgZFXc3Rx_fnkfzR2DHmZTuw1eFjfNSMJKOgbQI/edit  


# 작업 히스토리
* **2024-05-31** : API 형식의 회원가입 기능 구현
* **2024-06-04** : 로그인/로그아웃 기능 구현
* **2024-06-07** : 회원 정보 보기 및 수정, 이메일 찾기, 비밀번호 변경하기 기능 구현
* **2024-06-10** : 상품 카테고리 목록 조회, 추가, 수정, 삭제 기능 구현
* **2024-06-13** : 로그인 토큰 관련 JWT 필터 예외 처리 추가
* **2024-06-17** : 상품 상세 조회 기능 구현
* **2024-06-19** : 상품 추가 및 수정 기능 구현 (파일 업로드 기능 포함)
* **2024-06-20** : 권한에 따른 액세스를 허용하는 인터셉터 추가 및 상품 카테고리, 상품 API 권한 확인 로직 추가
* **2024-06-24** : 카테고리, 상품 조회 시 프론트 엔드에서 보여줄 페이지의 수를 표현하기 위해 조회 API 응답 구조 변경, 조회 관련 API 액세스 오류에 따른 인터셉터 예외 URL 추가
* **2024-07-02** : 카트 추가 기능 구현
* **2024-07-03** : nginx를 이용한 웹서버 환경 구축
* **2024-07-04** : SSL 인증서를 추가한 백엔드용 임시 도메인 구축 및 API 도메인 주소 이전 (http://58.238.170.xxx:4001 -> https://project1.babychat.xyz) (이전 채팅 프로젝트의 서브 도메인 사용)
* **2024-07-08** : 카트 목록 조회, 수정 기능 구현, 카트 추가 로직 수정(카트에 존재할 경우 해당 카트 번호를 찾아 수량을 더하도록 함)
* **2024-07-09** : 관리자와 소유주(글 작성자, 카트 수정 등) 외 액세스 차단 인터셉터 추가, 리퀘스트 캐시 필터 추가, 카트 삭제 기능 구현, 협업을 위한 로직 내 주석 추가
* **2024-08-01** : 결제 테이블 설계
* **2024-08-02** : 범용 액세스 인터셉터 사용을 위한 접근 액세스를 담당하는 인터셉터 수정(CertainUserAccessInterceptor)
* **2024-08-05** : 상품 조회 로직 수정 (리스트에서 상품의 이름과 카테고리명을 식별할 수 있도록 반환값 추가)
* **2024-08-07** : 테스트 서버에서 사용하기 편하게 JWT 암호화 방식을 동적에서 정적으로 변경 (JwtUtil.kt 참고, secret-key는 application-env.properties 참고), 리퀘스트 캐시화 필터로 인한 이미지 업로드 API 오류 수정(멀티 파트는 캐시 필터에서 제외하여 로직을 수행하도록 함)
* **2024-08-08 ~** : 결제 로직(DTO, Repository, Controller, Service) 설계
> * **2024-08-08** : 결제 요청 기능 구현
> * **2024-08-22** : 결제 완료 기능 구현
> * **2024-08-23** : 결제 실패 기능 구현
* **2024-08-26** : 회원 정보 수정 기능 수정(기존 : 상세 정보 필요 -> 변경 : 이름과 비밀번호 정보만 필요), 주문 조회 및 기간별 주문 조회, 주문 상태 업데이트 기능 구현


### 작성중