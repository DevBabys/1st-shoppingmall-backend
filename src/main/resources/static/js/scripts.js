/* 인덱스 배너 슬라이드 */
let slideIndex = 0;
showSlides();

function showSlides() {
    let slides = document.querySelectorAll(".slides img");
    for (let i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    slideIndex++;
    if (slideIndex > slides.length) {slideIndex = 1}
    slides[slideIndex - 1].style.display = "block";
    setTimeout(showSlides, 2000); // Change image every 2 seconds
}

/* 로그인 및 회원가입 */
document.getElementById('login-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Perform login action (this is just an example)
    if (email === 'user@example.com' && password === 'password') {
        alert('Login successful');
        window.location.href = 'index.html'; // Redirect to home page after login
    } else {
        alert('Invalid email or password');
    }
});

document.getElementById('register-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    // Perform registration action (this is just an example)
    if (password === confirmPassword) {
        alert('Registration successful');
        window.location.href = 'login.html'; // Redirect to login page after registration
    } else {
        alert('Passwords do not match');
    }
});

/* 결제 */
document.getElementById('checkout-form').addEventListener('submit', function(e) {
    e.preventDefault();
    alert('Your order has been placed successfully!');
    window.location.href = 'index.html'; // Redirect to home page after placing order
});