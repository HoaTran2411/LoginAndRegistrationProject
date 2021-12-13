# Mô tả project
## Màn hình
### Màn hình Login:
Gồm email và password. Email chính là NaturalID đó. Màn hình này có 2 link: tạo mới tài khoản (Create Account) và tìm lại mật khẩu (Get New Password). Nếu login thành công thì cần hiển thị thông tin:
- Full Name
- Email
- Danh sách các vài trò (roles) user đó được gán
- Hyper link để update Full Name và Email
- Hyper link để update Password
- Lịch sử truy cập gồm có các sự kiện sắp xếp mới nhất đến cũ nhất:
  + Login - time stamp
  + Update Full Name or Email - time stamp
  + Update password - time stamp
  + Retrieve password - time stamp
  + Create Account - time stamp
### Màn hình Create Account
  Gồm có các trường:
  + Họ và tên
  + Email
  + Password
### Màn hình Get New Password:
  Chỉ cần một trường duy nhất là Email. Khi người dùng quên mật khẩu, anh ta cung cấp email, ứng dụng kiểm tra nếu email có trong bảng User thì sinh ra một password mới, băm rồi lưu vào bảng user đồng thời gửi email chưa password mới đến user. Bạn có thể lập trình chức năng gửi email nhưng để đơn giản trước mắt chỉ cần hiển thị password mới luôn ra màn hình.

### Màn hình Udate Role. 
Chỉ có admin khi đăng nhập, thì có thêm link để tìm kiếm user theo email. Nếu tìm được thì update Role.
Hiện thị tất cả các role dạng check box. Tick vào checkbox nào thì user được gán role đó.
Màn hình này chỉ admin mới được truy cập. Anonymous user hay user khác vào là hiển thị cảnh báo Forbidden Access ngay.

### Màn hình Find User. 
Màn hình này chỉ admin mới được truy cập. Anonymous user hay user khác vào là hiển thị cảnh báo Forbidden Access ngay.

### Phân quyền và dữ liệu ban đầu
Ứng dụng được khởi tạo với một user đầu tiên là admin, email: admin@techmaster.vn có role là admin, password mặc định là r@0T. Hãy dùng file sql user.sql để nạp dữ liệu.

Danh sách các vai trò gồm có:
- admin
- customer
- developer
- sales
- operator
- trainer
Danh sách các vai trò có thể bổ xung trong tương lai. Ban đầu hãy tạo file role.sql để nạp những vai trò mặc định vào ứng dụng.