# Graph Report - bluemoon  (2026-10-31)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 2631 nodes · 5887 edges · 207 communities (127 shown, 80 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 331 edges (avg confidence: 0.85)
- Token cost: 47,327 input · 2,776 output

## Graph Freshness
- Built from commit: `a47a9d6c`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Frontend UI Components
- Frontend Auth & Layout
- Admin Data Tables
- Admin User Management
- Residency Event Controllers
- Backend Controllers
- Apartment Frontend Components
- Apartment Backend Config
- Docker Configuration
- Fee Type Controllers
- Resident Entity & Repository
- UI Component Internals
- Minified/Compiled Code
- Carousel Component Logic
- Admin Apartment Controllers
- Fee Record Controllers
- Icon Components
- Fee Record Entity
- Validation Annotations
- Modal Component Logic
- Admin User List Controllers
- Bill Entity & Repository
- Apartment Spec Controllers
- Apartment Fee Unit Entities
- Bill Frontend Components
- Minified/Compiled Code
- Frontend Dependencies
- Chakra UI Theme Components
- User Apartment Data Tables
- User Profile & Modals
- Vehicle Entity
- API Exception Handling
- Frontend Package Config
- Auth Pages & Components
- Minified/Compiled Code
- Dropdown Component Logic
- Tooltip Component Logic
- Notification Entity & Service
- Statistics Controllers
- User DTO Classes
- Toast Component Logic
- Forgot Password Controllers
- Validation Annotations
- JWT Authentication Filter
- Frontend Route Config
- Account Controllers
- Admin Bill Controllers
- Sidebar Component Logic
- Chart Components
- Vehicle Controllers
- Bill Type Enums
- Security Configuration
- Fee Type Enums
- Bill Service Methods
- Tab Component Logic
- Collapse Component Logic
- Scrollspy Component Logic
- Notification Controllers
- Manual Notification Entity
- Bill API Controllers
- User & Notification Services
- Change Password Controllers
- Scrollbar Component Logic
- Modal Backdrop Logic
- Sidebar Toggle Logic
- Calendar & Homepage Components
- Admin Bill Data Tables
- User Data Table Components
- MySQL Docker Config
- Apartment API Controllers
- OTP Service
- Excel Export Utility
- Popover Component Logic
- Toast/Alert Component Logic
- Resident Controllers
- Focus Trap Logic
- Static Assets & Manifest
- Bill DTO Classes
- Change Password Request DTO
- Apartment Exists Validator
- RTL Provider Config
- Dev Dependencies
- Chart Data Config
- Payment Status Enums
- NPM Scripts
- PWA Manifest
- Payment Column Data
- Relationship Enum
- Apartment Column Data
- Admin Column Data Variables
- User Column Data
- User Column Data Variables
- Spring Security Auth Controller
- Email OTP Mail Service
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
- Bootstrap Icons Font
- DataTable Stylesheet
- jQuery DataTable Stylesheet
- Dripicons Icon Stylesheet
- Chatbox Widget Stylesheet
- Todolist Widget Stylesheet
- Browserslist Config
- Admin Default Column Data
- Admin RTL Column Data
- User Default Column Data
- User RTL Column Data
- Profile Edit DTO
- Theme Initialization Script
- Select Form Element
- jQuery Reference
- ESLint Override Config
- React Toastify Dependency
- Testing Library React
- Web Vitals Dependency
- QLCC Backend

## God Nodes (most connected - your core abstractions)
1. `react` - 177 edges
2. `@chakra-ui/react` - 168 edges
3. `Resident` - 84 edges
4. `ResidentRepository` - 72 edges
5. `Apartment` - 61 edges
6. `Bill` - 54 edges
7. `ke` - 53 edges
8. `User` - 50 edges
9. `ApartmentRepository` - 46 edges
10. `he` - 40 edges

