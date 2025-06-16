$(document).ready(function () {
  $("#header").load("components/header.html", function () {
    if (typeof initHeaderAuth === "function") {
      initHeaderAuth();
    }
  });
  $("#footer").load("components/footer.html");
});