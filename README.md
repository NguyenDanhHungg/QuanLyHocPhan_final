# Hệ Thống Đăng Ký Học Phần - Trường Đại Học An Giang

## 📋 Mô Tả Dự Án

Hệ thống đăng ký học phần là ứng dụng desktop Java Swing được phát triển để quản lý quy trình đăng ký học phần của sinh viên tại Trường Đại Học An Giang. Hệ thống hỗ trợ hai loại người dùng chính:

- **Sinh viên**: Đăng ký học phần, xem lịch sử đăng ký
- **Phòng Đào Tạo (PĐT/Admin)**: Quản lý học phần, duyệt đăng ký, cấu hình hệ thống

## ✨ Tính Năng Chính

### 🔐 Xác Thực và Đăng Nhập
- Đăng nhập bằng tài khoản Admin (PĐT)
- Đăng nhập bằng MSSV hoặc Email cho sinh viên
- Tạo tài khoản sinh viên mới
- Quên mật khẩu (liên hệ PĐT)

### 👨‍🎓 Dành Cho Sinh Viên

#### Đăng Ký Học Phần
- Xem danh sách học phần có thể đăng ký (theo học kỳ)
- Tìm kiếm học phần theo mã hoặc tên
- Phân loại học phần (GDTC, QP-AN, Ngoại ngữ, Lý luận chính trị, Toán-KHCB, Cơ sở ngành, Mô đun, v.v.)
- Thêm học phần vào giỏ đăng ký
- Xem tổng số tín chỉ đã đăng ký (tối thiểu 12 TC, tối đa 24 TC)
- Progress bar hiển thị tiến độ đăng ký
- Gửi đăng ký để PĐT duyệt
- Xóa học phần khỏi giỏ (chỉ khi chưa gửi hoặc trạng thái "Tạm")

#### Lịch Sử Đăng Ký
- Xem tất cả đăng ký qua các học kỳ
- Xem trạng thái đăng ký: Tạm, Đã gửi, Đã duyệt, Đã từ chối
- Màu sắc phân biệt trạng thái:
  - 🟢 Xanh lá: Đã duyệt
  - 🔴 Đỏ: Đã từ chối
  - 🟡 Vàng: Chờ duyệt (Tạm, Đã gửi)

### 👨‍💼 Dành Cho Phòng Đào Tạo (Admin)

#### 1. Quản Lý Học Phần Mở Trong Kỳ
- Xem danh sách học phần mở trong từng học kỳ
- Lọc học phần theo: học kỳ, thời gian, trạng thái mở/đóng, từ khóa tìm kiếm
- Xem số lượng sinh viên đã đăng ký mỗi học phần
- Tạo học phần mới và mở lớp cho học kỳ
- Cập nhật Offering (mở/đóng lớp, CTĐT được phép đăng ký)
- Xóa học phần (chỉ khi chưa có sinh viên đăng ký)

#### 2. Duyệt Đăng Ký Học Phần
- Xem tất cả đăng ký của sinh viên
- Lọc đăng ký theo:
  - Học kỳ
  - Trạng thái (Chờ duyệt, Đã duyệt, Đã từ chối)
  - Khoa/Viện (chương trình đào tạo)
  - Từ khóa tìm kiếm (tên/MSSV sinh viên, mã/tên học phần)
- Duyệt đăng ký (chuyển trạng thái thành "Đã duyệt")
- Từ chối đăng ký (chuyển trạng thái thành "Đã từ chối")
- Duyệt/từ chối hàng loạt (tất cả đăng ký đang chờ duyệt)

#### 3. Cài Đặt Học Phần
- Quản lý danh sách học phần master (không phụ thuộc học kỳ)
- Thêm học phần mới vào hệ thống
- Sửa thông tin học phần (mã, tên, số tín chỉ, loại)
- Xóa học phần (chỉ khi chưa có sinh viên đăng ký)
- Tìm kiếm và lọc học phần theo loại
- Xem thống kê số lượng sinh viên đã đăng ký

#### 4. Cài Đặt Kỳ Học
- Quản lý học kỳ (thêm, sửa, xóa)
- Mở/đóng đăng ký cho từng học kỳ
- Xem thông tin học kỳ: tên, năm học, ngày bắt đầu/kết thúc
- Lọc học kỳ theo năm học và trạng thái hoạt động

## 🛠️ Công Nghệ Sử Dụng

