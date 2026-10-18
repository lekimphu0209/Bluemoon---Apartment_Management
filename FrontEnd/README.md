# BlueMoon FrontEnd

React 18 (Create React App) — giao diện quản lý thu phí chung cư BlueMoon.

## Chạy local

```bash
npm install
npm start          # http://localhost:3000 — backend phải chạy ở http://localhost:9090
```

Địa chỉ backend cấu hình trong `.env` (`REACT_APP_API_BASE_URL`). File này không chứa secret.

## Cấu trúc chính

```
src/
├── App.js                 # Khai báo route: /dang-nhap, /app/* (UI mới), /admin|/user (UI cũ)
├── services/apiConfig.js  # Axios instance + JWT interceptor (token trong localStorage)
├── views/spec/            # ★ UI BlueMoon theo thiết kế mới
│   ├── spec.css           #   Theme (màu, card, form, sidebar) — CSS thuần
│   ├── SpecLayout.jsx     #   Sidebar + topbar + guard token + user-menu
│   ├── SignIn.jsx         #   /dang-nhap
│   ├── DashboardPage.jsx  #   /app/tong-quan
│   ├── AccountPage.jsx    #   /app/tai-khoan
│   └── ComingSoonPage.jsx #   Placeholder cho trang chưa làm
└── views/admin, views/user, layouts/, components/, theme/
                           # Template Horizon UI (Chakra) — UI cũ, thay dần
```

## Thêm trang mới (theo thiết kế)

1. Tạo `src/views/spec/<TenTrang>Page.jsx`, import `./spec.css`, dùng class `sp-*` có sẵn.
2. Đăng ký route trong `App.js` dưới `<Route path="/app" element={<SpecLayout />}>`, thay `ComingSoonPage` tương ứng.
3. Gọi API qua `api` từ `services/apiConfig` (đã tự gắn `Authorization: Bearer`).

Tài liệu luồng màn hình & liên kết: xem `../../BlueMoon_UI_Flow_ManHinh.md` (ngoài repo) hoặc `docs/`.

## Tài khoản test

`admin` / `1234567890` (tạo bởi backend từ `.env` root).

## Build Docker

Được build bởi `docker-compose.yml` ở root (`Dockerfile` + `nginx.conf` trong thư mục này).
