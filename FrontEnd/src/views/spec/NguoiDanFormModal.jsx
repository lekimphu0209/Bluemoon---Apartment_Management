import React from 'react';
import './spec.css';

const RELATION_OPTIONS = [
  { value: 'HEAD', label: 'Chủ hộ' },
  { value: 'SPOUSE', label: 'Vợ/Chồng' },
  { value: 'CHILD', label: 'Con' },
  { value: 'PARENT', label: 'Cha/Mẹ' },
  { value: 'OTHER', label: 'Khác' },
];

const RESIDENCY_OPTIONS = [
  { value: 'LIVING', label: 'Thường trú' },
  { value: 'TEMP_STAY', label: 'Tạm trú' },
  { value: 'TEMP_AWAY', label: 'Tạm vắng' },
  { value: 'MOVED_OUT', label: 'Đã chuyển đi' },
];

const BADGE_CLASS = { LIVING: 'ok', TEMP_STAY: 'warn', TEMP_AWAY: 'warn', MOVED_OUT: 'muted' };
const phoneRegex = /^(0[3|5|7|8|9])+([0-9]{8})$/;

export default function NguoiDanFormModal({ mode, resident, existingPhones, onClose, onSubmit }) {
  const [formData, setFormData] = React.useState({
    fullName: '', phone: '', idCard: '', apartment: '',
    relation: 'HEAD', residencyStatus: 'LIVING',
  });
  const [error, setError] = React.useState('');
  const [submitting, setSubmitting] = React.useState(false);

  React.useEffect(() => {
    if (mode === 'view' && resident) {
      setFormData({
        fullName: resident.fullName || '', phone: resident.phone || '',
        idCard: resident.idCard || '', apartment: resident.apartment || '',
        relation: resident.relation || 'HEAD', residencyStatus: resident.residencyStatus || 'LIVING',
      });
    } else if (mode === 'create') {
      setFormData({ fullName: '', phone: '', idCard: '', apartment: '', relation: 'HEAD', residencyStatus: 'LIVING' });
    }
    setError('');
  }, [mode, resident]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (name === 'phone' && existingPhones?.includes(value)) setError('Số điện thoại đã tồn tại.');
    else setError('');
  };

  const validate = () => {
    if (!formData.fullName.trim()) { setError('Vui lòng nhập họ tên.'); return false; }
    if (!formData.phone.trim()) { setError('Vui lòng nhập số điện thoại.'); return false; }
    if (!phoneRegex.test(formData.phone)) { setError('Số điện thoại không đúng định dạng (VD: 0901234567).'); return false; }
    if (existingPhones?.includes(formData.phone.trim())) { setError('Số điện thoại đã tồn tại.'); return false; }
    if (!formData.apartment.trim()) { setError('Vui lòng chọn căn hộ.'); return false; }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (submitting) return;
    setError('');
    if (!validate()) return;
    setSubmitting(true);
    try { await onSubmit(formData); onClose(); }
    catch (err) { setError(err.response?.data?.message || 'Không thể lưu người dân.'); }
    finally { setSubmitting(false); }
  };

  const renderView = () => (
    <div className="sp-modal-backdrop" onClick={onClose}>
      <div className="sp-modal" onClick={(e) => e.stopPropagation()}>
        <div className="sp-modal-header">
          <h3>Chi tiết người dân</h3>
          <button className="sp-modal-close" onClick={onClose} aria-label="Đóng">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>
        <div className="sp-modal-body">
          <div className="sp-form-grid">
            <label className="sp-field"><span>Họ tên</span><div className="sp-static">{resident?.fullName || '—'}</div></label>
            <label className="sp-field"><span>Số điện thoại</span><div className="sp-static">{resident?.phone || '—'}</div></label>
            <label className="sp-field"><span>CCCD</span><div className="sp-static">{resident?.idCard || '—'}</div></label>
            <label className="sp-field"><span>Căn hộ</span><div className="sp-static">{resident?.apartment || '—'}</div></label>
            <label className="sp-field"><span>Quan hệ</span><div className="sp-static">{RELATION_OPTIONS.find(o => o.value === resident?.relation)?.label || resident?.relation || '—'}</div></label>
            <label className="sp-field"><span>Cư trú</span><div className="sp-static"><span className={`sp-badge ${BADGE_CLASS[resident?.residencyStatus] || 'muted'}`}>{RESIDENCY_OPTIONS.find(o => o.value === resident?.residencyStatus)?.label || resident?.residencyStatus || '—'}</span></div></label>
          </div>
        </div>
        <div className="sp-modal-footer"><button type="button" className="sp-btn" onClick={onClose}>Đóng</button></div>
      </div>
    </div>
  );

  const renderCreate = () => (
    <div className="sp-modal-backdrop" onClick={onClose}>
      <div className="sp-modal" onClick={(e) => e.stopPropagation()}>
        <div className="sp-modal-header">
          <h3>Thêm người dân</h3>
          <button className="sp-modal-close" onClick={onClose} aria-label="Đóng">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="sp-modal-body">
            {error && <div className="sp-alert danger">{error}</div>}
            <div className="sp-form-grid">
              <label className="sp-field"><span>Họ tên <span className="req">*</span></span><input className="sp-input" type="text" name="fullName" placeholder="VD: Nguyễn Văn An" value={formData.fullName} onChange={handleChange} /></label>
              <label className="sp-field"><span>Số điện thoại <span className="req">*</span></span><input className="sp-input" type="tel" name="phone" placeholder="VD: 0901234567" value={formData.phone} onChange={handleChange} /></label>
              <label className="sp-field"><span>CCCD</span><input className="sp-input" type="text" name="idCard" placeholder="VD: 001200001234" value={formData.idCard} onChange={handleChange} /></label>
              <label className="sp-field"><span>Căn hộ <span className="req">*</span></span><input className="sp-input" type="text" name="apartment" placeholder="VD: A-1201" value={formData.apartment} onChange={handleChange} /></label>
              <label className="sp-field"><span>Quan hệ <span className="req">*</span></span><select className="sp-input" name="relation" value={formData.relation} onChange={handleChange}>{RELATION_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}</select></label>
              <label className="sp-field"><span>Cư trú</span><select className="sp-input" name="residencyStatus" value={formData.residencyStatus} onChange={handleChange}>{RESIDENCY_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}</select></label>
            </div>
          </div>
          <div className="sp-modal-footer">
            <button type="button" className="sp-btn secondary" onClick={onClose}>Hủy</button>
            <button type="submit" className="sp-btn" disabled={submitting}>{submitting ? 'Đang lưu…' : 'Lưu người dân'}</button>
          </div>
        </form>
      </div>
    </div>
  );

  return mode === 'view' ? renderView() : renderCreate();
}