- **Ngôn ngữ**: Java
- **Framework UI**: Java Swing
- **Look & Feel**: FlatLaf (FlatMacLightLaf theme)
- **Kiến trúc**: MVC (Model-View-Controller)
- **Lưu trữ dữ liệu**: In-Memory (Memory class - dữ liệu chỉ tồn tại trong RAM)

## 📁 Cấu Trúc Dự Án

```
Project_Java-main/
├── src/
│   └── university/
│       └── registration/
│           ├── App.java                          # Entry point của ứng dụng
│           ├── controller/                       # Controller layer
│           │   ├── AdminController.java          # Controller cho AdminFrame
│           │   ├── LoginController.java          # Controller cho LoginFrame
│           │   └── StudentRegistrationController.java  # Controller cho StudentRegistrationFrame
│           ├── model/                            # Model layer (Entity)
│           │   ├── Course.java                   # Mô hình học phần
│           │   ├── Offering.java                 # Mô hình cấu hình mở lớp
│           │   ├── RegItem.java                 # Mô hình mục đăng ký
│           │   ├── Student.java                 # Mô hình sinh viên
│           │   └── TermSetting.java             # Mô hình cấu hình học kỳ
│           ├── service/                          # Service layer (Business Logic)
│           │   ├── AuthenticationService.java    # Xử lý đăng nhập, xác thực
│           │   ├── CourseService.java           # Xử lý logic học phần
│           │   ├── RegistrationService.java     # Xử lý logic đăng ký
│           │   ├── StudentService.java          # Xử lý logic sinh viên
│           │   └── TermService.java             # Xử lý logic học kỳ
│           ├── store/                            # Data layer
│           │   └── Memory.java                  # Lưu trữ dữ liệu trong bộ nhớ
│           ├── ui/                               # View layer (UI Components)
│           │   ├── AdminFrame.java              # Màn hình quản trị (PĐT)
│           │   ├── LoginFrame.java             # Màn hình đăng nhập
│           │   ├── CreateStudentDialog.java    # Dialog tạo tài khoản sinh viên
│           │   ├── RegistrationApprovalFrame.java  # Màn hình duyệt đăng ký (có thể không dùng)
│           │   ├── StudentRegistrationFrame.java    # Màn hình đăng ký học phần (sinh viên)
│           │   ├── UI.java                      # Utility class cho UI
│           │   ├── LookAndFeelUtil.java          # Thiết lập Look & Feel
│           │   └── components/                   # Custom UI components
│           │       ├── CardPanel.java           # Panel với bo góc và shadow
│           │       ├── BackgroundPanel.java    # Panel với gradient background
│           │       ├── HeroBackgroundPanel.java # Panel với hero background
│           │       ├── AguBadgePanel.java        # Badge logo AGU
│           │       ├── PrimaryButton.java        # Nút primary (màu xanh)
│           │       ├── NeutralButton.java       # Nút neutral (màu trắng)
│           │       ├── PlaceholderTextField.java # TextField với placeholder
│           │       ├── PlaceholderDateSpinner.java  # DateSpinner với placeholder
│           │       └── PlaceholderDateEditor.java   # DateEditor với placeholder
│           └── util/                             # Utility classes
│               └── LookAndFeelUtil.java          # (có thể trùng với ui/)
└── CourseRegistrationApp.iml                     # File cấu hình IntelliJ IDEA
```

## 🏗️ Kiến Trúc Hệ Thống

Hệ thống được xây dựng theo mô hình **MVC (Model-View-Controller)**:

### Model Layer (`model/`)
- Chứa các entity classes: `Course`, `Student`, `RegItem`, `Offering`, `TermSetting`
- Định nghĩa cấu trúc dữ liệu của hệ thống

### View Layer (`ui/`)
- Chứa các UI components (JFrame, JDialog, JPanel)
- Chỉ chịu trách nhiệm hiển thị và xử lý sự kiện UI
- Gọi Controller để xử lý logic nghiệp vụ

### Controller Layer (`controller/`)
- Điều phối giữa UI và Service
- Xử lý dữ liệu để hiển thị (format, transform)
- Validate dữ liệu đầu vào từ UI

### Service Layer (`service/`)
- Chứa business logic (logic nghiệp vụ)
- Xử lý các quy tắc nghiệp vụ (validation, tính toán)
- Gọi Memory để lưu trữ/lấy dữ liệu

