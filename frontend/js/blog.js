// Kiểm tra đăng nhập khi bấm Đăng trên form blog
// Tác giả: AI hỗ trợ

document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector(".post-box form");
  if (form) {
    form.addEventListener("submit", async function (e) {
      const token = localStorage.getItem("authToken");
      if (!token) {
        e.preventDefault();
        alert("Bạn cần đăng nhập");
        window.location.href = "signin_signup.html";
        return false;
      }

      e.preventDefault(); // Ngăn reload trang

      // Lấy dữ liệu từ form
      const title = document.getElementById("title").value.trim();
      const content = document.getElementById("content").value.trim();

      if (!title || !content) {
        alert("Vui lòng nhập đầy đủ tiêu đề và nội dung!");
        return;
      }

      try {
        // Gửi dữ liệu lên backend
        const response = await fetch("http://localhost:8082/api/blogs", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
          body: JSON.stringify({ title, content }),
        });

        if (!response.ok) {
          throw new Error("Đăng bài thất bại!");
        }

        const newPost = await response.json();

        // Thêm bài viết mới vào đầu danh sách diễn đàn
        const forumBox = document.querySelector(".forum-box");
        const postItem = document.createElement("div");
        postItem.className = "post-item";
        postItem.innerHTML = `
          <div class="post-header">
            <img src="./assets/images/doctors/doctor1.jpeg" alt="Avatar" class="post-avatar">
            <span class="post-name">${newPost.author || "Bạn"}</span>
          </div>
          <div class="post-content" data-short="${
            newPost.content
          }" data-full="${newPost.content}">
            ${newPost.content}
          </div>
          <div class="post-readmore">Đọc Thêm</div>
        `;
        forumBox.prepend(postItem);

        // Reset form
        form.reset();

        // Gắn lại sự kiện Đọc Thêm cho bài mới
        postItem
          .querySelector(".post-readmore")
          .addEventListener("click", function () {
            const contentDiv = this.previousElementSibling;
            const isExpanded = this.textContent === "Thu Gọn";
            if (isExpanded) {
              contentDiv.textContent = contentDiv.getAttribute("data-short");
              this.textContent = "Đọc Thêm";
            } else {
              contentDiv.textContent = contentDiv.getAttribute("data-full");
              this.textContent = "Thu Gọn";
            }
          });
      } catch (err) {
        alert(err.message || "Có lỗi xảy ra!");
      }
    });
  }
});
