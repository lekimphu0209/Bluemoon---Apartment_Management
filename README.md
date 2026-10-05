# BlueMoon - Hệ thống Quản lý Chung cư (Apartment Management)

## Tổng quan dự án

**BlueMoon** là hệ thống quản lý chung cư trên nền tảng web, được xây dựng cho đồ án môn học Công nghệ phần mềm (HUST). Hệ thống hỗ trợ ban quản lý tòa nhà vận hành các nghiệp vụ cốt lõi và cư dân theo dõi phí dịch vụ của mình.

### Chức năng chính

- **Xác thực & phân quyền** — đăng nhập/đăng ký bằng JWT, xác thực OTP qua email, phân quyền Admin/Resident
- **Quản lý căn hộ** — CRUD thông tin căn hộ, tình trạng sở hữu, gán cư dân vào căn hộ
- **Quản lý cư dân** — hồ sơ cư dân, tạm trú/tạm vắng, liên kết căn hộ
- **Quản lý hóa đơn & phí** — tạo hóa đơn theo căn hộ/loại phí, theo dõi trạng thái thanh toán, nhắc hạn tự động qua email
- **Khoản đóng góp** — quản lý các loại đóng góp (quỹ từ thiện, sự kiện) và lượt đóng góp của cư dân
- **Thuê chỗ để xe** — đăng ký/quản lý chỗ để xe của cư dân
- **Thông báo** — gửi thông báo tới cư dân
- **Thanh toán trực tuyến** — tích hợp webhook SePay để đối soát thanh toán tự động
- **Trang cá nhân** — cư dân xem/chỉnh sửa thông tin, lịch sử hóa đơn

### Kiến trúc hệ thống

- **Backend**: Spring Boot REST API, kiến trúc phân lớp (controller → service → repository → entity), bảo mật Spring Security + JWT, JPA/Hibernate ORM
- **Frontend**: React 18 SPA, Chakra UI, React Router, Axios với JWT interceptor
- **Database**: MySQL 8 trên Docker, schema tự sinh bởi JPA
- **Triển khai**: Docker Compose orchestrate 3 services (MySQL, Backend, Frontend/Nginx), cấu hình tập trung qua file `.env` ở root

## Công nghệ đang sử dụng

### Backend
| Công nghệ | Phiên bản | Vai trò |
|-----------|-----------|---------|
| Java | 17 | Ngôn ngữ chính |
| Spring Boot | 3.4.3 | Framework REST API |
| Maven | 3.9.x | Quản lý dependency & build |
| Spring Security + JJWT | 0.12.3 | Xác thực JWT, phân quyền |
| Spring Data JPA / Hibernate | (theo Spring Boot) | ORM, tự sinh schema |
| Spring Mail | (theo Spring Boot) | Gửi OTP / nhắc hạn qua email |
| Lombok | (theo Spring Boot) | Giảm boilerplate code |
| Thymeleaf | (theo Spring Boot) | Render template (email, trang chào) |
| iTextPDF | 5.5.13.3 | Xuất hóa đơn PDF |
| Firebase Admin | 9.4.3 | Push notification |

### Frontend
| Công nghệ | Phiên bản | Vai trò |
|-----------|-----------|---------|
| React | 18 | SPA framework |
| Node.js | 18 | Runtime build frontend |
| npm | 9+ | Package manager |
| Chakra UI | (theo package.json) | UI component library |
| React Router | (theo package.json) | Điều hướng + route guard |
| Axios | (theo package.json) | HTTP client, JWT interceptor |

### Database & DevOps
| Công nghệ | Vai trò |
|-----------|---------|
| MySQL 8 | Database chính, chạy trên Docker |
| Docker + Docker Compose | Container hóa & orchestrate 3 services |
| Nginx | Serve frontend build + reverse proxy `/api` → backend |
| Git + GitHub | Version control, Git flow (`main`/`develop`/`feature/*`) |

## Cách cài đặt & chạy hệ thống

### Cách 1: Chạy bằng Docker (khuyến nghị)

Chỉ cần cài **Docker Desktop** (đã có sẵn docker compose).

1. Clone repo và tạo file cấu hình môi trường từ template:

<pre>
git clone https://github.com/lekimphu0209/Bluemoon---Apartment_Management.git
cd Bluemoon---Apartment_Management
copy .env.example .env
</pre>

Mở file `.env` và điền giá trị thật (mật khẩu DB, JWT secret, tài khoản admin...). File `.env` chứa secrets nên **không được commit** lên git.

2. Build và chạy toàn bộ hệ thống (MySQL + Backend + Frontend):

<pre>
docker compose up -d --build
</pre>

3. Kiểm tra trạng thái:

<pre>
docker ps
</pre>

Cả 3 container `mysql` (healthy), `backend`, `frontend` đều phải `Up`.

4. Truy cập hệ thống:

| Thành phần | Địa chỉ |
|------------|---------|
| Frontend | http://localhost |
| Backend API | http://localhost:9090 |
| MySQL | localhost:3306 (user: root, password: xem trong `.env`) |

Tài khoản admin mặc định được backend tự tạo lúc khởi động, cấu hình trong `.env`:

<pre>
ADMIN_USERNAME=admin
ADMIN_PASSWORD=1234567890
</pre>

### Cách 2: Chạy local để develop

1. **MySQL**: chỉ chạy riêng mysql bằng docker:

<pre>
docker compose up -d mysql
</pre>

2. **Backend** (yêu cầu Java 17 + Maven, hoặc dùng `mvnw` có sẵn):

<pre>
cd Backend
./mvnw clean install
./mvnw spring-boot:run
</pre>

Backend chạy ở `http://localhost:9090`. Để dùng tính năng OTP qua email, điền `spring.mail.username` và `spring.mail.password` (app password của Gmail: https://myaccount.google.com/apppasswords) trong `Backend/src/main/resources/application.properties`.

3. **Frontend** (yêu cầu Node.js 18 + npm):

<pre>
cd FrontEnd
npm install
npm start
</pre>

Frontend dev chạy ở `http://localhost:3000`. URL backend được cấu hình qua `REACT_APP_API_BASE_URL` trong `FrontEnd/.env` (local) hoặc build arg từ `.env` root (Docker).

## Liên hệ
Cảm ơn các bạn đã đọc 😄🌞😊🙏. 

Project mẫu còn nhiều hạn chế, mong các bạn cố gắng cải thiện để có được dự án tốt nhất, đạt được điểm cao. 

Nếu gặp vấn đề khi khởi chạy hệ thống, các bạn liên hệ qua email: tung.nguyenson@hust.edu.vn hoặc tungns@soict.hust.edu.vn


## Lời cảm ơn
Xin được gửi lời cảm ơn trân trọng nhất tới các bạn sinh viên xuất sắc sau đây đã góp phần giúp cải thiện phần bài tập môn học rất nhiều:

Nguyễn Bá Nhật Hoàng 20230030

Nguyễn Tấn Dũng 20230021

Sái Văn Hiếu 20230027

Trần Quốc Thái 20230065