### Data Layer (`store/`)
- Lớp `Memory`: Lưu trữ tất cả dữ liệu trong bộ nhớ (RAM)
- Sử dụng các Map và List để lưu trữ
- **Lưu ý**: Dữ liệu chỉ tồn tại trong RAM, mất khi tắt ứng dụng

## 🚀 Hướng Dẫn Cài Đặt và Chạy

### Yêu Cầu Hệ Thống
- **Java**: JDK 8 trở lên (khuyến nghị JDK 11+)
- **IDE**: IntelliJ IDEA, Eclipse, hoặc NetBeans (khuyến nghị IntelliJ IDEA)
- **Thư viện**: FlatLaf (đã được tích hợp trong code, sử dụng reflection)

### Các Bước Cài Đặt

1. **Clone hoặc tải dự án về máy**
   ```bash
   git clone <repository-url>
   cd Project_Java-main
   ```

2. **Mở dự án trong IDE**
   - Mở IntelliJ IDEA
   - File → Open → Chọn thư mục `Project_Java-main`
   - IDE sẽ tự động nhận diện cấu trúc dự án

3. **Cấu hình JDK**
   - File → Project Structure → Project
   - Chọn JDK version (8 trở lên)
   - Apply → OK

4. **Thêm thư viện FlatLaf (nếu cần)**
   - File → Project Structure → Libraries
   - Thêm JAR file FlatLaf (nếu IDE không tự động nhận diện)
   - Hoặc sử dụng Maven/Gradle để quản lý dependency

5. **Chạy ứng dụng**
   - Tìm file `App.java` trong `src/university/registration/`
   - Click chuột phải → Run 'App.main()'
   - Hoặc chạy từ terminal:
     ```bash
     javac -d out src/university/registration/**/*.java
     java -cp out university.registration.App
     ```

## 👤 Tài Khoản Demo

### Tài Khoản Admin (PĐT)
- **Username**: `pdt`
- **Password**: `pdt123`

### Tài Khoản Sinh Viên Demo
- **MSSV**: `SV001`
- **Email**: `sv001@university.edu`
- **Password**: `sv123`
- **Chương trình đào tạo**: Kỹ thuật Điện tử - Viễn thông 2021

## 📖 Hướng Dẫn Sử Dụng

### Đăng Nhập

1. **Đăng nhập Admin (PĐT)**
   - Nhập username: `pdt`
   - Nhập password: `pdt123`
   - Click "Đăng nhập"
   - Màn hình quản trị sẽ hiển thị

2. **Đăng nhập Sinh viên**
   - Nhập MSSV hoặc Email: `SV001` hoặc `sv001@university.edu`
   - Nhập password: `sv123`
   - Click "Đăng nhập"
   - Màn hình đăng ký học phần sẽ hiển thị

3. **Tạo tài khoản mới**
   - Click "Tạo tài khoản sinh viên"
   - Điền đầy đủ thông tin:
     - Mã số sinh viên (MSSV)
     - Họ tên đầy đủ
     - Ngày sinh (định dạng: YYYY-MM-DD)
     - Địa chỉ liên hệ
     - Email (phải là duy nhất)
     - Chương trình học
     - Mật khẩu và xác nhận mật khẩu
   - Click "Tạo tài khoản"
   - Đăng nhập ngay sau khi tạo thành công

### Sử Dụng Cho Sinh Viên

#### Đăng Ký Học Phần

1. **Chọn học kỳ**
   - Chọn học kỳ từ dropdown "HỌC KỲ"
   - Danh sách học phần sẽ tự động cập nhật

2. **Tìm kiếm học phần**
   - Nhập mã hoặc tên học phần vào ô "TÌM KIẾM HỌC PHẦN"
   - Kết quả được cập nhật real-time

3. **Thêm học phần vào giỏ**
   - Click nút "THÊM" ở cột "HÀNH ĐỘNG"
   - Học phần sẽ được thêm vào giỏ đăng ký (bên phải)
   - Tổng số tín chỉ được cập nhật tự động

4. **Xem giỏ đăng ký**
   - Giỏ đăng ký hiển thị ở bên phải màn hình
   - Xem tổng số tín chỉ: "X/24 TC"
   - Progress bar hiển thị tiến độ
   - Cảnh báo nếu < 12 TC hoặc > 24 TC

5. **Xóa học phần khỏi giỏ**
   - Click nút "x" bên cạnh học phần trong giỏ
   - Chỉ có thể xóa nếu chưa gửi hoặc trạng thái là "Tạm"

