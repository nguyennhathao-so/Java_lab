$(document).ready(function () {
  function updateHeader() {
    console.log('Updating header...'); // Debug log
    const userEmail = localStorage.getItem('userEmail');
    const userName = localStorage.getItem('userName');
    const authToken = localStorage.getItem('authToken');
    const role = localStorage.getItem('role');

    console.log('User info from localStorage:', { // Debug log
      email: userEmail,
      name: userName,
      token: authToken,
      role: role
    });

    if (userEmail && authToken) {
      console.log('User is logged in, updating UI...'); // Debug log
      $('#guest-view').hide();
      $('#user-view').show();
      $('#user-email').text(userName || userEmail);
    } else {
      console.log('User is not logged in, showing guest view...'); // Debug log
      $('#guest-view').show();
      $('#user-view').hide();
    }

    // Kiểm tra role admin
    if (role === "admin") {
      const adminLink = $('<a href="admin/index.html">Quản lý</a>');
      $("nav").prepend(adminLink);
    }
  }

  $("#header").load("components/header.html", function() {
    console.log('Header loaded, updating content...'); // Debug log
    updateHeader();
  });
  
  // Thêm event listener để cập nhật header khi localStorage thay đổi
  window.addEventListener('storage', function(e) {
    console.log('Storage changed:', e); // Debug log
    updateHeader();
  });
  
  $("#footer").load("components/footer.html");
});