## Surprising Connections (you probably didn't know these)
- `FeeTypeRequest` --references--> `FeeTypeType`  [EXTRACTED]
  Backend/src/main/java/com/qlcc/dto/SpecDtos.java → Backend/src/main/java/com/qlcc/enums/FeeTypeType.java
- `FeeRecord` --references--> `FeeType`  [EXTRACTED]
  Backend/src/main/java/com/qlcc/entity/FeeRecord.java → Backend/src/main/java/com/qlcc/entity/FeeType.java
- `FeeType` --references--> `FeeTypeType`  [EXTRACTED]
  Backend/src/main/java/com/qlcc/entity/FeeType.java → Backend/src/main/java/com/qlcc/enums/FeeTypeType.java
- `FeeRecordService` --references--> `FeeTypeRepository`  [EXTRACTED]
  Backend/src/main/java/com/qlcc/service/FeeRecordService.java → Backend/src/main/java/com/qlcc/repository/FeeTypeRepository.java
- `FeeRecordService` --references--> `FeeTypeService`  [EXTRACTED]
  Backend/src/main/java/com/qlcc/service/FeeRecordService.java → Backend/src/main/java/com/qlcc/service/FeeTypeService.java

## Import Cycles
- None detected.

## Communities (207 total, 80 thin omitted)

### Community 0 - "Frontend UI Components"
Cohesion: 0.05
Nodes (18): SidebarContext, AppartmentModal(), ApartmentList(), BillListModal(), ChangePassModal(), GeneralInformation(), UserListModal(), Overview() (+10 more)

### Community 1 - "Frontend Auth & Layout"
Cohesion: 0.05
Nodes (20): Main(), AuthGuard(), isTokenExpired(), AuthNavbar(), styleSheet, root, Dashboard(), Auth() (+12 more)

### Community 3 - "Admin Data Tables"
Cohesion: 0.05
Nodes (27): columnHelper, activateAccount(), columnHelper, ColumnTable(), deactivateAccount(), generateColumns(), toggleStatus(), columnHelper (+19 more)

### Community 4 - "Admin User Management"
Cohesion: 0.05
Nodes (6): AdminUserInitializer, AdminController, ManualUserDTO, User, UserRepository, UserService

### Community 5 - "Residency Event Controllers"
Cohesion: 0.06
Nodes (14): ResidencyEventController, SpecResidentController, ResidencyEventRequest, ResidencyEvent, ResidencyEventType, INFO_CHANGE, MOVE_IN, MOVE_OUT (+6 more)

### Community 6 - "Backend Controllers"
Cohesion: 0.08
Nodes (4): RegisterAuthController, RegisterController, BulkIdsRequest, UserDTO

### Community 7 - "Apartment Frontend Components"
Cohesion: 0.07
Nodes (24): api, AppartmentModal(), activateAccount(), cache, columnHelper, ColumnTable(), generateColumns(), toggleStatus() (+16 more)

### Community 8 - "Apartment Backend Config"
Cohesion: 0.08
Nodes (12): ApartmentConfig, DashboardController, Apartment, ApartmentStatus, EMPTY, OCCUPIED, ResidentStatus, LIVING (+4 more)

### Community 9 - "Docker Configuration"
Cohesion: 0.04
Nodes (47): driver, REACT_APP_API_BASE_URL, build, container_name, depends_on, environment, networks, ports (+39 more)

### Community 10 - "Fee Type Controllers"
Cohesion: 0.08
Nodes (13): FeeTypeController, FeeTypeRequest, FeeType, FeeCalcMethod, MANUAL_PER_APARTMENT, PER_AREA, PER_VEHICLE, VOLUNTARY (+5 more)

### Community 11 - "Resident Entity & Repository"
Cohesion: 0.10
Nodes (3): Resident, ResidentRepository, ResidentService

### Community 13 - "Minified/Compiled Code"
Cohesion: 0.07
Nodes (27): Cr(), cs(), De(), ds(), Ee(), fs(), ge(), getDataAttribute() (+19 more)