6. **Gửi đăng ký**
   - Kiểm tra tổng số tín chỉ (12-24 TC)
   - Click nút "Gửi đăng ký"
   - Đăng ký sẽ chuyển sang trạng thái "Đã gửi" và chờ PĐT duyệt

#### Xem Lịch Sử Đăng Ký

1. Click tab "Lịch sử đăng ký"
2. Xem tất cả đăng ký qua các học kỳ
3. Màu sắc phân biệt trạng thái:
   - 🟢 Xanh lá: Đã duyệt
   - 🔴 Đỏ: Đã từ chối
   - 🟡 Vàng: Chờ duyệt

### Sử Dụng Cho Admin (PĐT)

#### Quản Lý Học Phần Mở Trong Kỳ

1. **Xem danh sách học phần**
   - Chọn học kỳ từ dropdown
   - Xem danh sách học phần mở trong kỳ đó
   - Lọc theo trạng thái, tìm kiếm theo mã/tên

2. **Tạo học phần mới**
   - Click nút "+ Tạo học phần mới"
   - Điền thông tin: mã HP, tên, số tín chỉ, loại
   - Chọn học kỳ để mở lớp
   - Chọn CTĐT được phép đăng ký
   - Click "Lưu"

3. **Cập nhật Offering**
   - Click nút "Mở/Đóng" trong cột "Hành động"
   - Thay đổi trạng thái mở/đóng lớp
   - Cập nhật CTĐT được phép đăng ký

#### Duyệt Đăng Ký Học Phần

1. **Lọc đăng ký**
   - Chọn học kỳ, trạng thái, khoa/viện
   - Tìm kiếm theo tên/MSSV sinh viên, mã/tên học phần

2. **Duyệt đăng ký**
   - Click nút "✓" (Duyệt) trong cột "Hành động"
   - Đăng ký chuyển sang trạng thái "Đã duyệt"

3. **Từ chối đăng ký**
   - Click nút "✗" (Từ chối) trong cột "Hành động"
   - Nhập lý do từ chối
   - Đăng ký chuyển sang trạng thái "Đã từ chối"

4. **Duyệt/Từ chối hàng loạt**
   - Click nút "Duyệt tất cả" hoặc "Từ chối tất cả"
   - Xác nhận hành động
   - Tất cả đăng ký đang chờ duyệt sẽ được xử lý

#### Cài Đặt Học Phần

1. **Thêm học phần master**
   - Điền form: mã HP, tên, số tín chỉ, loại
   - Click "Thêm"
   - Học phần được thêm vào danh sách master

2. **Sửa học phần**
   - Chọn học phần trong bảng
   - Click "Sửa"
   - Cập nhật thông tin và lưu
   - **Lưu ý**: Chỉ sửa được nếu chưa có sinh viên đăng ký

3. **Xóa học phần**
   - Chọn học phần trong bảng
   - Click "Xóa"
   - Xác nhận xóa
   - **Lưu ý**: Chỉ xóa được nếu chưa có sinh viên đăng ký

#### Cài Đặt Kỳ Học

1. **Thêm học kỳ mới**
   - Click nút "Thêm học kỳ mới"
   - Điền thông tin: mã học kỳ, tên, năm học, ngày bắt đầu/kết thúc
   - Chọn trạng thái mở/đóng đăng ký
   - Click "Lưu"

2. **Mở/Đóng đăng ký**
   - Chọn học kỳ trong bảng
   - Click nút "Mở/Đóng" trong cột "Hành động"
   - Thay đổi trạng thái mở/đóng đăng ký

## 📊 Cấu Trúc Dữ Liệu

### Lưu Trữ Trong Memory

Hệ thống sử dụng lớp `Memory` để lưu trữ dữ liệu trong bộ nhớ:

- **adminPasswords**: Map<username, password> - Tài khoản admin
- **studentsById**: Map<MSSV, Student> - Danh sách sinh viên
- **emailIndex**: Map<email_lowercase, MSSV> - Index email để tra cứu
- **courses**: Map<courseCode, Course> - Danh sách học phần master
- **terms**: List<String> - Danh sách học kỳ
- **programs**: List<String> - Danh sách chương trình đào tạo
- **regs**: Map<MSSV, Map<Học kỳ, List<RegItem>>> - Đăng ký học phần
- **termSettings**: Map<Học kỳ, TermSetting> - Cấu hình học kỳ
- **offerings**: Map<Học kỳ, Map<courseCode, Offering>> - Cấu hình mở lớp

### Quy Tắc Nghiệp Vụ

