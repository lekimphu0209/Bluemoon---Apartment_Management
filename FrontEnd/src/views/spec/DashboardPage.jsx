import React, { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from 'services/apiConfig';
import { SpecUserContext } from './SpecLayout';
import './spec.css';

const fmtVnd = (n) =>
  `${Number(n || 0).toLocaleString('vi-VN', { maximumFractionDigits: 0 })} đ`;

export default function DashboardPage() {
  const { user } = useContext(SpecUserContext);
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    api
      .get('/dashboard/summary')
      .then((res) => setSummary(res.data))
      .catch(() => setError('Không tải được dữ liệu tổng quan.'));
  }, []);

  const items = [
    {
      value: summary ? summary.apartmentCount : '…',
      label: 'Căn hộ',
      note: 'Xem căn hộ →',
      to: '/app/can-ho',
    },
    {
      value: summary ? summary.residentCount : '…',
      label: 'Người dân',
      note: 'Xem người dân →',
      to: '/app/nguoi-dan',
    },
    {
      value: summary ? fmtVnd(summary.unpaidAmount) : '…',
      label: 'Chưa thu',
      note: 'Xem khoản chưa thu →',
      to: '/app/thu-phi?status=UNPAID',
    },
    {
      value: summary ? fmtVnd(summary.paidAmount) : '…',
      label: 'Đã thu',
      note: 'Xem khoản đã thu →',
      to: '/app/thu-phi?status=PAID',
    },
  ];

  return (
    <>
      <div className="sp-header">
        <h1>Xin chào, {user?.fullName || user?.name || '—'}</h1>
        <p>Tình hình chung cư hôm nay.</p>
      </div>
      {error && <div className="sp-alert danger">{error}</div>}
      <div className="sp-grid-4">
        {items.map((it) => (
          <Link key={it.label} to={it.to} className="sp-stat">
            <div className="v">{it.value}</div>
            <div className="l">{it.label}</div>
            <div className="n">{it.note}</div>
          </Link>
        ))}
      </div>
    </>
  );
}
