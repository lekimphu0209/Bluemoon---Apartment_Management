import React, { useContext, useEffect, useState } from 'react';
import api from 'services/apiConfig';
import { SpecUserContext } from './SpecLayout';
import './spec.css';

export default function AccountPage() {
  const { user, setUser } = useContext(SpecUserContext);
  const [tab, setTab] = useState('info');
  const [toast, setToast] = useState(null);
  const [fullName, setFullName] = useState('');
  const [saving, setSaving] = useState(false);
  const [infoError, setInfoError] = useState('');

  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [pwErrors, setPwErrors] = useState({});
  const [pwError, setPwError] = useState('');
  const [changing, setChanging] = useState(false);

  useEffect(() => {
    if (user) setFullName(user.fullName || '');
  }, [user]);

  useEffect(() => {
    if (!toast) return;
    const t = setTimeout(() => setToast(null), 3500);
    return () => clearTimeout(t);
  }, [toast]);

  const saveInfo = async (e) => {
    e.preventDefault();
    if (saving) return;
    setInfoError('');
    if (!fullName.trim()) {
      setInfoError('Vui lòng nhập họ tên.');
      return;
    }
    setSaving(true);
    try {
      const res = await api.put('/auth/me', { fullName: fullName.trim() });
      setUser(res.data);
      setToast('Đã lưu thay đổi thông tin cá nhân');
    } catch (err) {
      setInfoError(err.response?.data?.message || 'Không thể lưu thông tin.');
    } finally {
      setSaving(false);
    }
  };

  const changePw = async (e) => {
    e.preventDefault();
    if (changing) return;
    const errs = {};
    if (!oldPassword) errs.oldPassword = 'Vui lòng nhập mật khẩu hiện tại.';
    if (newPassword.length < 8)
      errs.newPassword = 'Mật khẩu mới phải có ít nhất 8 ký tự.';
    if (newPassword !== confirmPassword)
      errs.confirmPassword = 'Mật khẩu xác nhận không khớp.';
    setPwErrors(errs);
    setPwError('');
    if (Object.keys(errs).length) return;

    setChanging(true);
    try {
      await api.put('/auth/password', { oldPassword, newPassword });
      setToast('Đổi mật khẩu thành công');
      setOldPassword('');
      setNewPassword('');
      setConfirmPassword('');
    } catch (err) {
      const data = err.response?.data;
      if (data?.errors) setPwErrors(data.errors);
      setPwError(data?.message || 'Không thể đổi mật khẩu.');
    } finally {
      setChanging(false);
    }
  };

  const displayName = user?.fullName || user?.name || '—';
  const initials = displayName
    .split(' ')
    .filter(Boolean)
    .map((w) => w[0])
    .slice(-2)
    .join('')
    .toUpperCase();

  return (
    <>
      <div className="sp-header" style={{ maxWidth: 1080, margin: '0 auto 24px' }}>
        <h1>Tài khoản</h1>
        <p>Cập nhật thông tin cá nhân và bảo mật tài khoản.</p>
      </div>

      <div className="sp-account">
        <aside className="sp-card sp-profile">
          <div className="sp-profile-avatar">{initials}</div>
          <div className="sp-profile-name">{displayName}</div>
          <div className="sp-profile-role">{user?.role || 'ADMIN'}</div>
          <dl className="sp-profile-meta">
            <div>
              <dt>Tên đăng nhập</dt>
              <dd>{user?.name || '—'}</dd>
            </div>
            <div>
              <dt>Trạng thái</dt>
              <dd>
                <span className="sp-badge ok">Hoạt động</span>
              </dd>
            </div>
          </dl>
          <nav className="sp-profile-nav">
            <button
              className={tab === 'info' ? 'active' : ''}
              onClick={() => setTab('info')}
            >
              Thông tin cá nhân
            </button>
            <button
              className={tab === 'password' ? 'active' : ''}
              onClick={() => setTab('password')}
            >
              Đổi mật khẩu
            </button>
          </nav>
        </aside>

        <section className="sp-card sp-account-main">
          {tab === 'info' ? (
            <form onSubmit={saveInfo}>
              <div className="sp-section-head">
                <h2>Thông tin cá nhân</h2>
                <p>Thông tin hiển thị trên hệ thống BlueMoon.</p>
              </div>
              <div className="sp-form-grid">
                <label className="sp-field">
                  <span>Họ tên</span>
                  <input
                    className="sp-input"
                    name="name"
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                  />
                </label>
                <div className="sp-field">
                  <span>Tên đăng nhập</span>
                  <div className="sp-static">{user?.name || '—'}</div>
                </div>
                <div className="sp-field">
                  <span>Vai trò</span>
                  <div className="sp-static">{user?.role || '—'}</div>
                </div>
                <div className="sp-field">
                  <span>Email</span>
                  <div className="sp-static">{user?.email || '—'}</div>
                </div>
              </div>
              {infoError && <div className="sp-alert danger">{infoError}</div>}
              <div className="sp-form-actions">
                <button type="submit" className="sp-btn" disabled={saving}>
                  {saving ? 'Đang lưu…' : 'Lưu thay đổi'}
                </button>
              </div>
            </form>
          ) : (
            <form onSubmit={changePw}>
              <div className="sp-section-head">
                <h2>Đổi mật khẩu</h2>
                <p>Mật khẩu mới cần tối thiểu 8 ký tự.</p>
              </div>
              <div className="sp-form-grid">
                <label className="sp-field span-2">
                  <span>Mật khẩu hiện tại</span>
                  <input
                    className="sp-input"
                    type="password"
                    autoComplete="current-password"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                  />
                  {pwErrors.oldPassword && (
                    <span className="sp-field-error">{pwErrors.oldPassword}</span>
                  )}
                </label>
                <label className="sp-field">
                  <span>Mật khẩu mới</span>
                  <input
                    className="sp-input"
                    type="password"
                    autoComplete="new-password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                  />
                  {pwErrors.newPassword && (
                    <span className="sp-field-error">{pwErrors.newPassword}</span>
                  )}
                </label>
                <label className="sp-field">
                  <span>Xác nhận mật khẩu mới</span>
                  <input
                    className="sp-input"
                    type="password"
                    autoComplete="new-password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                  {pwErrors.confirmPassword && (
                    <span className="sp-field-error">{pwErrors.confirmPassword}</span>
                  )}
                </label>
              </div>
              {pwError && <div className="sp-alert danger">{pwError}</div>}
              <div className="sp-form-actions">
                <button type="submit" className="sp-btn" disabled={changing}>
                  {changing ? 'Đang đổi…' : 'Đổi mật khẩu'}
                </button>
              </div>
            </form>
          )}
        </section>
      </div>

      {toast && <div className="sp-toast">{toast}</div>}
    </>
  );
}