1. **Đăng ký học phần**
   - Tối thiểu: 12 tín chỉ
   - Tối đa: 24 tín chỉ
   - Không được đăng ký trùng học phần trong cùng học kỳ
   - Học kỳ phải đang mở đăng ký
   - Học phần phải đang mở lớp (Offering.open = true)
   - CTĐT của sinh viên phải được phép đăng ký

2. **Xóa học phần**
   - Chỉ xóa được khi chưa có sinh viên nào đăng ký
   - Nếu đã có sinh viên đăng ký: không cho phép xóa

3. **Trạng thái đăng ký**
   - **Tạm**: Sinh viên đã thêm vào giỏ nhưng chưa gửi
   - **Đã gửi**: Sinh viên đã gửi, chờ PĐT duyệt
   - **Chờ duyệt**: Tương đương "Đã gửi" (chuẩn hóa)
   - **Đã duyệt**: PĐT đã duyệt, sinh viên được học
   - **Đã từ chối**: PĐT đã từ chối đăng ký

## 🎨 Giao Diện

- **Theme**: FlatLaf (FlatMacLightLaf) - theme sáng, hiện đại
- **Font**: Segoe UI (size 14-18px)
- **Màu sắc**: 
  - Primary: #3B82F6 (xanh dương)
  - Success: #10B981 (xanh lá)
  - Danger: #EF4444 (đỏ)
  - Warning: #F59E0B (vàng)
- **Bo góc**: 16-20px cho các component
- **Responsive**: Tự động điều chỉnh theo kích thước cửa sổ

## ⚠️ Lưu Ý Quan Trọng

### Dữ Liệu Chỉ Tồn Tại Trong RAM
- Hệ thống sử dụng in-memory storage (lớp `Memory`)
- **Tất cả dữ liệu sẽ bị mất khi tắt ứng dụng**
- Không có database, không có file lưu trữ
- Chỉ phù hợp cho demo, testing, hoặc học tập

### Bảo Mật
- Mật khẩu được lưu dạng plain text (không hash)
- **KHÔNG phù hợp cho môi trường production**
- Trong hệ thống thực tế, cần:
  - Hash mật khẩu (BCrypt, Argon2, v.v.)
  - Sử dụng database (MySQL, PostgreSQL, v.v.)
  - Mã hóa dữ liệu nhạy cảm
  - Xác thực 2 lớp (2FA)

### Mở Rộng
Để chuyển sang hệ thống production, cần:
1. Thay thế `Memory` bằng Database (JDBC, JPA/Hibernate)
2. Thêm hash mật khẩu
3. Thêm logging và error handling
4. Thêm unit tests và integration tests
5. Thêm backup và restore dữ liệu
6. Thêm API REST (nếu cần web/mobile app)

## 📝 Dữ Liệu Demo

Hệ thống được khởi tạo với dữ liệu demo:

- **3 học kỳ**: 20252, 20251, 20242
- **3 chương trình đào tạo**:
  - Kỹ thuật Điện tử - Viễn thông 2021
  - Công nghệ Thông tin 2021
  - Kỹ thuật Cơ khí 2021
- **Hơn 100 học phần** thuộc các loại:
  - GDTC (Giáo dục thể chất)
  - QP-AN (Quốc phòng An ninh)
  - Ngoại ngữ
  - Lý luận chính trị
  - Toán-KHCB
  - Cơ sở ngành
  - Mô đun chuyên ngành
- **1 sinh viên demo**: SV001
- **Tất cả học phần được mở lớp** cho học kỳ mới nhất (20252)

## 🐛 Xử Lý Lỗi

Hệ thống có xử lý lỗi cơ bản:
- Validate dữ liệu đầu vào
- Hiển thị thông báo lỗi bằng JOptionPane
- Kiểm tra điều kiện nghiệp vụ trước khi thực hiện

## 📚 Tài Liệu Tham Khảo

- **Java Swing Documentation**: https://docs.oracle.com/javase/tutorial/uiswing/
- **FlatLaf**: https://www.formdev.com/flatlaf/
- **MVC Pattern**: https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller

## 👥 Đóng Góp

Dự án này được phát triển cho mục đích học tập và demo. Mọi đóng góp đều được chào đón!

## 📄 License

Dự án này được phát triển cho mục đích giáo dục và học tập.

---

**Phát triển bởi**: Trường Đại Học An Giang  
**Phiên bản**: 1.0  
**Năm**: 2025