### Community 15 - "Admin Apartment Controllers"
Cohesion: 0.08
Nodes (4): AdminListApartmentController, UserApartmentDetailController, ApartmentDTO, ApartmentService

### Community 16 - "Fee Record Controllers"
Cohesion: 0.10
Nodes (8): FeeRecordController, AmountRequest, CollectRequest, ContributionRequest, GenerateRequest, ResidentRequest, ResidentUpdateRequest, SpecDtos

### Community 17 - "Icon Components"
Cohesion: 0.05
Nodes (36): AdobexdLogo, AndroidLogo, AppleLogo, AtlassianLogo, BitcoinLogo, ButtonLeft, ButtonRight, CartIcon (+28 more)

### Community 19 - "Fee Record Entity"
Cohesion: 0.11
Nodes (5): FeeRecord, BillStatus, PAID, UNPAID, FeeRecordRepository

### Community 20 - "Validation Annotations"
Cohesion: 0.13
Nodes (5): ApartmentsExistsValidator, ResidentBelongsToApartmentValidator, UniqueEmailValidator, UniqueNameValidator, ValidPasswordValidator

### Community 24 - "Apartment Spec Controllers"
Cohesion: 0.13
Nodes (3): SpecApartmentController, ApartmentRequest, SpecApartmentService

### Community 25 - "Apartment Fee Unit Entities"
Cohesion: 0.14
Nodes (3): ApartmentFeeUnit, ApartmentFeeUnitRepository, ApartmentFeeUnitService

### Community 26 - "Bill Frontend Components"
Cohesion: 0.11
Nodes (18): BillModal(), activateAccount(), columnHelper, ColumnTable(), generateColumns(), toggleStatus(), columnsAppartmentDataColumns, columnsBillDataColumns (+10 more)

### Community 28 - "Minified/Compiled Code"
Cohesion: 0.12
Nodes (27): Ai(), An(), as(), at(), bi(), ct(), En(), es() (+19 more)

### Community 29 - "Frontend Dependencies"
Cohesion: 0.07
Nodes (27): dependencies, axios@^1.8.4, @chakra-ui/icons@^2.0.19, @chakra-ui/react@2.6.1, @chakra-ui/system@2.5.7, @emotion/styled@^11.12.0, jwt-decode@^4.0.0, react@^18.3.1 (+19 more)

### Community 30 - "Chakra UI Theme Components"
Cohesion: 0.14
Nodes (14): @chakra-ui/theme-tools@^1.3.6, Card, CardComponent, badgeStyles, buttonStyles, inputStyles, linkStyles, progressStyles (+6 more)

### Community 31 - "User Apartment Data Tables"
Cohesion: 0.14
Nodes (19): ActionsCell(), columnHelper, columnsDataIcon(), AppartmentModal(), activateAccount(), columnHelper, ColumnTable(), generateColumns() (+11 more)

### Community 32 - "User Profile & Modals"
Cohesion: 0.16
Nodes (15): UserFilterModal(), ActionsCell(), columnHelper, columnsIcon(), Settings(), NotificationModal(), UserModal(), activateAccount() (+7 more)

### Community 34 - "Vehicle Entity"
Cohesion: 0.11
Nodes (8): Vehicle, VehicleStatus, ACTIVE, INACTIVE, VehicleType, CAR, MOTORBIKE, VehicleRepository

### Community 35 - "API Exception Handling"
Cohesion: 0.15
Nodes (3): ApiException, ApiExceptionHandler, GlobalExceptionHandler

### Community 36 - "Frontend Package Config"
Cohesion: 0.09
Nodes (22): apexcharts@3.50.0, @babel/preset-react@^7.18.6, framer-motion@^11.3.7, npm-force-resolutions@^0.0.10, react-dom@^18.3.1, react-is@^18.3.1, @testing-library/user-event@^14.5.2, eslintConfig (+14 more)

