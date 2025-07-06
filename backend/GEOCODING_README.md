# Chức năng Geocoding với Nominatim API

## Tổng quan
Chức năng này sử dụng Nominatim API để chuyển đổi địa chỉ thành tọa độ (latitude, longitude) khi đăng ký thông tin user.

## Các thành phần đã tạo

### 1. NominatimResponse.java
- DTO class để map response từ Nominatim API
- Chứa thông tin về tọa độ, địa chỉ chi tiết

### 2. GeocodingService.java
- Service chính để gọi Nominatim API
- Có 2 method:
  - `geocodeAddress(String address)`: Chuyển đổi địa chỉ thành tọa độ
  - `geocodeAddressWithTimeout(String address)`: Chuyển đổi với timeout 10 giây

### 3. LocationUpdateService.java
- Service để cập nhật tọa độ cho các user đã có sẵn
- Có 2 method:
  - `updateLocationsForAllUsers()`: Cập nhật cho tất cả user chưa có location
  - `updateLocationForUser(String userId)`: Cập nhật cho một user cụ thể

### 4. GeocodingController.java
- Controller để quản lý chức năng geocoding
- Các endpoint:
  - `POST /api/geocoding/update-all-users`: Cập nhật tọa độ cho tất cả user
  - `POST /api/geocoding/update-user/{userId}`: Cập nhật tọa độ cho user cụ thể

## Cách hoạt động

### Khi đăng ký user mới:
1. User nhập thông tin đăng ký bao gồm địa chỉ
2. Hệ thống tự động gọi Nominatim API để chuyển đổi địa chỉ thành tọa độ
3. Tọa độ được lưu vào cột `location` của bảng `users`
4. Nếu không thể chuyển đổi, user vẫn được đăng ký thành công (location = null)

### Format tọa độ:
- Tọa độ được lưu dưới dạng string: `"latitude,longitude"`
- Ví dụ: `"10.762622,106.660172"`

## API Endpoints

### Cập nhật tọa độ cho tất cả user
```http
POST /api/geocoding/update-all-users
```

### Cập nhật tọa độ cho user cụ thể
```http
POST /api/geocoding/update-user/US1
```

## Cấu hình

### Dependencies đã thêm:
- `spring-boot-starter-webflux`: Để gọi HTTP API

### User-Agent:
- Nominatim yêu cầu User-Agent header
- Đã cấu hình: `"BloodDonationApp/1.0"`

### Rate Limiting:
- Nominatim có giới hạn 1 request/giây
- Đã thêm timeout 10 giây cho mỗi request

## Lưu ý quan trọng

1. **Rate Limiting**: Nominatim có giới hạn request, không nên gọi quá nhiều request liên tiếp
2. **Độ chính xác**: Kết quả phụ thuộc vào độ chính xác của địa chỉ nhập vào
3. **Error Handling**: Nếu API lỗi, quá trình đăng ký vẫn tiếp tục (không throw exception)
4. **Logging**: Các thao tác geocoding được log ra console để debug

## Sử dụng trong code

### Trong UserService:
```java
// Chuyển đổi địa chỉ thành tọa độ khi đăng ký
if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
    String coordinates = geocodingService.geocodeAddressWithTimeout(request.getAddress());
    if (coordinates != null) {
        user.setLocation(coordinates);
    }
}
```

### Trong Repository:
```java
// Tìm user chưa có location
List<User> findByLocationIsNull();
```

## Testing

1. Đăng ký user mới với địa chỉ hợp lệ
2. Kiểm tra cột `location` trong database
3. Sử dụng endpoint update để cập nhật user cũ 