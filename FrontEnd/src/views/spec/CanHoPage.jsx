import React, { useContext, useEffect, useState, useCallback } from 'react';
import api from 'services/apiConfig';
import { SpecUserContext } from './SpecLayout';
import './spec.css';

const STATUS_LABELS = {
  OCCUPIED: 'Đang ở',
  EMPTY: 'Trống',
  RENT: 'Cho thuê',
};

const STATUS_CLASSES = {
  OCCUPIED: 'ok',
  EMPTY: 'muted',
  RENT: 'warn',
};

export default function CanHoPage() {
  const [apartments, setApartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [selectedRows, setSelectedRows] = useState(new Set());
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState('');
  const [formData, setFormData] = useState({ apartmentNumber: '', floor: '', area: '' });

  const PAGE_SIZE = 10;

  const fetchApartments = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const params = new URLSearchParams();
      params.append('page', page);
      params.append('size', PAGE_SIZE);
      if (search.trim()) params.append('search', search.trim());
      if (statusFilter !== 'ALL') params.append('status', statusFilter);

      const res = await api.get(`/api/apartments?${params.toString()}`);
      const data = res.data;
      setApartments(data.content || []);
      setTotalPages(data.totalPages || 1);
      setTotalElements(data.totalElements || 0);
      setSelectedRows(new Set());
    } catch (err) {
      setError('Không tải được danh sách căn hộ.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [page, search, statusFilter]);

  useEffect(() => {
    fetchApartments();
  }, [fetchApartments]);

  const handleSearchChange = (e) => {
    setSearch(e.target.value);
    setPage(0);
  };

  const handleStatusFilterChange = (status) => {
    setStatusFilter(status);
    setPage(0);
  };

  const handleRowSelect = (id, checked) => {
    const next = new Set(selectedRows);
    if (checked) next.add(id); else next.delete(id);
    setSelectedRows(next);
  };

  const handleSelectAll = (checked) => {
    if (checked) {
      setSelectedRows(new Set(apartments.map(a => a.id)));
    } else {
      setSelectedRows(new Set());
    }
  };

  const openCreateModal = () => {
    setFormData({ apartmentNumber: '', floor: '', area: '' });
    setCreateError('');
    setShowCreateModal(true);
  };

  const closeCreateModal = () => {
    setShowCreateModal(false);
    setCreateError('');
  };

  const handleCreateSubmit = async (e) => {
    e.preventDefault();
    if (creating) return;
    setCreateError('');
    if (!formData.apartmentNumber.trim()) {
      setCreateError('Vui lòng nhập số căn hộ.');
      return;
    }
    if (!formData.floor) {
      setCreateError('Vui lòng nhập tầng.');
      return;
    }
    if (!formData.area) {
      setCreateError('Vui lòng nhập diện tích.');
      return;
    }
    setCreating(true);
    try {
      await api.post('/api/apartments', {
        apartmentNumber: formData.apartmentNumber.trim(),
        floor: Number(formData.floor),
        area: Number(formData.area),
      });
      closeCreateModal();
      fetchApartments();
    } catch (err) {
      const msg = err.response?.data?.message || 'Không thể tạo căn hộ.';
      if (err.response?.data?.errors) {
        const firstErr = Object.values(err.response.data.errors)[0];
        setCreateError(firstErr);
      } else {
        setCreateError(msg);
      }
    } finally {
      setCreating(false);
    }
  };

  const handleView = (apartment) => {
    // Navigate to detail page - for now just alert
    alert(`Xem chi tiết căn hộ ${apartment.apartmentNumber} (chưa triển khai)`);
  };

  const handleChangeStatus = async (apartment, newStatus) => {
    if (!confirm(`Chuyển căn hộ ${apartment.apartmentNumber} sang trạng thái ${STATUS_LABELS[newStatus]}?`)) return;
    try {
      // For now, use the existing batch-update-status endpoint
      await api.post('/api/apartments/batch-update-status', {
        ids: [apartment.id],
        status: newStatus,
      });
      fetchApartments();
    } catch (err) {
      alert(err.response?.data?.message || 'Không thể thay đổi trạng thái.');
    }
  };

  const isAllSelected = apartments.length > 0 && selectedRows.size === apartments.length;

  const filteredApartments = apartments;

  return (
    <>
      <div className="sp-header">
        <h1>Căn hộ</h1>
        <p>Quản lý căn hộ và hộ gia đình đang cư trú.</p>
      </div>

      {error && <div className="sp-alert danger">{error}</div>}

      <div className="sp-card sp-card-toolbar">
        <div className="sp-toolbar">
          <div className="sp-toolbar-left">
            <button className="sp-btn" onClick={openCreateModal}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
              </svg>
              Thêm căn hộ
            </button>
          </div>
          <div className="sp-toolbar-right">
            <div className="sp-search">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
              <input
                type="text"
                className="sp-input"
                placeholder="Tìm theo số căn hộ hoặc chủ hộ..."
                value={search}
                onChange={handleSearchChange}
              />
            </div>
            <div className="sp-tabs" role="tablist" aria-label="Lọc trạng thái">
              {['ALL', 'OCCUPIED', 'EMPTY'].map((s) => (
                <button
                  key={s}
                  role="tab"
                  aria-selected={statusFilter === s}
                  className={`sp-tab ${statusFilter === s ? 'active' : ''}`}
                  onClick={() => handleStatusFilterChange(s)}
                >
                  {s === 'ALL' ? 'Tất cả' : s === 'OCCUPIED' ? 'Đang ở' : 'Trống'}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="sp-card">
        {loading ? (
          <div className="sp-table-loading">Đang tải…</div>
        ) : filteredApartments.length === 0 ? (
          <div className="sp-empty">
            <div className="t">Không có căn hộ</div>
            <div className="d">Thử điều chỉnh bộ lọc hoặc tìm kiếm khác.</div>
          </div>
        ) : (
          <>
            <div className="sp-table-wrapper">
              <table className="sp-table">
                <thead>
                  <tr>
                    <th style={{ width: 48 }}>
                      <input
                        type="checkbox"
                        checked={isAllSelected}
                        onChange={(e) => handleSelectAll(e.target.checked)}
                        aria-label="Chọn tất cả"
                      />
                    </th>
                    <th>SỐ CĂN HỘ</th>
                    <th>TẦNG</th>
                    <th>CHỦ HỘ</th>
                    <th>TRẠNG THÁI</th>
                    <th style={{ width: 140, textAlign: 'right' }}>THAO TÁC</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredApartments.map((apt) => (
                    <tr key={apt.id}>
                      <td>
                        <input
                          type="checkbox"
                          checked={selectedRows.has(apt.id)}
                          onChange={(e) => handleRowSelect(apt.id, e.target.checked)}
                        />
                      </td>
                      <td>
                        <span className="sp-apt-number">{apt.apartmentNumber}</span>
                      </td>
                      <td>{apt.floor ?? '—'}</td>
                      <td>
                        {apt.headName ? (
                          <span className="sp-head-name">{apt.headName}</span>
                        ) : (
                          <span className="sp-muted">—</span>
                        )}
                      </td>
                      <td>
                        <span className={`sp-badge ${STATUS_CLASSES[apt.status] || 'muted'}`}>
                          {STATUS_LABELS[apt.status] || apt.status}
                        </span>
                      </td>
                      <td style={{ textAlign: 'right' }}>
                        <div className="sp-actions">
                          <button
                            className="sp-btn-icon"
                            title="Xem"
                            onClick={() => handleView(apt)}
                          >
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                              <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                              <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                          </button>
                          <button
                            className="sp-btn-icon"
                            title={apt.status === 'EMPTY' ? 'Chuyển sang Đang ở' : 'Chuyển sang Trống'}
                            onClick={() => handleChangeStatus(apt, apt.status === 'EMPTY' ? 'OCCUPIED' : 'EMPTY')}
                          >
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                              <path d="M7 16l-4-4 4-4"></path>
                              <path d="M17 8l4 4-4 4"></path>
                              <path d="M17 16H7"></path>
                              <path d="M7 8h10"></path>
                            </svg>
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            {totalPages > 1 && (
              <div className="sp-pagination">
                <button
                  className="sp-btn secondary"
                  disabled={page === 0}
                  onClick={() => setPage(p => Math.max(0, p - 1))}
                >
                  Trước
                </button>
                <span className="sp-page-info">
                  Trang {page + 1} / {totalPages} — Tổng {totalElements} căn hộ
                </span>
                <button
                  className="sp-btn secondary"
                  disabled={page >= totalPages - 1}
                  onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                >
                  Sau
                </button>
              </div>
            )}
          </>
        )}
      </div>

      {/* Create Modal */}
      {showCreateModal && (
        <div className="sp-modal-backdrop" onClick={closeCreateModal}>
          <div className="sp-modal" onClick={(e) => e.stopPropagation()}>
            <div className="sp-modal-header">
              <h3>Thêm căn hộ</h3>
              <button className="sp-modal-close" onClick={closeCreateModal} aria-label="Đóng">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
              </button>
            </div>
            <form onSubmit={handleCreateSubmit}>
              <div className="sp-modal-body">
                {createError && <div className="sp-alert danger">{createError}</div>}
                <div className="sp-form-grid">
                  <label className="sp-field">
                    <span>Số căn hộ <span className="req">*</span></span>
                    <input
                      className="sp-input"
                      type="text"
                      placeholder="VD: A-1201"
                      value={formData.apartmentNumber}
                      onChange={(e) => setFormData({ ...formData, apartmentNumber: e.target.value })}
                    />
                  </label>
                  <label className="sp-field">
                    <span>Tầng <span className="req">*</span></span>
                    <input
                      className="sp-input"
                      type="number"
                      min="1"
                      placeholder="VD: 12"
                      value={formData.floor}
                      onChange={(e) => setFormData({ ...formData, floor: e.target.value })}
                    />
                  </label>
                  <label className="sp-field">
                    <span>Diện tích (m²) <span className="req">*</span></span>
                    <input
                      className="sp-input"
                      type="number"
                      min="1"
                      step="0.1"
                      placeholder="VD: 85.5"
                      value={formData.area}
                      onChange={(e) => setFormData({ ...formData, area: e.target.value })}
                    />
                  </label>
                </div>
              </div>
              <div className="sp-modal-footer">
                <button type="button" className="sp-btn secondary" onClick={closeCreateModal}>
                  Hủy
                </button>
                <button type="submit" className="sp-btn" disabled={creating}>
                  {creating ? 'Đang tạo…' : 'Tạo căn hộ'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}