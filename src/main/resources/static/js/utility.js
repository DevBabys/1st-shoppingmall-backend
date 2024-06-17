var apiBaseUrl = 'http://localhost:8888/'

// API 처리 함수
function restApi(httpMethod, data, url, token="") {
    var method = "";
    var apiUrl = apiBaseUrl + url;
    var jsonData = JSON.stringify(data);

    // HTTP 메소드 확인
    if (httpMethod == 'get' || httpMethod == "GET")             method = 'GET';
    else if (httpMethod == 'post' || httpMethod == "POST")      method = 'POST';

    // 토큰 확인
    if (token != "") {
        // HTTP 통신
        return fetch(apiUrl, {
            method: method,
            headers: {
                'Content-Type' : 'application/json',
                'Authorization' : 'Bearer ' + token
            },
        })
        .then(async response => {
            return await response.json();
        })
        .then(data => {
            return data;
        })
        .catch(error => {
            console.error('HTTP Method 요청 중 오류 발생:', error);
        });
    } else {
        // HTTP 통신
        return fetch(apiUrl, {
            method: method,
            headers: { 'Content-Type' : 'application/json' },
            body: jsonData
        })
        .then(async response => {
            return await response.json();
        })
        .then(data => {
            return data;
        })
        .catch(error => {
            console.error('HTTP Method 요청 중 오류 발생:', error);
        });
    }
}

/**
* 쿠키 설정 함수
* @param {string} name: 쿠키 이름
* @param {string} value: 쿠키 값
* @param {number} days: 쿠키 유효 기간
* @return {void}
* 쿠키 설정 예제
* setCookie("username", "John Doe", 7); // 7일 동안 유효한 쿠키 설정
*/
function setCookie(name, value, days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

// 쿠키 값을 가져오는 함수
function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

// 쿠키 삭제 함수
function deleteCookie(name) {
    document.cookie = name + '=; Max-Age=-99999999;';
}

function hello() {
    return "hello";
}