### Community 37 - "Auth Pages & Components"
Cohesion: 0.16
Nodes (3): isTokenExpired(), SignIn(), jwt-decode

### Community 38 - "Minified/Compiled Code"
Cohesion: 0.20
Nodes (19): Ae(), bn(), bt(), ce(), Ei(), fi(), Fo(), gs() (+11 more)

### Community 42 - "Notification Entity & Service"
Cohesion: 0.15
Nodes (3): Notification, NotificationRepository, NotificationService

### Community 43 - "Statistics Controllers"
Cohesion: 0.14
Nodes (3): StatisticsController, CollectBulkRequest, FeeRecordService

### Community 44 - "User DTO Classes"
Cohesion: 0.13
Nodes (3): UserCreationRequest, ApartmentsExists, ValidPassword

### Community 46 - "Forgot Password Controllers"
Cohesion: 0.16
Nodes (4): ApiResponse, ForgotPasswordController, ForgotPasswordRequest, ResetPasswordRequest

### Community 47 - "Validation Annotations"
Cohesion: 0.29
Nodes (3): UniqueEmail, UniqueName, UniqueNumber

### Community 49 - "Frontend Route Config"
Cohesion: 0.11
Nodes (4): adminRoutes, authRoutes, routes, userRoutes

### Community 50 - "Account Controllers"
Cohesion: 0.19
Nodes (4): AccountController, MeRequest, PasswordRequest, AccountService

### Community 52 - "Sidebar Component Logic"
Cohesion: 0.22
Nodes (4): calculateChildrenHeight(), onFirstLoad(), Sidebar, sidebarEl

### Community 53 - "Chart Components"
Cohesion: 0.12
Nodes (5): react-apexcharts@1.4.1, ColumnChart, LineChart, LineChart, react-apexcharts

### Community 54 - "Vehicle Controllers"
Cohesion: 0.17
Nodes (3): VehicleController, VehicleRequest, VehicleService

### Community 55 - "Bill Type Enums"
Cohesion: 0.12
Nodes (8): BillEditDTO, BillType, CONTRIBUTION, ELECTRICITY, FIXED_COST, OTHER, SERVICE_COST, WATER

### Community 57 - "Fee Type Enums"
Cohesion: 0.15
Nodes (9): FeeTypeType, DONATION, ELECTRICITY, INTERNET, MANAGEMENT, PARKING_CAR, PARKING_MOTORBIKE, SERVICE (+1 more)

### Community 63 - "Manual Notification Entity"
Cohesion: 0.19
Nodes (3): ManualNotification, ManualNotificationRepository, ManualNotificationService

### Community 67 - "Scrollbar Component Logic"
Cohesion: 0.28
Nodes (3): on(), removeDataAttribute(), setDataAttribute()

### Community 70 - "Calendar & Homepage Components"
Cohesion: 0.23
Nodes (4): react-calendar@^5.0.0, events, events, react-calendar

### Community 71 - "Admin Bill Data Tables"
Cohesion: 0.30
Nodes (9): ActionsCell(), columnHelper, columnsBillIcon(), BillModal(), activateAccount(), columnHelper, ColumnTable(), generateColumns() (+1 more)

### Community 72 - "User Data Table Components"
Cohesion: 0.30
Nodes (9): activateAccount(), columnHelper, ColumnTable(), generateColumns(), toggleStatus(), ActionsCell(), columnHelper, columnsIcon() (+1 more)

### Community 73 - "MySQL Docker Config"
Cohesion: 0.17
Nodes (11): MYSQL_ROOT_PASSWORD, container_name, environment, hostname, image, ports, tty, volumes (+3 more)

### Community 81 - "Focus Trap Logic"
Cohesion: 0.27
Nodes (8): E(), find(), focusableChildren(), h(), m(), O(), p(), v()

