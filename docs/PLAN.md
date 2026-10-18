# KẾ HOẠCH THỰC HIỆN DỰ ÁN BLUEMOON

> Tài liệu này được lập cho toàn bộ thành viên nhóm, giải thích rõ nhiệm vụ, công nghệ sử dụng, lý do lựa chọn và sự khác biệt so với công nghệ hiện tại.

---

## 1. TỔNG QUAN

**Tên dự án:** Phần mềm quản lý thu phí chung cư BlueMoon
**Môn học:** Nhập môn Công nghệ phần mềm
**Ngôn ngữ chính:** Java (Spring Boot) + ReactJS + MySQL
**Môi trường triển khai:** Local development, hướng tới Docker production

**Mục tiêu:**
- Hoàn thiện yêu cầu v1.0 (quản lý căn hộ, cư dân, hóa đơn, thống kê)
- Bổ sung yêu cầu v2.0 (phí gửi xe, điện, nước, internet)
- Cải thiện chất lượng code, bảo mật, kiến trúc database
- Triển khai bằng Docker

---

## 2. CÔNG NGHỆ HIỆN TẠI

| Thành phần | Công nghệ hiện tại |
|---|---|
| Backend | Spring Boot 3.4.3, Java 17, Maven |
| Frontend | React 18, Chakra UI, Horizon UI Template |
| Database | MySQL 8.0 |
| Auth | Spring Security, JWT |
| ORM | JPA / Hibernate |
| Build | Maven (backend), npm (frontend) |
| Containerization | Docker Desktop, Docker Compose (cơ bản) |

---

## 3. PHÂN TÍCH VẤN ĐỀ HIỆN TẠI

### 3.1. Backend
- `BillService` quá dài (576 dòng), vi phạm SRP
- Database dùng `@ElementCollection` thay vì quan hệ JPA chuẩn
- JWT secret hardcoded, CORS mở rộng
- Thiếu test, thiếu logging
- Tính toán phí chưa tự động theo yêu cầu bài toán

### 3.2. Frontend
- Giải mã JWT bằng `atob` không an toàn
- Lưu token trong `localStorage` rủi ro XSS
- ESLint cấu hình chưa chuẩn
- Chưa có `.dockerignore`

### 3.3. Database
- Thiếu migration tool
- Thiếu quan hệ chuẩn hóa giữa `Apartment` - `Resident` - `Bill`
- Không có index tối ưu

---

## 4. PHÂN CÔNG NHIỆM VỤ

### Sprint 1: Ổn định nền tảng (Tuần 1)

| STT | Nhiệm vụ | Người phụ trách | Công nghệ | Mục tiêu |
|---|---|---|---|---|
| 1.1 | Khởi tạo Git repo, branching strategy | DevOps Lead | Git, Git Flow | Quản lý version, tránh xung đột code |
| 1.2 | Cấu hình `.gitignore` hoàn chỉnh | DevOps Lead | Git | Loại bỏ file build, node_modules khỏi repo |
| 1.3 | Chuẩn hóa database schema | Backend Lead | JPA, Flyway/Liquibase | Chuyển collection thành quan hệ `@ManyToMany`, `@OneToMany` |
| 1.4 | Setup migration database | Backend Lead | Flyway | Kiểm soát thay đổi schema có lịch sử rõ ràng |
| 1.5 | Tách `BillService` thành các service nhỏ | Backend Dev | Spring | Giảm độ phức tạp, dễ bảo trì |
| 1.6 | Cấu hình ESLint + Prettier | Frontend Lead | ESLint, Prettier | Đảm bảo code style thống nhất |
| 1.7 | Viết file `PLAN.md`, `README.md` | PM/Lead | Markdown | Tài liệu hóa kế hoạch |

### Sprint 2: Hoàn thiện v1.0 (Tuần 2-3)

