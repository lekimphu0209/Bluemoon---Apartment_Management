import React from 'react';
import './spec.css';

const TYPE_OPTIONS = [
  { value: 'SERVICE', label: 'Phí dịch vụ' },
  { value: 'MANAGEMENT', label: 'Phí quản lý' },
  { value: 'DONATION', label: 'Đóng góp' },
];

const STATUS_OPTIONS = [
  { value: 'ACTIVE', label: 'Đang áp dụng' },
  { value: 'INACTIVE', label: 'Ngừng áp dụng' },
];

export default function KhoanThuFormModal({ mode, feeType, existingNames, onClose, onSubmit }) {
  const [formData, setFormData] = React.useState({
    name: '',
    type: 'SERVICE',
    amount: '',
    status: 'ACTIVE',
  });
  const [error, setError] = React.useState('');
  const [submitting, setSubmitting] = React.useState(false);

  React.useEffect(() => {
    if (mode === 'view' && feeType) {
      setFormData({
        name: feeType.name || '',
        type: feeType.type || 'SERVICE',
        amount: feeType.amount || '',
        status: feeType.status || 'ACTIVE',
      });
    } else if (mode === 'create') {
      setFormData({ name: '', type: 'SERVICE', amount: '', status: 'ACTIVE' });
    }
    setError('');
  }, [mode, feeType]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (name === 'name' && existingNames?.includes(value)) {
      setError('Tên khoản thu đã tồn tại.');
    } else {
      setError('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (submitting) return;
    setError('');
    if (!formData.name.trim()) {
      setError('Vui lòng nhập tên khoản thu.');
      return;
    }
    if (existingNames?.includes(formData.name.trim())) {
      setError('Tên khoản thu đã tồn tại.');
      return;
    }
    setSubmitting(true);
    try {
      await onSubmit(formData);
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Không thể lưu khoản thu.');
    } finally {
      setSubmitting(false);
    }
  };

  if (mode === 'view') {
    return (
      <div className="sp-modal-backdrop" onClick={onClose}>
        <div className="sp-modal" onClick={(e) => e.stopPropagation()}>
          <div className="sp-modal-header">
            <h3>Chi tiết khoản thu</h3>
            <button className="sp-modal-close" onClick={onClose} aria-label="Đóng">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
              </svg>
            </button>
          </div>
          <div className="sp-modal-body">
            <div className="sp-form-grid">
              <label className="sp-field">
                <span>Tên khoản thu</span>
                <div className="sp-static">{feeType?.name || '—'}</div>
              </label>
              <label className="sp-field">
                <span>Loại</span>
                <div className="sp-static">{TYPE_OPTIONS.find(t => t.value === feeType?.type)?.label || feeType?.type || '—'}</div>
              </label>
              <label className="sp-field">
                <span>Mức thu</span>
                <div className="sp-static">{feeType?.amount || '—'}</div>
              </label>
              <label className="sp-field">
                <span>Trạng thái</span>
                <div className="sp-static">
                  <span className={`sp-badge ${feeType?.status === 'ACTIVE' ? 'ok' : 'muted'}`}>
                    {STATUS_OPTIONS.find(s => s.value === feeType?.status)?.label || feeType?.status || '—'}
                  </span>
                </div>
              </label>
              <label className="sp-field span-2">
                <span>Mô tả</span>
                <div className="sp-static">{feeType?.description || '—'}</div>
              </label>
              <label className="sp-field span-2">
                <span>Ngày áp dụng</span>
                <div className="sp-static">{feeType?.startDate ? new Date(feeType.startDate).toLocaleDateString('vi-VN') : '—'}
                  {feeType?.endDate && ` - ${new Date(feeType.endDate).toLocaleDateString('vi-VN')}`}
                </div>
              </label>
            </div>
          </div>
          <div className="sp-modal-footer">
            <button type="button" className="sp-btn" onClick={onClose}>Đóng</button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="sp-modal-backdrop" onClick={onClose}>
      <div className="sp-modal" onClick={(e) => e.stopPropagation()}>
        <div className="sp-modal-header">
          <h3>Thêm khoản thu</h3>
          <button className="sp-modal-close" onClick={onClose} aria-label="Đóng">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="sp-modal-body">
            {error && <div className="sp-alert danger">{error}</div>}
            <div className="sp-form-grid">
              <label className="sp-field">
                <span>Tên khoản thu <span className="req">*</span></span>
                <input
                  className="sp-input"
                  type="text"
                  name="name"
                  placeholder="VD: Phí dịch vụ tháng 06/2025"
                  value={formData.name}
                  onChange={handleChange}
                />
              </label>
              <label className="sp-field">
                <span>Loại <span className="req">*</span></span>
                <select className="sp-input" name="type" value={formData.type} onChange={handleChange}>
                  {TYPE_OPTIONS.map(t => <option key={t.value} value={t.value}>{t.label}</option>)}
                </select>
              </label>
              <label className="sp-field">
                <span>Mức thu <span className="req">*</span></span>
                <input
                  className="sp-input"
                  type="text"
                  name="amount"
                  placeholder="VD: 8.000 đ/m²/tháng hoặc Tự nguyện"
                  value={formData.amount}
                  onChange={handleChange}
                />
              </label>
              <label className="sp-field">
                <span>Trạng thái</span>
                <select className="sp-input" name="status" value={formData.status} onChange={handleChange}>
                  {STATUS_OPTIONS.map(s => <option key={s.value} value={s.value}>{s.label}</option>)}
                </select>
              </label>
            </div>
          </div>
          <div className="sp-modal-footer">
            <button type="button" className="sp-btn secondary" onClick={onClose}>Hủy</button>
            <button type="submit" className="sp-btn" disabled={submitting}>
              {submitting ? 'Đang lưu…' : 'Lưu khoản thu'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}