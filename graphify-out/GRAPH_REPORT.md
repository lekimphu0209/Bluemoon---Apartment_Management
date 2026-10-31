# Graph Report - bluemoon  (2026-11-01)

## Corpus Check
- 387 files · ~536,132 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 111 file(s) not represented in the graph (top: .css 36, .woff 25, .woff2 23)

## Summary
- 2763 nodes · 6089 edges · 209 communities (134 shown, 75 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 340 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `a47a9d6c`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- @chakra-ui/react
- Frontend Auth & Layout
- Admin Dashboard Components
- Admin Data Tables
- Admin User Management
- Residency Event Controllers
- Backend Controllers
- Apartment Frontend Components
- Apartment
- Docker Configuration
- dependencies
- UI Component Internals
- Minified/Compiled Code
- Carousel Component Logic
- Admin Apartment Controllers
- Fee Record Controllers
- Icon Components
- UI Menu & Calendar Components
- Fee Record Entity
- Validation Annotations
- Modal Component Logic
- Admin User List Controllers
- Bill Entity & Repository
- Apartment Spec Controllers
- Apartment Fee Unit Entities
- Bill Frontend Components
- Service Layer Classes
- Minified/Compiled Code
- Frontend Dependencies
- Chakra UI Theme Components
- User Apartment Data Tables
- User Profile & Modals
- DTO & Entity Classes
- Vehicle Entity
- API Exception Handling
- Frontend Package Config
- Auth Pages & Components
- Minified/Compiled Code
- org.springframework.stereotype.Component
- ResidentStatus
- Admin & User View Routes
- Notification Entity & Service
- User DTO Classes
- react
- Forgot Password Controllers
- Validation Annotations
- JWT Authentication Filter
- Frontend Route Config
- Account Controllers
- Admin Bill Controllers
- Sidebar Component Logic
- Chart Components
- HomeSecurity.java
- Bill Type Enums
- Security Configuration
- Fee Type Enums
- 5. CÔNG NGHỆ ĐỀ XUẤT VÀ LÝ DO CHỌN
- Tab Component Logic
- Collapse Component Logic
- Scrollspy Component Logic
- Notification Controllers
- NavbarLinksAdmin.js
- Bill API Controllers
- User & Notification Services
- Change Password Controllers
- Scrollbar Component Logic
- ApiException
- Sidebar Toggle Logic
- Calendar & Homepage Components
- Admin Bill Data Tables
- MySQL Docker Config
- Apartment API Controllers
- OTP Service
- Excel Export Utility
- Popover Component Logic
- Toast/Alert Component Logic
- Maven Wrapper Script
- Resident Controllers
- Focus Trap Logic
- Bill DTO Classes
- package.json
- Apartment Exists Validator
- RTL Provider Config
- Dev Dependencies
- mockApi.js
- VehicleService
- JWT Service
- NPM Scripts
- PWA Manifest
- Profile Project Components
- Payment Column Data
- Relationship Enum
- Apartment Column Data
- Admin Column Data Variables
- User Column Data
- User Column Data Variables
- Spring Security Auth Controller
- Email OTP Mail Service
- Excel Export Utility
- Annotation Definitions
- Spring Boot Application Entry
- DataTables Pagination Adapter
- Spring Boot Test Context
- React Pie Chart Component
- User Role Enum
- Dark Theme Initialization
- Dashboard Chart Options
- jQuery DataTables Config
- JSConfig Path Aliases
- Apartment Not Found Exception
- CSS Grid Size Variables
- Toast Notification Components
- Horizontal Layout Responsive
- SweetAlert2 Toast Wrapper
- App Main Stylesheet
- Email Template Stylesheet
- Iconly Bold Font EOT
- Iconly Bold Font SVG
- Iconly Bold Font TTF
- Iconly Bold Font WOFF
- Nunito Font 300 WOFF
- Nunito Font 300 WOFF2
- Nunito Font 400 WOFF2
- Nunito Font 600 WOFF
- Nunito Font 600 WOFF2
- Nunito Font 700 WOFF
- Nunito Font 700 WOFF2
- Nunito Font 800 WOFF
- Nunito Font 800 WOFF2
- Background Image Asset
- Avatar Face Image 1
- Avatar Face Image 2
- Avatar Face Image 3
- Avatar Face Image 4
- Avatar Face Image 5
- Avatar Face Image 6
- Avatar Face Image 7
- Avatar Face Image 8
- Favicon Logo SVG
- Main Logo SVG
- Sample Image 1
- Sample Image 2
- Sample Image 3
- Sample Image 4
- Architecture Sample Image
- Banana Sample Image
- Mountain Background Image
- Building Sample Image
- Error 403 Illustration
- Error 404 Illustration
- Error 500 Illustration
- Jump Sample Image
- Motorcycle Sample Image
- Origami Sample Image
- Water Sample Image
- Audio SVG Loader
- Circles SVG Loader
- Grid SVG Loader
- Hearts SVG Loader
- Oval SVG Loader
- Puff SVG Loader
- Rings SVG Loader
- Auth Page Stylesheet
- Error Page Stylesheet
- Summernote Editor Stylesheet
- Iconly Icon Stylesheet
- DataTable Stylesheet
- Testing Library React
- BlueMoon - Hệ thống Quản lý Chung cư (Apartment Management)
- ApartmentService
- ResidentDTO
- AdminListUserController
- user/dataTables/components/ColumnsTable.js
- ResidentController
- ApartmentDTO
- Relationship
- ManualNotification
- user/ColumnsData.js
- PaymentStatus
- PieChart
- browserslist
- CLAUDE.md
- react-toastify@^11.0.5
- @testing-library/react@^16.0.0
- web-vitals@^4.2.2

## God Nodes (most connected - your core abstractions)
1. `react` - 182 edges
2. `@chakra-ui/react` - 168 edges
3. `Resident` - 85 edges
4. `ResidentRepository` - 72 edges
5. `Apartment` - 62 edges
6. `Bill` - 55 edges
7. `ke` - 53 edges
8. `User` - 50 edges
9. `ApartmentRepository` - 46 edges
10. `he` - 40 edges

## Surprising Connections (you probably didn't know these)
- `3.1. Backend` --references--> `BillService`  [INFERRED]
  docs/PLAN.md → Backend/src/main/java/com/qlcc/service/BillService.java
- `5.3. Tách Service: BillCalculationService, BillPaymentService, BillNotificationService` --references--> `BillService`  [INFERRED]
  docs/PLAN.md → Backend/src/main/java/com/qlcc/service/BillService.java
- `Sprint 1: Ổn định nền tảng (Tuần 1)` --references--> `BillService`  [INFERRED]
  docs/PLAN.md → Backend/src/main/java/com/qlcc/service/BillService.java
- `3.3. Database` --references--> `Apartment`  [INFERRED]
  docs/PLAN.md → Backend/src/main/java/com/qlcc/entity/Apartment.java
- `3.3. Database` --references--> `Bill`  [INFERRED]
  docs/PLAN.md → Backend/src/main/java/com/qlcc/entity/Bill.java

## Import Cycles
- None detected.

## Communities (209 total, 75 thin omitted)

### Community 0 - "@chakra-ui/react"
Cohesion: 0.09
Nodes (11): AppartmentModal(), ApartmentList(), BillListModal(), ChangePassModal(), GeneralInformation(), UserListModal(), Overview(), ChangePassModal() (+3 more)

### Community 1 - "Frontend Auth & Layout"
Cohesion: 0.11
Nodes (3): Resident, ResidentRepository, ResidentService

### Community 2 - "Admin Dashboard Components"
Cohesion: 0.05
Nodes (45): BlueMoon FrontEnd, Build Docker, Chạy local, Cấu trúc chính, Thêm trang mới (theo thiết kế), Tài khoản test, Main(), AuthGuard() (+37 more)

### Community 3 - "Admin Data Tables"
Cohesion: 0.10
Nodes (8): UserCreationRequest, ApartmentExists, ApartmentsExists, ResidentBelongsToApartment, UniqueEmail, UniqueName, UniqueNumber, ValidPassword

### Community 4 - "Admin User Management"
Cohesion: 0.05
Nodes (26): columnHelper, columnHelper, columnHelper, columnHelper, columnHelper, columnHelper, columnHelper, columnHelper (+18 more)

### Community 5 - "Residency Event Controllers"
Cohesion: 0.10
Nodes (10): FeeRecordController, StatisticsController, AmountRequest, CollectBulkRequest, CollectRequest, ContributionRequest, GenerateRequest, ResidentUpdateRequest (+2 more)

### Community 6 - "Backend Controllers"
Cohesion: 0.07
Nodes (8): AdminUserInitializer, User, UserRepository, AccountService, CurrentUserService, CustomUserDetailsService, NotificationService, UserService

### Community 7 - "Apartment Frontend Components"
Cohesion: 0.09
Nodes (4): RegisterController, BulkIdsRequest, UserDTO, ApiExceptionHandler

### Community 8 - "Apartment"
Cohesion: 0.11
Nodes (3): AdminListApartmentController, Apartment, ApartmentRepository

### Community 9 - "Docker Configuration"
Cohesion: 0.05
Nodes (22): FeeTypeController, FeeTypeRequest, FeeType, FeeCalcMethod, MANUAL_PER_APARTMENT, PER_AREA, PER_VEHICLE, VOLUNTARY (+14 more)

### Community 10 - "dependencies"
Cohesion: 0.07
Nodes (27): dependencies, axios@^1.8.4, @chakra-ui/icons@^2.0.19, @chakra-ui/react@2.6.1, @chakra-ui/system@2.5.7, @emotion/styled@^11.12.0, jwt-decode@^4.0.0, react@^18.3.1 (+19 more)

### Community 12 - "UI Component Internals"
Cohesion: 0.08
Nodes (11): ResidencyEventController, ResidencyEventRequest, ResidencyEvent, ResidencyEventType, INFO_CHANGE, MOVE_IN, MOVE_OUT, TEMP_ABSENCE (+3 more)

### Community 13 - "Minified/Compiled Code"
Cohesion: 0.04
Nodes (47): driver, REACT_APP_API_BASE_URL, build, container_name, depends_on, environment, networks, ports (+39 more)

### Community 15 - "Admin Apartment Controllers"
Cohesion: 0.07
Nodes (27): Cr(), cs(), De(), ds(), Ee(), fs(), ge(), getDataAttribute() (+19 more)

### Community 18 - "UI Menu & Calendar Components"
Cohesion: 0.09
Nodes (3): AdminController, ChangePasswordRequest, UserController

### Community 19 - "Fee Record Entity"
Cohesion: 0.05
Nodes (36): AdobexdLogo, AndroidLogo, AppleLogo, AtlassianLogo, BitcoinLogo, ButtonLeft, ButtonRight, CartIcon (+28 more)

### Community 22 - "Admin User List Controllers"
Cohesion: 0.16
Nodes (15): UserFilterModal(), ActionsCell(), columnHelper, columnsIcon(), Settings(), NotificationModal(), UserModal(), activateAccount() (+7 more)

### Community 23 - "Bill Entity & Repository"
Cohesion: 0.13
Nodes (7): Vehicle, VehicleStatus, ACTIVE, INACTIVE, VehicleType, CAR, MOTORBIKE

### Community 24 - "Apartment Spec Controllers"
Cohesion: 0.10
Nodes (27): apartment_bills, apartment_fee_units, apartment_residents, apartments, bills, manual_notification_residents, manual_notifications, notifications (+19 more)

### Community 25 - "Apartment Fee Unit Entities"
Cohesion: 0.12
Nodes (3): SpecResidentController, ResidentRequest, SpecResidentService

### Community 26 - "Bill Frontend Components"
Cohesion: 0.14
Nodes (5): ApartmentFeeUnit, ApartmentFeeUnitRepository, ManualNotificationRepository, VehicleRepository, ApartmentFeeUnitService

### Community 27 - "Service Layer Classes"
Cohesion: 0.10
Nodes (3): gt, Ka(), Ua

### Community 29 - "Frontend Dependencies"
Cohesion: 0.13
Nodes (3): SpecApartmentController, ApartmentRequest, SpecApartmentService

### Community 30 - "Chakra UI Theme Components"
Cohesion: 0.09
Nodes (6): DashboardController, FeeRecord, BillStatus, PAID, UNPAID, FeeRecordRepository

### Community 31 - "User Apartment Data Tables"
Cohesion: 0.12
Nodes (27): Ai(), An(), as(), at(), bi(), ct(), En(), es() (+19 more)

### Community 32 - "User Profile & Modals"
Cohesion: 0.14
Nodes (20): AppartmentModal(), activateAccount(), cache, columnHelper, ColumnTable(), generateColumns(), toggleStatus(), ApartmentFilterModal() (+12 more)

### Community 33 - "DTO & Entity Classes"
Cohesion: 0.16
Nodes (3): AdminNotificationController, NotificationRequestDTO, ManualNotificationService

### Community 34 - "Vehicle Entity"
Cohesion: 0.14
Nodes (14): @chakra-ui/theme-tools@^1.3.6, Card, CardComponent, badgeStyles, buttonStyles, inputStyles, linkStyles, progressStyles (+6 more)

### Community 35 - "API Exception Handling"
Cohesion: 0.26
Nodes (10): ActionsCell(), columnHelper, columnsDataIcon(), AppartmentModal(), activateAccount(), columnHelper, ColumnTable(), generateColumns() (+2 more)

### Community 36 - "Frontend Package Config"
Cohesion: 0.09
Nodes (15): BillModal(), activateAccount(), columnHelper, ColumnTable(), generateColumns(), toggleStatus(), BillFilterModal(), ActionsCell() (+7 more)

### Community 38 - "Minified/Compiled Code"
Cohesion: 0.12
Nodes (5): react-apexcharts@1.4.1, ColumnChart, LineChart, LineChart, react-apexcharts

### Community 39 - "org.springframework.stereotype.Component"
Cohesion: 0.09
Nodes (6): ApartmentExistsValidator, ApartmentsExistsValidator, ResidentBelongsToApartmentValidator, UniqueEmailValidator, UniqueNameValidator, ValidPasswordValidator

### Community 40 - "ResidentStatus"
Cohesion: 0.10
Nodes (8): ApartmentStatus, EMPTY, OCCUPIED, ResidentStatus, LIVING, MOVED_OUT, TEMP_ABSENCE, TEMP_RESIDENCE

### Community 41 - "Admin & User View Routes"
Cohesion: 0.20
Nodes (19): Ae(), bn(), bt(), ce(), Ei(), fi(), Fo(), gs() (+11 more)

### Community 44 - "User DTO Classes"
Cohesion: 0.15
Nodes (3): isTokenExpired(), SignIn(), jwt-decode

### Community 45 - "react"
Cohesion: 0.06
Nodes (7): SidebarContext, AppartmentModal(), ApartmentList(), BillListModal(), UserListModal(), react, react-dropzone

### Community 48 - "JWT Authentication Filter"
Cohesion: 0.16
Nodes (4): ApiResponse, ForgotPasswordController, ForgotPasswordRequest, ResetPasswordRequest

### Community 50 - "Account Controllers"
Cohesion: 0.11
Nodes (3): RegisterAuthController, MailService, OTPService

### Community 51 - "Admin Bill Controllers"
Cohesion: 0.11
Nodes (4): adminRoutes, authRoutes, routes, userRoutes

### Community 54 - "HomeSecurity.java"
Cohesion: 0.06
Nodes (5): ApartmentConfig, AuthController, HomeSecurity, JwtAuthenticationFilter, JwtService

### Community 55 - "Bill Type Enums"
Cohesion: 0.22
Nodes (4): calculateChildrenHeight(), onFirstLoad(), Sidebar, sidebarEl

### Community 58 - "5. CÔNG NGHỆ ĐỀ XUẤT VÀ LÝ DO CHỌN"
Cohesion: 0.07
Nodes (28): 1. TỔNG QUAN, 2. CÔNG NGHỆ HIỆN TẠI, 3.1. Backend, 3.2. Frontend, 3.3. Database, 3. PHÂN TÍCH VẤN ĐỀ HIỆN TẠI, 4. PHÂN CÔNG NHIỆM VỤ, 5.1. Quản lý Database Migration: Flyway (+20 more)

### Community 59 - "Tab Component Logic"
Cohesion: 0.10
Nodes (9): BillDTO, BillEditDTO, BillType, CONTRIBUTION, ELECTRICITY, FIXED_COST, OTHER, SERVICE_COST (+1 more)

### Community 60 - "Collapse Component Logic"
Cohesion: 0.28
Nodes (3): on(), removeDataAttribute(), setDataAttribute()

### Community 62 - "Notification Controllers"
Cohesion: 0.28
Nodes (10): activateAccount(), columnHelper, ColumnTable(), deactivateAccount(), generateColumns(), toggleStatus(), ActionsCell(), columnHelper (+2 more)

### Community 65 - "User & Notification Services"
Cohesion: 0.23
Nodes (4): react-calendar@^5.0.0, events, events, react-calendar

### Community 66 - "Change Password Controllers"
Cohesion: 0.30
Nodes (9): ActionsCell(), columnHelper, columnsBillIcon(), BillModal(), activateAccount(), columnHelper, ColumnTable(), generateColumns() (+1 more)

### Community 67 - "Scrollbar Component Logic"
Cohesion: 0.17
Nodes (11): MYSQL_ROOT_PASSWORD, container_name, environment, hostname, image, ports, tty, volumes (+3 more)

### Community 68 - "ApiException"
Cohesion: 0.11
Nodes (5): AccountController, MeRequest, PasswordRequest, ApiException, GlobalExceptionHandler

### Community 73 - "MySQL Docker Config"
Cohesion: 0.27
Nodes (8): E(), find(), focusableChildren(), h(), m(), O(), p(), v()

### Community 74 - "Apartment API Controllers"
Cohesion: 0.20
Nodes (9): app-dark.css, file, src, assets/static/fonts/nunito-latin-400-normal.woff, file, src, assets/static/images/svg-loaders/ball-triangle.svg, file (+1 more)

### Community 76 - "Excel Export Utility"
Cohesion: 0.22
Nodes (7): @emotion/cache@^11.12.0, @emotion/react@^11.12.0, stylis-plugin-rtl@^2.0.2, options, @emotion/cache, @emotion/react, stylis-plugin-rtl

### Community 77 - "Popover Component Logic"
Cohesion: 0.22
Nodes (9): devDependencies, anymatch@^3.1.3, chokidar@^3.6.0, gh-pages@^6.1.1, micromatch@^4.0.7, anymatch, chokidar, gh-pages (+1 more)

### Community 78 - "Toast/Alert Component Logic"
Cohesion: 0.22
Nodes (8): barChartDataConsumption, barChartDataDailyTraffic, barChartOptionsConsumption, barChartOptionsDailyTraffic, lineChartDataTotalSpent, lineChartOptionsTotalSpent, pieChartData, pieChartOptions

### Community 80 - "Resident Controllers"
Cohesion: 0.25
Nodes (8): scripts, build, deploy, eject, predeploy, preinstall, start, test

### Community 81 - "Focus Trap Logic"
Cohesion: 0.25
Nodes (7): background_color, display, icons, name, short_name, start_url, theme_color

### Community 83 - "Bill DTO Classes"
Cohesion: 0.25
Nodes (7): columnsAppartmentDataColumns, columnsBillDataColumns, columnsComplaints, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 84 - "package.json"
Cohesion: 0.09
Nodes (22): apexcharts@3.50.0, @babel/preset-react@^7.18.6, framer-motion@^11.3.7, npm-force-resolutions@^0.0.10, react-dom@^18.3.1, react-is@^18.3.1, @testing-library/user-event@^14.5.2, eslintConfig (+14 more)

### Community 85 - "Apartment Exists Validator"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 86 - "RTL Provider Config"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 87 - "Dev Dependencies"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 88 - "mockApi.js"
Cohesion: 0.11
Nodes (10): api, apartments, feeTypes, MOCK_APARTMENTS, MOCK_FEE_TYPES, MOCK_RESIDENTS, MOCK_USER, mockApi (+2 more)

### Community 89 - "VehicleService"
Cohesion: 0.16
Nodes (3): VehicleController, VehicleRequest, VehicleService

### Community 90 - "JWT Service"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 92 - "PWA Manifest"
Cohesion: 0.50
Nodes (3): adaptPagination(), dataTable, refreshPagination()

### Community 94 - "Payment Column Data"
Cohesion: 0.50
Nodes (3): Role, ADMIN, RESIDENT

### Community 95 - "Relationship Enum"
Cohesion: 0.83
Nodes (3): initTheme(), setTheme(), toggleDarkTheme()

### Community 96 - "Apartment Column Data"
Cohesion: 0.50
Nodes (3): optionsAmerica, optionsIndonesia, optionsVisitorsProfile

### Community 98 - "User Column Data"
Cohesion: 0.50
Nodes (3): compilerOptions, baseUrl, paths

### Community 100 - "Spring Security Auth Controller"
Cohesion: 0.67
Nodes (3): vs(), o(), xs()

### Community 104 - "Spring Boot Application Entry"
Cohesion: 0.67
Nodes (3): app.css, file, src

### Community 105 - "DataTables Pagination Adapter"
Cohesion: 0.67
Nodes (3): application-email.css, file, src

### Community 106 - "Spring Boot Test Context"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.eot, file, src

### Community 107 - "React Pie Chart Component"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.svg, file, src

### Community 108 - "User Role Enum"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.ttf, file, src

### Community 109 - "Dark Theme Initialization"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.woff, file, src

### Community 110 - "Dashboard Chart Options"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-300-normal.woff, file, src

### Community 111 - "jQuery DataTables Config"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-300-normal.woff2, file, src

### Community 112 - "JSConfig Path Aliases"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-400-normal.woff2, file, src

### Community 113 - "Apartment Not Found Exception"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-600-normal.woff, file, src

### Community 114 - "CSS Grid Size Variables"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-600-normal.woff2, file, src

### Community 115 - "Toast Notification Components"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-700-normal.woff, file, src

### Community 116 - "Horizontal Layout Responsive"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-700-normal.woff2, file, src

### Community 117 - "SweetAlert2 Toast Wrapper"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-800-normal.woff, file, src

### Community 118 - "App Main Stylesheet"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-800-normal.woff2, file, src

### Community 119 - "Email Template Stylesheet"
Cohesion: 0.67
Nodes (3): assets/static/images/bg/4853433.png, file, src

### Community 120 - "Iconly Bold Font EOT"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/1.jpg, file, src

### Community 121 - "Iconly Bold Font SVG"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/2.jpg, file, src

### Community 122 - "Iconly Bold Font TTF"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/3.jpg, file, src

### Community 123 - "Iconly Bold Font WOFF"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/4.jpg, file, src

### Community 124 - "Nunito Font 300 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/5.jpg, file, src

### Community 125 - "Nunito Font 300 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/6.jpg, file, src

### Community 126 - "Nunito Font 400 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/7.jpg, file, src

### Community 127 - "Nunito Font 600 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/8.jpg, file, src

### Community 128 - "Nunito Font 600 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/images/logo/favicon.svg, file, src

### Community 129 - "Nunito Font 700 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/images/logo/logo.svg, file, src

### Community 130 - "Nunito Font 700 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/1.png, file, src

### Community 131 - "Nunito Font 800 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/2.png, file, src

### Community 132 - "Nunito Font 800 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/3.png, file, src

### Community 133 - "Background Image Asset"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/4.png, file, src

### Community 134 - "Avatar Face Image 1"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/architecture1.jpg, file, src

### Community 135 - "Avatar Face Image 2"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/banana.jpg, file, src

### Community 136 - "Avatar Face Image 3"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/bg-mountain.jpg, file, src

### Community 137 - "Avatar Face Image 4"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/building.jpg, file, src

### Community 138 - "Avatar Face Image 5"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/error-403.svg, file, src

### Community 139 - "Avatar Face Image 6"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/error-404.svg, file, src

### Community 140 - "Avatar Face Image 7"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/error-500.svg, file, src

### Community 141 - "Avatar Face Image 8"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/jump.jpg, file, src

### Community 142 - "Favicon Logo SVG"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/motorcycle.jpg, file, src

### Community 143 - "Main Logo SVG"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/origami.jpg, file, src

### Community 144 - "Sample Image 1"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/water.jpg, file, src

### Community 145 - "Sample Image 2"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/audio.svg, file, src

### Community 146 - "Sample Image 3"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/circles.svg, file, src

### Community 147 - "Sample Image 4"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/grid.svg, file, src

### Community 148 - "Architecture Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/hearts.svg, file, src

### Community 149 - "Banana Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/oval.svg, file, src

### Community 150 - "Mountain Background Image"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/puff.svg, file, src

### Community 151 - "Building Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/rings.svg, file, src

### Community 152 - "Error 403 Illustration"
Cohesion: 0.67
Nodes (3): auth.css, file, src

### Community 153 - "Error 404 Illustration"
Cohesion: 0.67
Nodes (3): error.css, file, src

### Community 154 - "Error 500 Illustration"
Cohesion: 0.67
Nodes (3): form-editor-summernote.css, file, src

### Community 155 - "Jump Sample Image"
Cohesion: 0.67
Nodes (3): iconly.css, file, src

### Community 156 - "Motorcycle Sample Image"
Cohesion: 0.67
Nodes (3): ../node_modules/bootstrap-icons/font/fonts/bootstrap-icons.woff2, file, src

### Community 157 - "Origami Sample Image"
Cohesion: 0.67
Nodes (3): table-datatable.css, file, src

### Community 158 - "Water Sample Image"
Cohesion: 0.67
Nodes (3): table-datatable-jquery.css, file, src

### Community 159 - "Audio SVG Loader"
Cohesion: 0.67
Nodes (3): ui-icons-dripicons.css, file, src

### Community 160 - "Circles SVG Loader"
Cohesion: 0.67
Nodes (3): ui-widgets-chatbox.css, file, src

### Community 161 - "Grid SVG Loader"
Cohesion: 0.67
Nodes (3): ui-widgets-todolist.css, file, src

### Community 191 - "BlueMoon - Hệ thống Quản lý Chung cư (Apartment Management)"
Cohesion: 0.12
Nodes (15): Backend, BlueMoon - Hệ thống Quản lý Chung cư (Apartment Management), Chức năng chính, Cách 1: Chạy bằng Docker (khuyến nghị), Cách 2: Chạy local để develop, Cách cài đặt & chạy hệ thống, Công nghệ đang sử dụng, Cấu trúc thư mục (+7 more)

### Community 195 - "user/dataTables/components/ColumnsTable.js"
Cohesion: 0.30
Nodes (9): activateAccount(), columnHelper, ColumnTable(), generateColumns(), toggleStatus(), ActionsCell(), columnHelper, columnsIcon() (+1 more)

### Community 198 - "Relationship"
Cohesion: 0.25
Nodes (6): Relationship, CHILD, HEAD, OTHER, PARENT, SPOUSE

### Community 200 - "user/ColumnsData.js"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 201 - "PaymentStatus"
Cohesion: 0.33
Nodes (5): PaymentStatus, FAILED, PAID, REFUNDED, UNPAID

### Community 204 - "browserslist"
Cohesion: 0.67
Nodes (3): browserslist, development, production

## Knowledge Gaps
- **50 isolated node(s):** `com.qlcc:qlcc-backend`, `OCCUPIED`, `EMPTY`, `UNPAID`, `PAID` (+45 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 1015 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **75 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `react` connect `react` to `@chakra-ui/react`, `Admin Dashboard Components`, `Admin User Management`, `dependencies`, `Resident Entity & Repository`, `Modal Component Logic`, `Admin User List Controllers`, `User Profile & Modals`, `API Exception Handling`, `Frontend Package Config`, `Minified/Compiled Code`, `Statistics Controllers`, `User DTO Classes`, `Admin Bill Controllers`, `Notification Controllers`, `NavbarLinksAdmin.js`, `User & Notification Services`, `Change Password Controllers`, `user/dataTables/components/ColumnsTable.js`, `PieChart`, `Static Assets & Manifest`, `package.json`?**
  _High betweenness centrality (0.032) - this node is a cross-community bridge._
- **Are the 3 inferred relationships involving `Resident` (e.g. with `.run()` and `.addUser()`) actually correct?**
  _`Resident` has 3 INFERRED edges - model-reasoned connections that need verification._
- **What connects `com.qlcc:qlcc-backend`, `OCCUPIED`, `EMPTY` to the rest of the system?**
  _50 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `@chakra-ui/react` be split into smaller, more focused modules?**
  _Cohesion score 0.08858858858858859 - nodes in this community are weakly interconnected._
- **Why does `Apartment` connect `Apartment` to `Frontend Auth & Layout`, `Residency Event Controllers`, `Backend Controllers`, `Apartment Frontend Components`, `UI Component Internals`, `Validation Annotations`, `Bill Entity & Repository`, `Apartment Fee Unit Entities`, `Bill Frontend Components`, `Frontend Dependencies`, `Chakra UI Theme Components`, `Auth Pages & Components`, `ResidentStatus`, `Chart Components`, `HomeSecurity.java`, `5. CÔNG NGHỆ ĐỀ XUẤT VÀ LÝ DO CHỌN`, `ApartmentService`, `ResidentDTO`, `ResidentController`, `ApartmentDTO`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **Should `Frontend Auth & Layout` be split into smaller, more focused modules?**
  _Cohesion score 0.10505050505050505 - nodes in this community are weakly interconnected._
- **Why does `Resident` connect `Frontend Auth & Layout` to `Backend Controllers`, `Apartment Frontend Components`, `Apartment`, `UI Component Internals`, `Validation Annotations`, `Apartment Fee Unit Entities`, `Bill Frontend Components`, `org.springframework.stereotype.Component`, `ResidentStatus`, `Frontend Route Config`, `Account Controllers`, `5. CÔNG NGHỆ ĐỀ XUẤT VÀ LÝ DO CHỌN`, `.findById`, `ApartmentService`, `ResidentDTO`, `AdminListUserController`, `ResidentController`, `Relationship`, `ManualNotification`?**
  _High betweenness centrality (0.023) - this node is a cross-community bridge._