| STT | Nhiệm vụ | Người phụ trách | Công nghệ | Mục tiêu |
|---|---|---|---|---|
| 2.1 | Tính phí dịch vụ theo diện tích tự động | Backend Dev | Spring, JPA | Phí dịch vụ = diện tích × đơn giá/m² |
| 2.2 | Tính phí quản lý theo diện tích tự động | Backend Dev | Spring, JPA | Phí quản lý = diện tích × 7.000đ/m² |
| 2.3 | Quản lý đóng góp tự nguyện | Backend Dev | Spring, JPA | Có đợt thu, tình nguyện, không bắt buộc |
| 2.4 | Thêm tính năng tạm trú/tạm vắng | Backend + Frontend | Spring, React | Theo dõi biến động nhân khẩu |
| 2.5 | Thống kê, báo cáo chi tiết | Backend + Frontend | Spring, Chart.js/Recharts | Dashboard có biểu đồ, báo cáo |
| 2.6 | Cải thiện bảo mật JWT, CORS, localStorage | Fullstack | Spring, React | Giảm rủi ro bảo mật |

### Sprint 3: Bổ sung v2.0 (Tuần 4)

| STT | Nhiệm vụ | Người phụ trách | Công nghệ | Mục tiêu |
|---|---|---|---|---|
| 3.1 | Phí gửi xe máy (70.000đ/tháng) | Backend + Frontend | Spring, React | Tính theo số lượng xe đăng ký |
| 3.2 | Phí gửi ô tô (1.200.000đ/tháng) | Backend + Frontend | Spring, React | Tính theo số lượng xe đăng ký |
| 3.3 | Phí điện (tính theo kWh) | Backend + Frontend | Spring, React | Thu hộ theo chỉ số công tơ |
| 3.4 | Phí nước (tính theo m³) | Backend + Frontend | Spring, React | Thu hộ theo chỉ số công tơ |
| 3.5 | Phí internet | Backend + Frontend | Spring, React | Thu hộ từ nhà cung cấp |

### Sprint 4: Containerization & Testing (Tuần 5)

| STT | Nhiệm vụ | Người phụ trách | Công nghệ | Mục tiêu |
|---|---|---|---|---|
| 4.1 | Hoàn thiện Docker, Docker Compose | DevOps Lead | Docker, Docker Compose | Chạy toàn bộ hệ thống bằng 1 lệnh |
| 4.2 | Thêm `.dockerignore` | DevOps Lead | Docker | Giảm kích thước image |
| 4.3 | Viết unit test backend | Backend Dev | JUnit 5, Mockito | Đảm bảo business logic đúng |
| 4.4 | Viết test frontend cơ bản | Frontend Dev | Jest, React Testing Library | Kiểm tra component chính |
| 4.5 | Tối ưu performance, index DB | Backend Lead | JPA, MySQL | Truy vấn nhanh hơn |
| 4.6 | Fix lỗi, review code toàn bộ | Cả nhóm | Git, PR | Đảm bảo chất lượng trước nộp |

---

## 5. CÔNG NGHỆ ĐỀ XUẤT VÀ LÝ DO CHỌN

### 5.1. Quản lý Database Migration: Flyway

**Hiện tại:** `spring.jpa.hibernate.ddl-auto=update`

**Vấn đề hiện tại:**
- Auto update tự động sửa schema, dễ mất dữ liệu khi thay đổi lớn
- Không có lịch sử version schema
- Khó teamwork vì không biết ai thay đổi gì

**Đề xuất:** Flyway

**Lý do chọn:**
- Quản lý schema bằng file SQL có version rõ ràng
- Hỗ trợ rollback, phù hợp teamwork
- Tích hợp tốt với Spring Boot

**Khác biệt so với hiện tại:**
| Tiêu chí | `ddl-auto=update` | Flyway |
|---|---|---|
| Kiểm soát schema | Tự động, mất kiểm soát | Rõ ràng, có version |
| Lịch sử thay đổi | Không có | Có file V1, V2, V3... |
| An toàn dữ liệu | Rủi ro cao | Rủi ro thấp, review trước khi chạy |
| Teamwork | Khó | Dễ dàng merge schema |

### 5.2. ORM Mapping chuẩn: JPA `@ManyToMany`, `@OneToMany`

**Hiện tại:** `@ElementCollection` lưu `Set<Long>`