### Community 82 - "Static Assets & Manifest"
Cohesion: 0.20
Nodes (9): app-dark.css, file, src, assets/static/fonts/nunito-latin-400-normal.woff, file, src, assets/static/images/svg-loaders/ball-triangle.svg, file (+1 more)

### Community 86 - "RTL Provider Config"
Cohesion: 0.22
Nodes (7): @emotion/cache@^11.12.0, @emotion/react@^11.12.0, stylis-plugin-rtl@^2.0.2, options, @emotion/cache, @emotion/react, stylis-plugin-rtl

### Community 87 - "Dev Dependencies"
Cohesion: 0.22
Nodes (9): devDependencies, anymatch@^3.1.3, chokidar@^3.6.0, gh-pages@^6.1.1, micromatch@^4.0.7, anymatch, chokidar, gh-pages (+1 more)

### Community 88 - "Chart Data Config"
Cohesion: 0.22
Nodes (8): barChartDataConsumption, barChartDataDailyTraffic, barChartOptionsConsumption, barChartOptionsDailyTraffic, lineChartDataTotalSpent, lineChartOptionsTotalSpent, pieChartData, pieChartOptions

### Community 89 - "Payment Status Enums"
Cohesion: 0.25
Nodes (6): ResidentContributionDTO, PaymentStatus, FAILED, PAID, REFUNDED, UNPAID

### Community 91 - "NPM Scripts"
Cohesion: 0.25
Nodes (8): scripts, build, deploy, eject, predeploy, preinstall, start, test

### Community 92 - "PWA Manifest"
Cohesion: 0.25
Nodes (7): background_color, display, icons, name, short_name, start_url, theme_color

### Community 94 - "Payment Column Data"
Cohesion: 0.25
Nodes (7): columnsAppartmentDataColumns, columnsBillDataColumns, columnsComplaints, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 95 - "Relationship Enum"
Cohesion: 0.29
Nodes (6): Relationship, CHILD, HEAD, OTHER, PARENT, SPOUSE

### Community 96 - "Apartment Column Data"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 97 - "Admin Column Data Variables"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 98 - "User Column Data"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 99 - "User Column Data Variables"
Cohesion: 0.29
Nodes (6): columnsAppartmentDataColumns, columnsBillDataColumns, columnsDataCheck, columnsDataColumns, columnsDataComplex, columnsDataDevelopment

### Community 105 - "DataTables Pagination Adapter"
Cohesion: 0.50
Nodes (3): adaptPagination(), dataTable, refreshPagination()

### Community 108 - "User Role Enum"
Cohesion: 0.50
Nodes (3): Role, ADMIN, RESIDENT

### Community 109 - "Dark Theme Initialization"
Cohesion: 0.83
Nodes (3): initTheme(), setTheme(), toggleDarkTheme()

### Community 110 - "Dashboard Chart Options"
Cohesion: 0.50
Nodes (3): optionsAmerica, optionsIndonesia, optionsVisitorsProfile

### Community 112 - "JSConfig Path Aliases"
Cohesion: 0.50
Nodes (3): compilerOptions, baseUrl, paths

### Community 114 - "CSS Grid Size Variables"
Cohesion: 0.67
Nodes (3): vs(), o(), xs()

### Community 118 - "App Main Stylesheet"
Cohesion: 0.67
Nodes (3): app.css, file, src

### Community 119 - "Email Template Stylesheet"
Cohesion: 0.67
Nodes (3): application-email.css, file, src

### Community 120 - "Iconly Bold Font EOT"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.eot, file, src

### Community 121 - "Iconly Bold Font SVG"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.svg, file, src

### Community 122 - "Iconly Bold Font TTF"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.ttf, file, src

### Community 123 - "Iconly Bold Font WOFF"
Cohesion: 0.67
Nodes (3): assets/static/fonts/Iconly---Bold.woff, file, src

