# 코틀린 스프링부트 개발 환경
### *Use Env*
* Java Version : 21
* Gradle 8.5
* Spring Boot 3.2.5
* Port Number : 4001
* Branch : 은별 - eunbyeol

### Dev Env
* application.properties 파일 의도적으로 배제하였음. 참고.

### Reference
* Frontend (React Project)
> Github : https://github.com/yek-j/shopping-front-app  
> HOST : https://shopping-front-app.vercel.app
* DevBabys Team GitHub
>https://github.com/DevBabys
* PPT
> https://docs.google.com/presentation/d/1k21m5FauVM9W0OejN_bLNB3oxGLDfavWBdHiqaCZ_NE/edit#slide=id.p
* API 기능 명세서   
> https://docs.google.com/spreadsheets/d/1bSlyo5-Lb6NDKC9yH98G6WUEhLEeJteOSwbB86eL2vc/edit#gid=0
* 데이터베이스 테이블 설계서 (ERD)
> https://docs.google.com/document/d/1tydQcgZFXc3Rx_fnkfzR2DHmZTuw1eFjfNSMJKOgbQI/edit
* Notion
> https://www.notion.so/ShoppingMall-ERD-42cc7fb576274bffbd351d9d241e7c8b

# 작업 히스토리
### 회원가입
* 2024-05-31 : API 형식의 회원가입 기능 구현
* 2024-06-04 : 로그인/로그아웃 기능 구현
* 2024-06-07 : 회원 정보 보기 및 수정, 이메일 찾기, 비밀번호 변경하기 기능 구현
* 2024-06-10 : 상품 카테고리 목록 조회, 추가, 수정, 삭제 기능 구현
### 작성중

---
# 자바스크립트 함수 설명
## RESTful API 함수
```
restApi(httpMethod, data, url, token="")
```
* httpMethod : [필수파라미터] 수행할 HTTP 메소드 (GET/POST)
* data : [필수파라미터] API 서버에 보낼 데이터
* url : [필수파라미터] API 서버에서 기능 수행을 위한 요청 주소
* token : [옵션] 회원 관련 기능에 인증이 필요 할 경우 기입   
#### 예시
_이메일, 비밀번호를 이용한 로그인_
```
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    // API 서버에서 회원 정보 확인
    userInfo = {"email" : email, "password" : password};
    response = await restApi('post', userInfo, url);

    // 로그인 성공
    if (response.result == "success") {
        // 자동 로그인 확인
        isAutoLogin = document.getElementById("chkAutoLogin").checked;
        if (isAutoLogin)    localStorage.setItem("autoLogin", "Y");
        else    localStorage.setItem("autoLogin", "N");

        // 토큰 정보 로컬 스토리지에 저장
        localStorage.setItem("accessToken", response.accessToken);

        // 페이지 이동
        window.location.href="/";
    }
    // 로그인 실패
    else if (response.result == "error") {
        alert(response.message);
    }
```
_토큰을 이용한 회원 정보 확인_
```
    // 유저 유효 토큰 확인
    let response = await restApi('get', "", 'user/apiuserinfo', localStorage.getItem("accessToken"));
    if (response.result != "success") {
        alert("로그인 토큰 유효 기간이 만료되었습니다. 다시 로그인해주세요.");
        localStorage.clear();
        window.location.href = "/main";
    } else if (response.userInfo.email != email) {
        alert("이중 로그인이 확인되었습니다. 다시 로그인해주세요.");
        localStorage.clear();
        window.location.href = "/main";
    }
    else {
        // 내 정보에 유저 정보 입력
        document.getElementById("myInfoEmail").value = email;
        document.getElementById("myInfoName").value = userName;
    }
```

## 쿠키 관련 함수