**Vấn đề hiện tại:**
- Không có khóa ngoại thực sự
- Khó truy vấn ngược
- Dễ mất tính toàn vẹn dữ liệu

**Đề xuất:** Quan hệ JPA chuẩn

```java
// Apartment - Resident: Many-to-Many
@ManyToMany
@JoinTable(name = "apartment_residents",
    joinColumns = @JoinColumn(name = "apartment_id"),
    inverseJoinColumns = @JoinColumn(name = "resident_id"))
private Set<Resident> residents;

// Apartment - Bill: One-to-Many
@OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
private List<Bill> bills;
```

**Khác biệt:**
| Tiêu chí | `@ElementCollection` | Quan hệ JPA chuẩn |
|---|---|---|
| Toàn vẹn dữ liệu | Kém | Có khóa ngoại, cascade |
| Truy vấn ngược | Khó | Dễ dàng |
| Join | Phải tự làm | Tự động với JPQL |
| Maintainability | Kém | Tốt |

### 5.3. Tách Service: BillCalculationService, BillPaymentService, BillNotificationService

**Hiện tại:** `BillService` 576 dòng

**Vấn đề hiện tại:**
- Quá nhiều trách nhiệm trong 1 class
- Khó đọc, khó test, khó bảo trì

**Đề xuất:** Tách theo Single Responsibility Principle

**Khác biệt:**
| Tiêu chí | `BillService` đơn nhất | Tách service |
|---|---|---|
| Số dòng | 576 | Mỗi class < 200 |
| Testability | Khó | Dễ test từng phần |
| Maintainability | Kém | Tốt |
| Teamwork | Xung đột code cao | Ít xung đột hơn |

### 5.4. Bảo mật JWT: JJWT + Environment Variables

**Hiện tại:** JWT secret hardcoded trong `application.properties`

**Vấn đề hiện tại:**
- Lộ secret trong repo
- Expiration quá dài (~41 ngày)
- Lưu token trong `localStorage`

**Đề xuất:**
- Dùng JJWT hoặc `io.jsonwebtoken` mới nhất
- JWT secret từ biến môi trường
- Expiration 1-24 giờ
- Refresh token nếu cần
- Frontend lưu token trong `httpOnly` cookie hoặc `sessionStorage` (ít rủi ro hơn localStorage)

**Khác biệt:**
| Tiêu chí | Hiện tại | Đề xuất |
|---|---|---|
| Secret | Hardcoded | Environment variable |
| Expiration | 41 ngày | 1-24 giờ |
| Storage | localStorage | httpOnly cookie / sessionStorage |
| Bảo mật | Yếu | Tốt hơn |

### 5.5. State Management Frontend: Zustand hoặc Redux Toolkit

**Hiện tại:** Local state + localStorage

**Vấn đề hiện tại:**
- State rải rác các component
- Khó chia sẻ dữ liệu toàn app
- Khó debug

**Đề xuất:** Zustand (khuyên dùng) hoặc Redux Toolkit

**Lý do chọn Zustand:**
- Nhẹ, dễ học, không cần boilerplate nhiều
- Phù hợp dự án vừa và nhỏ
- Hỗ trợ middleware persistence, devtools

**Khác biệt:**
| Tiêu chí | Local State | Zustand |
|---|---|---|
| Kích thước | Nhẹ | Rất nhẹ (1KB) |
| Boilerplate | Ít | Ít |
| Global state | Khó | Dễ |
| Debug | Khó | Có devtools |
| Học curve | Thấp | Thấp |

**So sánh với Redux Toolkit:**
- Zustand: nhẹ hơn, ít boilerplate hơn
- Redux Toolkit: mạnh hơn cho app lớn, có RTK Query

### 5.6. Thư viện biểu đồ: Recharts

**Hiện tại:** Chưa rõ thư viện biểu đồ

**Đề xuất:** Recharts

**Lý do chọn:**
- Dễ tích hợp React
- Lightweight
- Hỗ trợ Bar, Line, Pie, Area chart

### 5.7. Testing Backend: JUnit 5 + Mockito + Testcontainers

**Hiện tại:** Không có test

