// Kiểm tra đăng nhập khi bấm Đăng trên form blog
// Tác giả: AI hỗ trợ

// Hàm load danh sách blog
async function loadBlogs() {
  const forumList = document.querySelector(".forum-list");
  // Lưu lại nội dung cũ (bài mẫu)
  const oldContent = forumList.innerHTML;
  try {
    const res = await fetch("http://localhost:8082/api/blogs");
    const data = await res.json();
    // Giữ lại bài mẫu, chỉ append bài backend vào sau
    forumList.innerHTML = oldContent;
    data.reverse().forEach((blog) => {
      const authorName = blog.author?.name || "Ẩn danh";
      // Nếu là bài mẫu A/B thì không render lại (đã có trong HTML)
      if (authorName === "Nguyen Van A" || authorName === "Nguyen Van B")
        return;
      // Sử dụng avatar mặc định user gửi
      const avatar = "./assets/images/blog/image.png";
      const postItem = document.createElement("div");
      postItem.className = "post-item";
      postItem.innerHTML = `
        <div class="post-header">
          <img src="${avatar}" alt="Avatar" class="post-avatar">
          <span class="post-name">${authorName}</span>
        </div>
        <div class="post-content" data-short="${blog.content}" data-full="${blog.content}">
          ${blog.content}
        </div>
        <div class="post-readmore">Đọc Thêm</div>
      `;
      forumList.appendChild(postItem);
    });
    // Gắn lại sự kiện Đọc Thêm cho tất cả bài viết
    attachReadMoreEvents();
  } catch (err) {
    forumList.innerHTML +=
      '<div style="color:red">Không tải được bài viết!</div>';
  }
}

function attachReadMoreEvents() {
  document.querySelectorAll(".post-readmore").forEach((button) => {
    button.onclick = function () {
      const content = this.previousElementSibling;
      const isExpanded = this.textContent === "Thu Gọn";
      if (isExpanded) {
        content.textContent = content.getAttribute("data-short");
        this.textContent = "Đọc Thêm";
      } else {
        content.textContent = content.getAttribute("data-full");
        this.textContent = "Thu Gọn";
      }
    };
  });
}

document.addEventListener("DOMContentLoaded", function () {
  loadBlogs();
  // Gắn sự kiện cho bài mẫu khi vừa load trang
  attachReadMoreEvents();
  // Kiểm tra đăng nhập khi bấm Đăng trên form blog
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

        // Sau khi đăng thành công, load lại danh sách blog
        await loadBlogs();

        // Reset form
        form.reset();
      } catch (err) {
        alert(err.message || "Có lỗi xảy ra!");
      }
    });
  }
});