### Community 124 - "Nunito Font 300 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-300-normal.woff, file, src

### Community 125 - "Nunito Font 300 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-300-normal.woff2, file, src

### Community 126 - "Nunito Font 400 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-400-normal.woff2, file, src

### Community 127 - "Nunito Font 600 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-600-normal.woff, file, src

### Community 128 - "Nunito Font 600 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-600-normal.woff2, file, src

### Community 129 - "Nunito Font 700 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-700-normal.woff, file, src

### Community 130 - "Nunito Font 700 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-700-normal.woff2, file, src

### Community 131 - "Nunito Font 800 WOFF"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-800-normal.woff, file, src

### Community 132 - "Nunito Font 800 WOFF2"
Cohesion: 0.67
Nodes (3): assets/static/fonts/nunito-latin-800-normal.woff2, file, src

### Community 133 - "Background Image Asset"
Cohesion: 0.67
Nodes (3): assets/static/images/bg/4853433.png, file, src

### Community 134 - "Avatar Face Image 1"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/1.jpg, file, src

### Community 135 - "Avatar Face Image 2"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/2.jpg, file, src

### Community 136 - "Avatar Face Image 3"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/3.jpg, file, src

### Community 137 - "Avatar Face Image 4"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/4.jpg, file, src

### Community 138 - "Avatar Face Image 5"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/5.jpg, file, src

### Community 139 - "Avatar Face Image 6"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/6.jpg, file, src

### Community 140 - "Avatar Face Image 7"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/7.jpg, file, src

### Community 141 - "Avatar Face Image 8"
Cohesion: 0.67
Nodes (3): assets/static/images/faces/8.jpg, file, src

### Community 142 - "Favicon Logo SVG"
Cohesion: 0.67
Nodes (3): assets/static/images/logo/favicon.svg, file, src

### Community 143 - "Main Logo SVG"
Cohesion: 0.67
Nodes (3): assets/static/images/logo/logo.svg, file, src

### Community 144 - "Sample Image 1"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/1.png, file, src

### Community 145 - "Sample Image 2"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/2.png, file, src

### Community 146 - "Sample Image 3"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/3.png, file, src

### Community 147 - "Sample Image 4"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/4.png, file, src

### Community 148 - "Architecture Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/architecture1.jpg, file, src

### Community 149 - "Banana Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/banana.jpg, file, src

### Community 150 - "Mountain Background Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/bg-mountain.jpg, file, src

### Community 151 - "Building Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/building.jpg, file, src

### Community 152 - "Error 403 Illustration"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/error-403.svg, file, src

### Community 153 - "Error 404 Illustration"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/error-404.svg, file, src

### Community 154 - "Error 500 Illustration"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/error-500.svg, file, src

### Community 155 - "Jump Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/jump.jpg, file, src

### Community 156 - "Motorcycle Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/motorcycle.jpg, file, src

### Community 157 - "Origami Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/origami.jpg, file, src

### Community 158 - "Water Sample Image"
Cohesion: 0.67
Nodes (3): assets/static/images/samples/water.jpg, file, src

### Community 159 - "Audio SVG Loader"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/audio.svg, file, src

### Community 160 - "Circles SVG Loader"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/circles.svg, file, src

### Community 161 - "Grid SVG Loader"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/grid.svg, file, src

### Community 162 - "Hearts SVG Loader"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/hearts.svg, file, src

### Community 163 - "Oval SVG Loader"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/oval.svg, file, src

### Community 164 - "Puff SVG Loader"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/puff.svg, file, src

### Community 165 - "Rings SVG Loader"
Cohesion: 0.67
Nodes (3): assets/static/images/svg-loaders/rings.svg, file, src

### Community 166 - "Auth Page Stylesheet"
Cohesion: 0.67
Nodes (3): auth.css, file, src

### Community 167 - "Error Page Stylesheet"
Cohesion: 0.67
Nodes (3): error.css, file, src