**Đề xuất:**
- Unit test: JUnit 5 + Mockito
- Integration test: Testcontainers (MySQL)

**Lý do:**
- Testcontainers cho phép test với MySQL thật trong Docker container
- Đảm bảo tính đúng đắn khi có database

**Khác biệt:**
| Tiêu chí | Hiện tại | Đề xuất |
|---|---|---|
| Test | Không có | Unit + Integration |
| Database test | Không | Testcontainers MySQL |
| Confidence | Thấp | Cao |

### 5.8. Docker Multi-stage Build

**Hiện tại:** Dockerfile backend dùng `eclipse-temurin:17`, frontend dùng nginx

**Vấn đề hiện tại:**
- Chưa có `.dockerignore`
- Build context lớn (node_modules)
- Maven build tải dependencies mỗi lần build

**Đề xuất:**
- Thêm `.dockerignore` cho Backend và Frontend
- Dùng Docker build cache cho Maven và npm
- Frontend: `nginx:alpine` + `node:18` multi-stage
- Backend: Maven multi-stage

**Khác biệt:**
| Tiêu chí | Hiện tại | Đề xuất |
|---|---|---|
| Build time | Lâu | Nhanh hơn nhờ cache |
| Image size | Lớn | Nhỏ hơn |
| Reproducibility | Kém | Tốt |

### 5.9. Logging: SLF4J/Logback + AOP

**Hiện tại:** `System.out.println`

**Đề xuất:** SLF4J + Logback

**Lý do:**
- Log chuyên nghiệp, có level (INFO, DEBUG, ERROR)
- Dễ cấu hình output file/console
- Có thể tích hợp monitoring

---

## 6. LỘ TRÌNH THỰC HIỆN

```
Tuần 1: Ổn định nền tảng
  - Git repo + .gitignore
  - Database migration (Flyway)
  - Refactor entity relationships
  - Tách BillService
  - ESLint/Prettier

Tuần 2-3: Hoàn thiện v1.0
  - Tính phí dịch vụ/quản lý tự động
  - Quản lý đóng góp
  - Tạm trú/tạm vắng
  - Thống kê/báo cáo
  - Bảo mật JWT/CORS

Tuần 4: Bổ sung v2.0
  - Phí gửi xe máy/ô tô
  - Phí điện/nước/internet

Tuần 5: Docker + Testing + Tối ưu
  - Docker hoàn chỉnh
  - Unit/Integration test
  - Performance tuning
  - Review + Merge + Nộp
```

---

## 7. QUY TRÌNH LÀM VIỆC VỚI GIT

### 7.1. Branching Strategy (Git Flow đơn giản)

- `main`: Production-ready, chỉ merge từ `develop` khi hoàn thành
- `develop`: Tích hợp tính năng
- `feature/ten-tinh-nang`: Mỗi tính năng 1 branch
- `bugfix/ten-loi`: Sửa lỗi
- `hotfix/...`: Sửa lỗi khẩn cấp trên main

### 7.2. Commit Message Convention

```
feat: thêm tính năng tính phí dịch vụ
fix: sửa lỗi tính diện tích căn hộ
refactor: tách BillService thành 3 service
chore: cập nhật .gitignore
docs: cập nhật README
```

### 7.3. Pull Request Checklist

- [ ] Code đã chạy thành công local
- [ ] Không có lỗi ESLint/Prettier
- [ ] Có test nếu là tính năng phức tạp
- [ ] Không hardcode secret
- [ ] Đã review bởi ít nhất 1 thành viên

---

## 8. KẾT LUẬN

Tài liệu này là kim chỉ nam cho nhóm trong 5 tuần tới. Mỗi công nghệ đều có lý do lựa chọn rõ ràng, nhằm khắc phục điểm yếu hiện tại và nâng cao chất lượng phần mềm. Thành viên cần tuân thủ quy trình Git, chủ động báo cáo tiến độ hàng ngày, và review code kỹ lưỡng trước khi merge.

**Mục tiêu cuối cùng:** Hoàn thành v1.0 + v2.0, có Docker, có test, đạt 8-9/10 khi nộp bài.
