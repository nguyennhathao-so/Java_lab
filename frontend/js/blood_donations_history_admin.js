document.addEventListener("DOMContentLoaded", () => {
    // Xử lý nút XÓA
    document.querySelectorAll(".delete").forEach(btn => {
        btn.addEventListener("click", () => {
            alert("Đã xóa yêu cầu.");
        });
    });

    // Xử lý nút ĐỒNG Ý
    document.querySelectorAll(".accept").forEach(btn => {
        btn.addEventListener("click", () => {
            alert("Đã đồng ý yêu cầu hiến máu.");
            // Nếu muốn chuyển sang approve.html sau khi đồng ý:
            window.location.href = "approve.html";
        });
    });

    // Xử lý form CẬP NHẬT
    const form = document.querySelector("form");
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            alert("Đã cập nhật thông tin hiến máu!");
        });
    }
});