### Community 168 - "Summernote Editor Stylesheet"
Cohesion: 0.67
Nodes (3): form-editor-summernote.css, file, src

### Community 169 - "Iconly Icon Stylesheet"
Cohesion: 0.67
Nodes (3): iconly.css, file, src

### Community 170 - "Bootstrap Icons Font"
Cohesion: 0.67
Nodes (3): ../node_modules/bootstrap-icons/font/fonts/bootstrap-icons.woff2, file, src

### Community 171 - "DataTable Stylesheet"
Cohesion: 0.67
Nodes (3): table-datatable.css, file, src

### Community 172 - "jQuery DataTable Stylesheet"
Cohesion: 0.67
Nodes (3): table-datatable-jquery.css, file, src

### Community 173 - "Dripicons Icon Stylesheet"
Cohesion: 0.67
Nodes (3): ui-icons-dripicons.css, file, src

### Community 174 - "Chatbox Widget Stylesheet"
Cohesion: 0.67
Nodes (3): ui-widgets-chatbox.css, file, src

### Community 175 - "Todolist Widget Stylesheet"
Cohesion: 0.67
Nodes (3): ui-widgets-todolist.css, file, src

### Community 176 - "Browserslist Config"
Cohesion: 0.67
Nodes (3): browserslist, development, production

## Knowledge Gaps
- **50 isolated node(s):** `MANUAL_PER_APARTMENT`, `PER_AREA`, `PER_VEHICLE`, `VOLUNTARY`, `ACTIVE` (+45 more)
  These have ≤1 connection - possible missing edges. (Counts symbols only; 937 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **80 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `react` connect `Frontend UI Components` to `Frontend Auth & Layout`, `Admin Dashboard Components`, `Admin Data Tables`, `Apartment Frontend Components`, `UI Menu & Calendar Components`, `Bill Frontend Components`, `Frontend Dependencies`, `User Apartment Data Tables`, `User Profile & Modals`, `Frontend Package Config`, `Auth Pages & Components`, `Admin & User View Routes`, `Frontend Route Config`, `Chart Components`, `Calendar & Homepage Components`, `Admin Bill Data Tables`, `User Data Table Components`, `Profile Project Components`, `React Pie Chart Component`?**
  _High betweenness centrality (0.026) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `Resident` (e.g. with `.run()` and `.addUser()`) actually correct?**
  _`Resident` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `MANUAL_PER_APARTMENT`, `PER_AREA`, `PER_VEHICLE` to the rest of the system?**
  _50 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Frontend UI Components` be split into smaller, more focused modules?**
  _Cohesion score 0.04695304695304695 - nodes in this community are weakly interconnected._
- **Why does `@chakra-ui/react` connect `Frontend UI Components` to `User Profile & Modals`, `Frontend Auth & Layout`, `Admin Dashboard Components`, `Admin Data Tables`, `Frontend Package Config`, `Auth Pages & Components`, `Calendar & Homepage Components`, `Apartment Frontend Components`, `Admin Bill Data Tables`, `Admin & User View Routes`, `User Data Table Components`, `Frontend Route Config`, `UI Menu & Calendar Components`, `Profile Project Components`, `Bill Frontend Components`, `Frontend Dependencies`, `Chakra UI Theme Components`, `User Apartment Data Tables`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **Should `Frontend Auth & Layout` be split into smaller, more focused modules?**
  _Cohesion score 0.04900900900900901 - nodes in this community are weakly interconnected._
- **Why does `User` connect `Admin User Management` to `User & Notification Services`, `API Exception Handling`, `Residency Event Controllers`, `Backend Controllers`, `Apartment Backend Config`, `OTP Service`, `JWT Authentication Filter`, `Account Controllers`, `Fee Record Entity`, `Admin User List Controllers`, `Apartment Fee Unit Entities`?**
  _High betweenness centrality (0.018) - this node is a cross-community bridge._