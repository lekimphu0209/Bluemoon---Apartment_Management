import React, { useContext, useEffect, useState, useCallback } from 'react';
import api from 'services/apiConfig';
import { SpecUserContext } from './SpecLayout';
import KhoanThuFormModal from './KhoanThuFormModal';
import './spec.css';

const TYPE_LABELS = { SERVICE: 'Phí dịch vụ', MANAGEMENT: 'Phí quản lý', DONATION: 'Đóng góp', PARKING_MOTORBIKE: 'Phí gửi xe máy', PARKING_CAR: 'Phí gửi xe ô tô' };
const STATUS_LABELS = { ACTIVE: 'Đang áp dụng', INACTIVE: 'Ngừng áp dụng' };
const STATUS_CLASSES = { ACTIVE: 'ok', INACTIVE: 'muted' };
const TYPE_FILTER_OPTIONS = ['ALL', 'SERVICE', 'MANAGEMENT', 'DONATION'];
const STATUS_FILTER_OPTIONS = ['ALL', 'ACTIVE', 'INACTIVE'];

export default function KhoanThuPage() {
  const [feeTypes, setFeeTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [typeFilter, setTypeFilter] = useState('ALL');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [selectedRows, setSelectedRows] = useState(new Set());
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [viewFeeType, setViewFeeType] = useState(null);

  const PAGE_SIZE = 10;

  const fetchFeeTypes = useCallback(async () => {
    setLoading(true); setError('');
    try {
      const params = new URLSearchParams();
      params.append('page', page);
      params.append('size', PAGE_SIZE);
      if (search.trim()) params.append('search', search.trim());
      if (typeFilter !== 'ALL') params.append('type', typeFilter);
      if (statusFilter !== 'ALL') params.append('status', statusFilter);

      const res = await api.get(`/api/fee-types?${params.toString()}`);
      const data = res.data;
      setFeeTypes(data.content || []);
      setTotalPages(data.totalPages || 1);
      setTotalElements(data.totalElements || 0);
      setSelectedRows(new Set());
    } catch (err) { setError('Không tải được danh sách khoản thu.'); console.error(err); }
    finally { setLoading(false); }
  }, [page, search, typeFilter, statusFilter]);

  useEffect(() => { fetchFeeTypes(); }, [fetchFeeTypes]);

  const handleSearchChange = (e) => { setSearch(e.target.value); setPage(0); };
  const handleTypeFilterChange = (type) => { setTypeFilter(type); setPage(0); };
  const handleStatusFilterChange = (status) => { setStatusFilter(status); setPage(0); };

  const handleRowSelect = (id, checked) => {
    const next = new Set(selectedRows);
    if (checked) next.add(id); else next.delete(id);
    setSelectedRows(next);
  };

  const handleSelectAll = (checked) => {
    if (checked) { setSelectedRows(new Set(feeTypes.map(f => f.id))); }
    else { setSelectedRows(new Set()); }
  };

  const isAllSelected = feeTypes.length > 0 && selectedRows.size === feeTypes.length;

  const openCreateModal = () => setShowCreateModal(true);
  const closeCreateModal = () => setShowCreateModal(false);

  const openViewModal = (ft) => { setViewFeeType(ft); setShowViewModal(true); };
  const closeViewModal = () => { setShowViewModal(false); setViewFeeType(null); };

  const existingNames = feeTypes.map(f => f.name);

  const handleCreateSubmit = async (data) => {
    await api.post('/api/fee-types', {
      name: data.name.trim(),
      type: data.type,
      amount: data.amount,
      status: data.status,
    });
    fetchFeeTypes();
  };

  const handleToggleStatus = async (ft) => {
    const newStatus = ft.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    const label = newStatus === 'ACTIVE' ? 'Đang áp dụng' : 'Ngừng áp dụng';
    if (!window.confirm(`Chuyển khoản thu "${ft.name}" sang ${label}?`)) return;
    try {
      await api.put(`/api/fee-types/${ft.id}`, { status: newStatus });
      fetchFeeTypes();
    } catch (err) {
      window.alert(err.response?.data?.message || 'Không thể thay đổi trạng thái.');
    }
  };

  return (
    <>
      <div className="sp-header"><h1>Khoản thu</h1><p>Quản lý các khoản thu và đóng góp.</p></div>
      {error && <div className="sp-alert danger">{error}</div>}
      <div className="sp-card sp-card-toolbar">
        <div className="sp-toolbar">
          <div className="sp-toolbar-left">
            <button className="sp-btn" onClick={openCreateModal}><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg> Thêm khoản thu</button>
          </div>
          <div className="sp-toolbar-right">
            <div className="sp-search" style={{ width: 320 }}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
              <input type="text" className="sp-input" placeholder="Tìm theo tên khoản thu..." value={search} onChange={handleSearchChange} />
            </div>
            <div className="sp-tabs" role="tablist" aria-label="Lọc loại">
              {TYPE_FILTER_OPTIONS.map(t => (
                <button key={t} role="tab" aria-selected={typeFilter === t} className={`sp-tab ${typeFilter === t ? 'active' : ''}`} onClick={() => handleTypeFilterChange(t)}>
                  {t === 'ALL' ? 'Tất cả' : TYPE_LABELS[t]}
                </button>
              ))}
            </div>
            <div className="sp-tabs" role="tablist" aria-label="Lọc trạng thái">
              {STATUS_FILTER_OPTIONS.map(s => (
                <button key={s} role="tab" aria-selected={statusFilter === s} className={`sp-tab ${statusFilter === s ? 'active' : ''}`} onClick={() => handleStatusFilterChange(s)}>
                  {s === 'ALL' ? 'Tất cả' : STATUS_LABELS[s]}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
      <div className="sp-card">
        {loading ? <div className="sp-table-loading">Đang tải…</div> : feeTypes.length === 0 ? <div className="sp-empty"><div className="t">Không có khoản thu</div></div> : (
          <>
          <div className="sp-table-wrapper">
            <table className="sp-table">
              <thead>
                <tr>
                  <th style={{width:48}}><input type="checkbox" checked={isAllSelected} onChange={e=>handleSelectAll(e.target.checked)} aria-label="Chọn tất cả"/></th>
                  <th>TÊN KHOẢN THU</th>
                  <th>LOẠI</th>
                  <th>MỨC THU</th>
                  <th>TRẠNG THÁI</th>
                  <th style={{width:140,textAlign:'right'}}>THAO TÁC</th>
                </tr>
              </thead>
              <tbody>{feeTypes.map(ft => <tr key={ft.id}><td><input type="checkbox" checked={selectedRows.has(ft.id)} onChange={e=>handleRowSelect(ft.id,e.target.checked)}/></td><td><span className="sp-apt-number">{ft.name}</span></td><td>{TYPE_LABELS[ft.type]||ft.type}</td><td>{ft.amount || (ft.rate?ft.rate.toLocaleString()+'đ/m²':ft.suggestedAmount?ft.suggestedAmount.toLocaleString()+'đ':'—')}</td><td><span className={`sp-badge ${STATUS_CLASSES[ft.status]||'muted'}`}>{STATUS_LABELS[ft.status]||ft.status}</span></td><td style={{textAlign:'right'}}><div className="sp-actions"><button className="sp-btn-icon" title="Xem" onClick={()=>openViewModal(ft)}><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg></button><button className="sp-btn-icon" title={ft.status==='INACTIVE'?'Áp dụng lại':'Ngừng áp dụng'} onClick={()=>handleToggleStatus(ft)}><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M7 16l-4-4 4-4"/><path d="M17 8l4 4-4 4"/><path d="M17 16H7"/><path d="M7 8h10"/></svg></button></div></td></tr>)}</tbody>
            </table>
          </div>
            {totalPages > 1 && (
              <div className="sp-pagination">
                <button className="sp-btn secondary" disabled={page === 0} onClick={() => setPage(p => Math.max(0, p - 1))}>Trước</button>
                <span className="sp-page-info">Trang {page + 1} / {totalPages} — Tổng {totalElements} khoản thu</span>
                <button className="sp-btn secondary" disabled={page >= totalPages - 1} onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}>Sau</button>
              </div>
            )}
          </>
        )}
      </div>
      {showCreateModal && <KhoanThuFormModal mode="create" existingNames={existingNames} onClose={closeCreateModal} onSubmit={handleCreateSubmit} />}
      {showViewModal && <KhoanThuFormModal mode="view" feeType={viewFeeType} onClose={closeViewModal} />}
    </>
  );
}