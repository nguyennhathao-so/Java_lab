$(document).ready(function () {
  $("#header").load("components/header.html", function () {
    const role = localStorage.getItem("role");
    if (role === "admin") {
      const adminLink = $('<a href="admin.html">Quản lý</a>');
      $("nav").prepend(adminLink);
    }
  });
  $("#footer").load("components/footer.html");
});