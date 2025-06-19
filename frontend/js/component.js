$(document).ready(function () {
  function updateHeader() {
    console.log('Updating header...'); // Debug log
    const userEmail = localStorage.getItem('userEmail');
    const userName = localStorage.getItem('userName');
    const authToken = localStorage.getItem('authToken');
    const role = localStorage.getItem('role')?.toUpperCase();

    console.log('User info from localStorage:', {
      email: userEmail,
      name: userName,
      token: authToken,
      role: role
    });

    if (userEmail && authToken) {
      $('#guest-view').hide();
      $('#user-view').show();
      $('#user-email').text(userName || userEmail);
      $('#service-link').show();
      $('#notification-link').show();

      if (role === "ADMIN" || role === "STAFF") {
        $('#admin-link').show();
      } else {
        $('#admin-link').hide();
      }
    } else {
      $('#guest-view').show();
      $('#user-view').hide();
      $('#admin-link').hide();
      $('#service-link').hide();
      $('#notification-link').hide();
    }
  }

  $("#header").load("components/header.html", function () {
    console.log('Header loaded');
    updateHeader();

    // Nếu vẫn cần giữ logic cũ:
    if (typeof initHeaderAuth === "function") {
      initHeaderAuth();
    }
  });

  window.addEventListener('storage', function (e) {
    console.log('Storage changed:', e);
    updateHeader();
  });

  $("#footer").load("components/footer.html");
});
