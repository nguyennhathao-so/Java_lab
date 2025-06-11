$(document).ready(function () {
  function updateHeader() {
    console.log('Updating header...'); // Debug log
    const userEmail = localStorage.getItem('userEmail');
    const userName = localStorage.getItem('userName');
    const authToken = localStorage.getItem('authToken');
    const role = localStorage.getItem('role')?.toUpperCase();

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

      // Show service and notification links for logged in users
      $('#service-link').show();
      $('#notification-link').show();

      // Kiểm tra role và hiển thị/ẩn link admin
      if (role === "ADMIN" || role === "STAFF") {
        console.log('User is admin/staff, showing admin link'); // Debug log
        $('#admin-link').show();
      } else {
        console.log('User is not admin/staff, hiding admin link'); // Debug log
        $('#admin-link').hide();
      }
    } else {
      console.log('User is not logged in, showing guest view...'); // Debug log
      $('#guest-view').show();
      $('#user-view').hide();
      $('#admin-link').hide(); // Ẩn link admin khi chưa đăng nhập
      
      // Hide service and notification links for guests
      $('#service-link').hide();
      $('#notification-link').hide();
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