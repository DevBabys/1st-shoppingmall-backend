const accessToken = ""

window.onload = function() {
    // 회원가입 이벤트 리스너
    document.getElementById('registerButton').addEventListener('click', async function(e) {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirm-password').value;
        const username = document.getElementById('username').value;

        if (password == confirmPassword) {
            userInfo = {"email" : email, "password" : password, "username" : username};
            let response = await restApi('post', userInfo, 'user/register');

            if (response.result == "success") {
                alert("회원 등록이 완료되었습니다.\n환영합니다!");
                window.location.href = '/';
            }
            else if (response.result == "fail") {
                alert(response.value);
            }
        } else {
            alert("비밀번호가 일치하지 않습니다.");
        }
    });

    // 로그인 이벤트 리스너
    document.getElementById('loginButton').addEventListener('click', async function(e) {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        if (password === confirmPassword) {
            alert('Registration successful');
            window.location.href = 'login.html'; // Redirect to login page after registration
        } else {
            alert('Passwords do not match');
        }
    });
}

function test() {
    alert("Hello World!");
}

function getToken() {
    accessToken = localStorage.getItem("accessToken");
    return accessToken;
}

async function checkToken(accessToken) {
    let response = await restApi('get', "", '/user/checktoken', accessToken);
    if (response.result != "success") {
        alert("로그인 토큰 유효 기간이 만료되었습니다. 다시 로그인해주세요.");
        localStorage.clear();
    } else if (response.userInfo.email != email) {
        alert("이중 로그인이 확인되었습니다. 다시 로그인해주세요.");
        localStorage.clear();
        window.location.href = "/main";
    }
}

function login(email, password) {

}