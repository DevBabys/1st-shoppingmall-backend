/* 결제 */
document.getElementById('checkout-form').addEventListener('submit', function(e) {
    e.preventDefault();
    alert('Your order has been placed successfully!');
    window.location.href = 'index.html'; // Redirect to home page after placing order
});