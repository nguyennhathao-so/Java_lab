// Check authentication status when page loads
$(document).ready(function () {
    // Wait for header to be loaded
    if ($('#admin-link').length === 0) return;

    const token = localStorage.getItem('authToken');
    const user = JSON.parse(localStorage.getItem('userData'));

    const guestView = document.getElementById('guest-view');
    const userView = document.getElementById('user-view');
    const adminLink = document.getElementById('admin-link');
    const userName = document.getElementById('user-name');
    const logoutBtn = document.getElementById('logout-btn');

    if (token && user) {
        // User is logged in
        guestView.style.display = 'none';
        userView.style.display = 'block';
        userName.textContent = user.name;

        // Show admin link if user is admin or staff
        if (user.role === 'ADMIN' || user.role === 'STAFF') {
            adminLink.style.display = 'block';
        }

        // Hide service and notification links for admin and staff
        const serviceLink = document.getElementById('service-link');
        const notificationLink = document.getElementById('notification-link');
        
        if (serviceLink && notificationLink) {
            if (user.role === 'ADMIN' || user.role === 'STAFF') {
                serviceLink.style.display = 'none';
                notificationLink.style.display = 'none';
            } else {
                serviceLink.style.display = 'inline-block';
                notificationLink.style.display = 'inline-block';
            }
        }

        // Handle logout
        logoutBtn.addEventListener('click', function (e) {
            e.preventDefault();
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = 'home.html';
        });
    } else {
        // User is not logged in
        guestView.style.display = 'block';
        userView.style.display = 'none';
        adminLink.style.display = 'none';
    }
});

function initHeaderAuth() {
    const token = localStorage.getItem('authToken');
    const user = JSON.parse(localStorage.getItem('userData'));

    const guestView = document.getElementById('guest-view');
    const userView = document.getElementById('user-view');
    const adminLink = document.getElementById('admin-link');
    const userName = document.getElementById('user-name');
    const logoutBtn = document.getElementById('logout-btn');

    if (!guestView || !userView || !adminLink || !userName || !logoutBtn) return;

    console.log('user:', user);
    console.log('token:', token);
    console.log('guestView:', guestView);
    console.log('userView:', userView);
    console.log('adminLink:', adminLink);
    console.log('userName:', userName);
    console.log('logoutBtn:', logoutBtn);

    if (token && user) {
        guestView.style.display = 'none';
        userView.style.display = 'block';
        userName.textContent = user.name;
        if (user.role === 'ADMIN' || user.role === 'STAFF') {
            adminLink.style.display = 'block';
        }

        // Hide service and notification links for admin and staff
        const serviceLink = document.getElementById('service-link');
        const notificationLink = document.getElementById('notification-link');
        
        if (serviceLink && notificationLink) {
            if (user.role === 'ADMIN' || user.role === 'STAFF') {
                serviceLink.style.display = 'none';
                notificationLink.style.display = 'none';
            } else {
                serviceLink.style.display = 'inline-block';
                notificationLink.style.display = 'inline-block';
            }
        }
        logoutBtn.addEventListener('click', function (e) {
            e.preventDefault();
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = 'home.html';
        });
    } else {
        guestView.style.display = 'block';
        userView.style.display = 'none';
        adminLink.style.display = 'none';
    }
} 