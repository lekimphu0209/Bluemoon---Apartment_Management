import React, { useEffect, useState, useCallback } from 'react';
import api from 'services/apiConfig';
import NguoiDanFormModal from './NguoiDanFormModal';
import './spec.css';

const RESIDENCY_LABELS = { LIVING: 'Đang ở', TEMP_AWAY: 'Tạm vắng', TEMP_STAY: 'Tạm trú', MOVED_OUT: 'Đã chuyển đi' };
const RESIDENCY_CLASSES = { LIVING: 'ok', TEMP_AWAY: 'warn', TEMP_STAY: 'warn', MOVED_OUT: 'muted' };
const RELATION_LABELS = { HEAD: 'Chủ hộ', SPOUSE: 'Vợ/Chồng', CHILD: 'Con', PARENT: 'Cha/Mẹ', OTHER: 'Khác' };
const RESIDENCY_FILTER_OPTIONS = ['ALL', 'LIVING', 'TEMP_STAY', 'TEMP_AWAY', 'MOVED_OUT'];

export default function NguoiDanPage() {
  const [residents, setResidents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [residencyFilter, setResidencyFilter] = useState('ALL');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [selectedRows, setSelectedRows] = useState(new Set());
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [viewResident, setViewResident] = useState(null);

  const PAGE_SIZE = 10;

  const fetchResidents = useCallback(async () => {
    setLoading(true); setError('');
    try {
      const params = new URLSearchParams();
      params.append('page', page);
      params.append('size', PAGE_SIZE);
      if (search.trim()) params.append('search', search.trim());
      if (residencyFilter !== 'ALL') params.append('residencyStatus', residencyFilter);

      const res = await api.get(`/api/residents?${params.toString()}`);
      const data = res.data;
      setResidents(data.content || []);
      setTotalPages(data.totalPages || 1);
      setTotalElements(data.totalElements || 0);
      setSelectedRows(new Set());
    } catch (err) { setError('Không tải được danh sách người dân.'); console.error(err); }
    finally { setLoading(false); }
  }, [page, search, residencyFilter]);

  useEffect(() => { fetchResidents(); }, [fetchResidents]);

  const handleSearchChange = (e) => { setSearch(e.target.value); setPage(0); };
  const handleResidencyFilterChange = (status) => { setResidencyFilter(status); setPage(0); };

  const handleRowSelect = (id, checked) => {
    const next = new Set(selectedRows);
    if (checked) next.add(id); else next.delete(id);
    setSelectedRows(next);
  };

  const handleSelectAll = (checked) => {
    if (checked) { setSelectedRows(new Set(residents.map(r => r.id))); }
    else { setSelectedRows(new Set()); }
  };

  const isAllSelected = residents.length > 0 && selectedRows.size === residents.length;

  const handleMoveOut = async (resident) => {
    if (!window.confirm(`Xác nhận chuyển "${resident.fullName}" sang trạng thái "Đã chuyển đi"?`)) return;
    try {
      await api.put(`/api/residents/${resident.id}`, { residencyStatus: 'MOVED_OUT' });
      fetchResidents();
    } catch (err) {
      window.alert(err.response?.data?.message || 'Không thể thay đổi trạng thái.');
    }
  };

  const openCreateModal = () => setShowCreateModal(true);
  const closeCreateModal = () => setShowCreateModal(false);
  const openViewModal = (r) => { setViewResident(r); setShowViewModal(true); };
  const closeViewModal = () => { setShowViewModal(false); setViewResident(null); };

  const existingPhones = residents.map(r => r.phone);

  const handleCreateSubmit = async (data) => {
    await api.post('/api/residents', {
      fullName: data.fullName,
      phone: data.phone,
      idCard: data.idCard,
      apartment: data.apartment,
      relation: data.relation,
      residencyStatus: data.residencyStatus,
    });
    fetchResidents();
  };

  return (
    <>
      <div className="sp-header"><h1>Người dân</h1><p>Quản lý nhân khẩu và thông tin cư trú.</p></div>
      {error && <div className="sp-alert danger">{error}</div>}
      <div className="sp-card sp-card-toolbar">
        <div className="sp-toolbar">
          <div className="sp-toolbar-left">
            <button className="sp-btn" onClick={openCreateModal}><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg> Thêm người dân</button>
          </div>
          <div className="sp-toolbar-right">
            <div className="sp-search" style={{ width: 360 }}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
              <input type="text" className="sp-input" placeholder="Tìm theo tên, CCCD hoặc số điện thoại..." value={search} onChange={handleSearchChange} />
            </div>
            <div className="sp-tabs" role="tablist" aria-label="Lọc trạng thái cư trú">
              {RESIDENCY_FILTER_OPTIONS.map(s => (
                <button key={s} role="tab" aria-selected={residencyFilter === s} className={`sp-tab ${residencyFilter === s ? 'active' : ''}`} onClick={() => handleResidencyFilterChange(s)}>
                  {s === 'ALL' ? 'Tất cả' : RESIDENCY_LABELS[s]}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
      <div className="sp-card">
        {loading ? <div className="sp-table-loading">Đang tải…</div> : residents.length === 0 ? <div className="sp-empty"><div className="t">Không có người dân</div></div> : (
          <>
            <div className="sp-table-wrapper">
              <table className="sp-table">
                <thead>
                  <tr>
                    <th style={{width:48}}><input type="checkbox" checked={isAllSelected} onChange={e=>handleSelectAll(e.target.checked)} aria-label="Chọn tất cả"/></th>
                    <th>HỌ TÊN</th>
                    <th>SỐ ĐIỆN THOẠI</th>
                    <th>CĂN HỘ</th>
                    <th>QUAN HỆ</th>
                    <th>CƯ TRÚ</th>
                    <th style={{width:140,textAlign:'right'}}>THAO TÁC</th>
                  </tr>
                </thead>
                <tbody>{residents.map(r => <tr key={r.id}><td><input type="checkbox" checked={selectedRows.has(r.id)} onChange={e=>handleRowSelect(r.id,e.target.checked)}/></td><td><span className="sp-apt-number">{r.fullName}</span></td><td>{r.phone || '—'}</td><td>{r.apartment}</td><td>{RELATION_LABELS[r.relation] || r.relation}</td><td><span className={`sp-badge ${RESIDENCY_CLASSES[r.residencyStatus] || 'muted'}`}>{RESIDENCY_LABELS[r.residencyStatus] || r.residencyStatus}</span></td><td style={{textAlign:'right'}}><div className="sp-actions"><button className="sp-btn-icon" title="Xem" onClick={()=>openViewModal(r)}><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg></button><button className="sp-btn-icon" title="Chuyển đi" onClick={()=>handleMoveOut(r)}><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M7 16l-4-4 4-4"/><path d="M17 8l4 4-4 4"/><path d="M17 16H7"/><path d="M7 8h10"/></svg></button></div></td></tr>)}</tbody>
              </table>
            </div>
            {totalPages > 1 && (
              <div className="sp-pagination">
                <button className="sp-btn secondary" disabled={page === 0} onClick={() => setPage(p => Math.max(0, p - 1))}>Trước</button>
                <span className="sp-page-info">Trang {page + 1} / {totalPages} — Tổng {totalElements} người</span>
                <button className="sp-btn secondary" disabled={page >= totalPages - 1} onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}>Sau</button>
              </div>
            )}
          </>
        )}
      </div>
      {showCreateModal && <NguoiDanFormModal mode="create" existingPhones={existingPhones} onClose={closeCreateModal} onSubmit={handleCreateSubmit} />}
      {showViewModal && <NguoiDanFormModal mode="view" resident={viewResident} onClose={closeViewModal} />}
    </>
  );
}