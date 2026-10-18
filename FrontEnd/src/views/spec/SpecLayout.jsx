import React, { useEffect, useRef, useState } from 'react';
import { Outlet, NavLink, useLocation, useNavigate } from 'react-router-dom';
import api from 'services/apiConfig';
import './spec.css';

const NAV_GROUPS = [
  { label: 'Tổng quan', items: [{ path: '/app/tong-quan', label: 'Tổng quan' }] },
  {
    label: 'Danh mục',
    items: [
      { path: '/app/khoan-thu', label: 'Khoản thu' },
      { path: '/app/can-ho', label: 'Căn hộ' },
      { path: '/app/nguoi-dan', label: 'Người dân' },
    ],
  },
  {
    label: 'Nghiệp vụ',
    items: [
      { path: '/app/thu-phi', label: 'Thu phí' },
      { path: '/app/bien-dong', label: 'Biến động' },
    ],
  },
  {
    label: 'Báo cáo',
    items: [
      { path: '/app/thong-ke', label: 'Thống kê' },
      { path: '/app/tai-khoan', label: 'Tài khoản' },
    ],
  },
];

const PAGE_TITLES = {
  '/app/tong-quan': 'Tổng quan',
  '/app/khoan-thu': 'Khoản thu',
  '/app/can-ho': 'Căn hộ',
  '/app/nguoi-dan': 'Người dân',
  '/app/thu-phi': 'Thu phí',
  '/app/bien-dong': 'Biến động',
  '/app/thong-ke': 'Thống kê',
  '/app/tai-khoan': 'Tài khoản',
};

export const SpecUserContext = React.createContext({ user: null, setUser: () => {} });

export default function SpecLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [userMenu, setUserMenu] = useState(false);
  const menuRef = useRef(null);
  const token = localStorage.getItem('token');

  useEffect(() => {
    if (!token) {
      navigate('/dang-nhap', { replace: true });
      return;
    }
    api
      .get('/auth/me')
      .then((res) => setUser(res.data))
      .catch(() => {
        localStorage.removeItem('token');
        navigate('/dang-nhap', { replace: true });
      });
  }, []);

  useEffect(() => {
    const close = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) setUserMenu(false);
    };
    document.addEventListener('mousedown', close);
    return () => document.removeEventListener('mousedown', close);
  }, []);

  if (!token) return null;

  const title = PAGE_TITLES[location.pathname] || 'Tổng quan';
  const initials = (user?.fullName || user?.name || 'A')
    .split(' ')
    .map((w) => w[0])
    .slice(-2)
    .join('')
    .toUpperCase();

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/dang-nhap', { replace: true });
  };

  return (
    <div className="spec-root">
      <aside className="sp-sidebar">
        <div className="sp-brand">
          <div className="sp-brand-badge">B</div>
          <div>
            <div className="sp-brand-name">BLUEMOON</div>
            <div className="sp-brand-sub">Quản lý thu phí</div>
          </div>
        </div>
        <nav className="sp-nav">
          {NAV_GROUPS.map((g) => (
            <div key={g.label} style={{ marginBottom: 20 }}>
              <div className="sp-nav-group-label">{g.label}</div>
              {g.items.map((item) => (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive }) => `sp-nav-item${isActive ? ' active' : ''}`}
                >
                  {item.label}
                </NavLink>
              ))}
            </div>
          ))}
        </nav>
      </aside>

      <div className="sp-main">
        <header className="sp-topbar">
          <div>
            <div className="sp-topbar-app">BlueMoon</div>
            <div className="sp-topbar-page">{title}</div>
          </div>
          <div style={{ position: 'relative' }} ref={menuRef}>
            <button
              className="sp-user-btn"
              aria-expanded={userMenu}
              aria-haspopup="menu"
              onClick={() => setUserMenu((v) => !v)}
            >
              <div>
                <div className="sp-user-name">{user?.fullName || user?.name || '—'}</div>
                <div className="sp-user-role">{user?.role || 'ADMIN'}</div>
              </div>
              <div className="sp-avatar">{initials}</div>
            </button>
            {userMenu && (
              <div role="menu" className="sp-user-menu">
                <button
                  role="menuitem"
                  onClick={() => {
                    setUserMenu(false);
                    navigate('/app/tai-khoan');
                  }}
                >
                  Tài khoản
                </button>
                <button role="menuitem" className="danger" onClick={logout}>
                  Đăng xuất
                </button>
              </div>
            )}
          </div>
        </header>
        <main className="sp-content">
          <SpecUserContext.Provider value={{ user, setUser }}>
            <Outlet />
          </SpecUserContext.Provider>
        </main>
      </div>
    </div>
  );
}
