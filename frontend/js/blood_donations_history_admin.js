document.addEventListener("DOMContentLoaded", () => {
    // Xử lý nút XÓA
    document.querySelectorAll(".delete").forEach(btn => {
        btn.addEventListener("click", async () => {
            const row = btn.closest("tr");
            const requestId = row.getAttribute("data-id");
            if (!requestId) return alert("Không tìm thấy ID!");

            if (!confirm("Bạn chắc chắn muốn xóa?")) return;

            try {
                await adminApiService.deleteBloodRequest(requestId);
                row.remove();
                alert("Đã xóa thành công!");
            } catch (err) {
                alert("Lỗi khi xóa: " + (err.message || "Không xác định"));
            }
        });
    });

    // Xử lý nút ĐỒNG Ý
    document.querySelectorAll(".accept").forEach(btn => {
        btn.addEventListener("click", async () => {
            const row = btn.closest("tr");
            const requestId = row.getAttribute("data-id");
            if (!requestId) return alert("Không tìm thấy ID!");

            try {
                await adminApiService.approveBloodRequest(requestId);
                alert("Đã duyệt thành công!");
                // Có thể cập nhật lại row hoặc reload lại bảng
            } catch (err) {
                alert("Lỗi khi duyệt: " + (err.message || "Không xác định"));
            }
        });
    });

    // Xử lý form CẬP NHẬT (nếu có)
    const form = document.querySelector("form");
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            alert("Đã cập nhật thông tin hiến máu!");
        });
    }
});