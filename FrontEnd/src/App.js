import './assets/css/App.css';
import { Routes, Route, Navigate } from 'react-router-dom';
import AuthLayout from './layouts/auth';
import AdminLayout from './layouts/admin';
import UserLayout from './layouts/user';
import RTLLayout from './layouts/rtl';
import { ChakraProvider } from '@chakra-ui/react';
import initialTheme from './theme/theme';
import { useState, useEffect } from 'react';
import AuthGuard from "./authguard/AuthGuard"; // Import AuthGuard
import SpecLayout from './views/spec/SpecLayout';
import SpecSignIn from './views/spec/SignIn';
import DashboardPage from './views/spec/DashboardPage';
import AccountPage from './views/spec/AccountPage';
import ComingSoonPage from './views/spec/ComingSoonPage';

import { useLocation } from 'react-router-dom';

export default function Main() {
  const [currentTheme, setCurrentTheme] = useState(initialTheme);
  const [role, setRole] = useState(null);
  const location = useLocation();

  useEffect(() => {
    const token = localStorage.getItem("token");
    
    if (token) {
      try {
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Giải mã JWT
        setRole(decodedToken.role); // Lấy giá trị role từ token
      } catch (error) {
        console.error("Lỗi khi giải mã token:", error);
        setRole(null);
      }
    } else {
      setRole(null);
    }
  }, []);

  return (
    <ChakraProvider theme={currentTheme}>
      <Routes>
        <Route path="auth/*" element={<AuthLayout />} />

        {/* BlueMoon spec UI (design tu file src mau) */}
        <Route path="/dang-nhap" element={<SpecSignIn />} />
        <Route path="/app" element={<SpecLayout />}>
          <Route index element={<Navigate to="/app/tong-quan" replace />} />
          <Route path="tong-quan" element={<DashboardPage />} />
          <Route path="tai-khoan" element={<AccountPage />} />
          <Route path="khoan-thu/*" element={<ComingSoonPage title="Khoản thu" description="Quản lý các khoản thu và đóng góp." />} />
          <Route path="can-ho/*" element={<ComingSoonPage title="Căn hộ" description="Quản lý thông tin căn hộ." />} />
          <Route path="nguoi-dan/*" element={<ComingSoonPage title="Người dân" description="Quản lý hộ gia đình và nhân khẩu." />} />
          <Route path="thu-phi/*" element={<ComingSoonPage title="Thu phí" description="Ghi nhận và theo dõi các khoản thu." />} />
          <Route path="bien-dong" element={<ComingSoonPage title="Biến động" description="Theo dõi biến động nhân khẩu, tạm vắng, tạm trú." />} />
          <Route path="thong-ke" element={<ComingSoonPage title="Thống kê" description="Thống kê tình trạng đóng góp." />} />
          <Route path="*" element={<ComingSoonPage title="Không tìm thấy trang" description="Đường dẫn không tồn tại." />} />
        </Route>

        {/* Bọc admin và user bằng AuthGuard đúng cách */}
        <Route key={location.pathname}
          path="admin/*"
          element={
            <AuthGuard>
              <AdminLayout theme={currentTheme} setTheme={setCurrentTheme} />
            </AuthGuard>
          }
        />
        <Route 
          path="user/*"
          element={
            <AuthGuard>
              <UserLayout theme={currentTheme} setTheme={setCurrentTheme} />
            </AuthGuard>
          }
        />

        <Route 
          path="rtl/*"
          element={<RTLLayout theme={currentTheme} setTheme={setCurrentTheme} />}
        />

        <Route path="/" element={<Navigate to="/dang-nhap" replace />} />
      </Routes>
    </ChakraProvider>
  );
}
