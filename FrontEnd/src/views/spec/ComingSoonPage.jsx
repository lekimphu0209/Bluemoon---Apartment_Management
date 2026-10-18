import React from 'react';
import './spec.css';

export default function ComingSoonPage({ title, description }) {
  return (
    <>
      <div className="sp-header">
        <h1>{title}</h1>
        <p>{description}</p>
      </div>
      <div className="sp-empty">
        <div className="t">Chức năng chưa hoạt động</div>
        <div className="d">Tính năng này đang được phát triển và sẽ sớm có mặt.</div>
      </div>
    </>
  );
}
