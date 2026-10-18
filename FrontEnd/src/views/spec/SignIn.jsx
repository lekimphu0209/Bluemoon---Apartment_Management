import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from 'services/apiConfig';
import './spec.css';

export default function SignIn() {
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [showPw, setShowPw] = useState(false);
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (localStorage.getItem('token')) navigate('/app/tong-quan', { replace: true });
  }, [navigate]);

  const submit = async (e) => {
    e.preventDefault();
    if (submitting) return;
    const errs = {};
    if (!name.trim()) errs.name = 'Vui lòng nhập tên đăng nhập.';
    if (!password) errs.password = 'Vui lòng nhập mật khẩu.';
    setFieldErrors(errs);
    setError('');
    if (Object.keys(errs).length) return;

    setSubmitting(true);
    try {
      const res = await api.post('/auth/login', { name: name.trim(), password });
      const token = String(res.data).replace(/^"|"$/g, '');
      localStorage.setItem('token', token);
      navigate('/app/tong-quan', { replace: true });
    } catch (err) {
      if (err.response?.data?.errors) setFieldErrors(err.response.data.errors);
      setError(
        err.response?.data?.message ||
          'Tên đăng nhập hoặc mật khẩu không đúng.',
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="spec-root">
      <main className="sp-auth">
        <div className="sp-auth-card">
          <div className="mb-7" style={{ marginBottom: 28 }}>
            <div className="sp-auth-badge">B</div>
            <h1>BlueMoon</h1>
            <p className="sub">Quản lý chung cư</p>
          </div>

          <div className="sp-alert info">
            <div style={{ fontWeight: 600 }}>Tài khoản kiểm thử nhanh</div>
            <div style={{ marginTop: 4 }}>Tên đăng nhập: admin</div>
            <div>Mật khẩu: 1234567890</div>
          </div>

          <form onSubmit={submit}>
            <label className="sp-field">
              <span>
                Tên đăng nhập <span className="req">*</span>
              </span>
              <input
                className="sp-input"
                name="name"
                autoComplete="username"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              {fieldErrors.name && <span className="sp-field-error">{fieldErrors.name}</span>}
            </label>

            <label className="sp-field">
              <span>
                Mật khẩu <span className="req">*</span>
              </span>
              <div className="sp-pw-wrap">
                <input
                  className="sp-input"
                  style={{ paddingRight: 64 }}
                  name="password"
                  type={showPw ? 'text' : 'password'}
                  autoComplete="current-password"
                  placeholder="Nhập mật khẩu"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <button
                  type="button"
                  className="sp-pw-toggle"
                  onClick={() => setShowPw((v) => !v)}
                >
                  {showPw ? 'Ẩn' : 'Hiện'}
                </button>
              </div>
              {fieldErrors.password && (
                <span className="sp-field-error">{fieldErrors.password}</span>
              )}
            </label>

            {error && (
              <div role="alert" className="sp-alert danger">
                {error}
              </div>
            )}

            <button type="submit" className="sp-btn full" disabled={submitting}>
              {submitting ? 'Đang đăng nhập…' : 'Đăng nhập'}
            </button>
          </form>

          <div className="sp-auth-foot">
            Tài khoản được cấp bởi Ban quản trị BlueMoon
          </div>
        </div>
      </main>
    </div>
  );
}
