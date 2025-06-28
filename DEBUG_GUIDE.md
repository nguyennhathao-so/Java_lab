# Hướng Dẫn Debug và Test

## Vấn Đề Hiện Tại
Lỗi: `Cannot read properties of null (reading 'value')` ở dòng 537 trong `services.html`

## Các Bước Debug

### 1. Kiểm Tra Console Browser
1. Mở Developer Tools (F12)
2. Vào tab Console
3. Tải lại trang `services.html`
4. Kiểm tra các log:
   - "Services.html loaded"
   - "All resources loaded"
   - "apiService available: true/false"
   - "adminApiService available: true/false"

### 2. Test API Service
1. Mở `http://localhost:8081/test_api.html`
2. Kiểm tra xem API có hoạt động không
3. Nếu có lỗi, kiểm tra:
   - Backend có chạy không (`mvn spring-boot:run`)
   - Frontend có chạy không (`npx http-server . -p 8081 --cors`)
   - CORS có được cấu hình đúng không

### 3. Kiểm Tra Network Tab
1. Mở Developer Tools > Network
2. Tải lại trang
3. Kiểm tra các file JavaScript có load thành công không:
   - `api.js`
   - `admin-api.js`
   - `component.js`
   - `form-toggle.js`

### 4. Test Form Đăng Ký Cần Máu
1. Điền form đăng ký cần máu
2. Nhấn "Đăng Ký"
3. Kiểm tra console có lỗi gì không
4. Kiểm tra Network tab xem request có được gửi không

## Các Lỗi Thường Gặp

### Lỗi 1: apiService is undefined
**Nguyên nhân**: File `api.js` không load được
**Giải pháp**:
- Kiểm tra đường dẫn file `js/api.js`
- Kiểm tra syntax trong file `api.js`
- Thêm `defer` attribute cho script tag

### Lỗi 2: Element not found
**Nguyên nhân**: Element chưa được tạo khi script chạy
**Giải pháp**:
- Đợi DOM load xong với `window.addEventListener('load')`
- Kiểm tra ID của element có đúng không

### Lỗi 3: CORS Error
**Nguyên nhân**: Backend không cho phép request từ frontend
**Giải pháp**:
- Kiểm tra `@CrossOrigin` trong backend
- Đảm bảo backend chạy trên port 8082
- Đảm bảo frontend chạy trên port 8081

## Cách Sửa Lỗi

### 1. Sửa Lỗi Null Pointer
```javascript
// Thay vì
const value = document.getElementById('element').value;

// Sử dụng
const element = document.getElementById('element');
if (element) {
    const value = element.value;
}
```

### 2. Đảm Bảo Script Load Đúng Thứ Tự
```html
<script src="js/jquery-3.7.1.min.js"></script>
<script src="js/api.js" defer></script>
<script src="js/admin-api.js" defer></script>
```

### 3. Kiểm Tra Service Trước Khi Sử Dụng
```javascript
if (typeof apiService === 'undefined') {
    alert('Hệ thống đang tải, vui lòng thử lại sau.');
    return;
}
```

## Test Cases

### Test Case 1: Load Page
1. Mở `http://localhost:8081/services.html`
2. Kiểm tra console không có lỗi
3. Kiểm tra form hiển thị đúng

### Test Case 2: Fill Form
1. Điền form đăng ký cần máu
2. Kiểm tra thông tin tự động điền
3. Kiểm tra validation hoạt động

### Test Case 3: Submit Form
1. Nhấn "Đăng Ký"
2. Kiểm tra request được gửi
3. Kiểm tra response từ backend
4. Kiểm tra form reset đúng

## Logs Cần Kiểm Tra

### Console Logs
```
Services.html loaded
All resources loaded
apiService available: true
adminApiService available: true
jQuery available: true
```

### Network Logs
- `GET /js/api.js` - 200 OK
- `GET /js/admin-api.js` - 200 OK
- `POST /api/donation-requests` - 200 OK

## Nếu Vẫn Có Lỗi

1. **Clear Browser Cache**: Ctrl+Shift+R
2. **Check Backend Logs**: Xem console của `mvn spring-boot:run`
3. **Check Frontend Logs**: Xem console của `npx http-server`
4. **Test API Directly**: Sử dụng Postman hoặc curl
5. **Check Database**: Đảm bảo database có dữ liệu

## Contact
Nếu vẫn gặp vấn đề, hãy cung cấp:
- Screenshot console error
- Network tab logs
- Backend logs
- Steps to reproduce 