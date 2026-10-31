// Mock API Service - dùng cho dev khi chưa có backend
// Kích hoạt: thêm REACT_APP_USE_MOCK_API=true vào .env.local hoặc set trong console: localStorage.setItem('USE_MOCK_API', 'true')

const DELAY = 300; // giả lập độ trễ network

// Tạo JWT token giả (header.payload.signature)
function createMockToken(user) {
  const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
  const payload = btoa(JSON.stringify({
    sub: user.name,
    name: user.name,
    fullName: user.fullName,
    email: user.email,
    phone: user.phone,
    role: 'ROLE_ADMIN',
    iat: Math.floor(Date.now() / 1000),
    exp: Math.floor(Date.now() / 1000) + 3600,
  }));
  const signature = btoa('mock-signature');
  return `${header}.${payload}.${signature}`;
}

// Mock user
const MOCK_USER = {
  id: 1,
  name: 'admin',
  fullName: 'Nguyen Hoang',
  email: 'admin@bluemoon.vn',
  phone: '0123456789',
  role: 'ROLE_ADMIN',
};

// Dữ liệu mẫu
const MOCK_APARTMENTS = [
  { id: 1, apartmentNumber: 'A-1201', floor: 12, area: 85.5, status: 'OCCUPIED', headName: 'Nguyễn Minh Anh', residentCount: 3, createdAt: '2024-01-15T08:00:00', updatedAt: '2024-01-15T08:00:00' },
  { id: 2, apartmentNumber: 'A-1004', floor: 10, area: 78.0, status: 'OCCUPIED', headName: 'Lê Thu Hà', residentCount: 2, createdAt: '2024-01-10T08:00:00', updatedAt: '2024-01-10T08:00:00' },
  { id: 3, apartmentNumber: 'B-0812', floor: 8, area: 92.0, status: 'EMPTY', headName: null, residentCount: 0, createdAt: '2024-02-01T08:00:00', updatedAt: '2024-02-01T08:00:00' },
  { id: 4, apartmentNumber: 'B-0605', floor: 6, area: 65.5, status: 'OCCUPIED', headName: 'Phạm Quang Huy', residentCount: 4, createdAt: '2024-01-20T08:00:00', updatedAt: '2024-01-20T08:00:00' },
  { id: 5, apartmentNumber: 'A-0302', floor: 3, area: 72.0, status: 'OCCUPIED', headName: 'Đỗ Lan Phương', residentCount: 2, createdAt: '2024-01-12T08:00:00', updatedAt: '2024-01-12T08:00:00' },
  { id: 6, apartmentNumber: 'A-1506', floor: 15, area: 110.0, status: 'OCCUPIED', headName: 'Vũ Đức Long', residentCount: 3, createdAt: '2024-03-01T08:00:00', updatedAt: '2024-03-01T08:00:00' },
  { id: 7, apartmentNumber: 'B-0203', floor: 2, area: 68.0, status: 'EMPTY', headName: null, residentCount: 0, createdAt: '2024-02-15T08:00:00', updatedAt: '2024-02-15T08:00:00' },
  { id: 8, apartmentNumber: 'A-0501', floor: 5, area: 80.0, status: 'RENT', headName: 'Trần Văn Nam', residentCount: 1, createdAt: '2024-02-10T08:00:00', updatedAt: '2024-02-10T08:00:00' },
  { id: 9, apartmentNumber: 'B-0704', floor: 7, area: 88.5, status: 'OCCUPIED', headName: 'Hoàng Thị Mai', residentCount: 2, createdAt: '2024-01-25T08:00:00', updatedAt: '2024-01-25T08:00:00' },
  { id: 10, apartmentNumber: 'A-1102', floor: 11, area: 95.0, status: 'EMPTY', headName: null, residentCount: 0, createdAt: '2024-03-10T08:00:00', updatedAt: '2024-03-10T08:00:00' },
];

let apartments = [...MOCK_APARTMENTS];
let nextId = 11;

// Mock Fee Types data
const MOCK_FEE_TYPES = [
  { id: 1, name: 'Phí dịch vụ tháng 06/2025', type: 'SERVICE', calcMethod: 'PER_AREA', rate: 8000, suggestedAmount: null, startDate: '2025-06-01', endDate: '2025-06-30', mandatory: true, status: 'ACTIVE', description: 'Phí dịch vụ hàng tháng', createdAt: '2025-05-15T08:00:00' },
  { id: 2, name: 'Phí quản lý quý II/2025', type: 'MANAGEMENT', calcMethod: 'PER_AREA', rate: 7000, suggestedAmount: null, startDate: '2025-04-01', endDate: '2025-06-30', mandatory: true, status: 'ACTIVE', description: 'Phí quản lý theo quý', createdAt: '2025-03-20T08:00:00' },
  { id: 3, name: 'Đóng góp quỹ thiếu nhi', type: 'DONATION', calcMethod: 'VOLUNTARY', rate: null, suggestedAmount: 50000, startDate: '2025-06-01', endDate: '2025-12-31', mandatory: false, status: 'ACTIVE', description: 'Đóng góp tự nguyện cho quỹ thiếu nhi', createdAt: '2025-05-01T08:00:00' },
  { id: 4, name: 'Phí dịch vụ tháng 07/2025', type: 'SERVICE', calcMethod: 'PER_AREA', rate: 8000, suggestedAmount: null, startDate: '2025-07-01', endDate: '2025-07-31', mandatory: true, status: 'ACTIVE', description: 'Phí dịch vụ hàng tháng', createdAt: '2025-06-15T08:00:00' },
  { id: 5, name: 'Phí quản lý quý III/2025', type: 'MANAGEMENT', calcMethod: 'PER_AREA', rate: 7000, suggestedAmount: null, startDate: '2025-07-01', endDate: '2025-09-30', mandatory: true, status: 'ACTIVE', description: 'Phí quản lý theo quý', createdAt: '2025-06-20T08:00:00' },
  { id: 6, name: 'Đóng góp quỹ khuyến học', type: 'DONATION', calcMethod: 'VOLUNTARY', rate: null, suggestedAmount: 100000, startDate: '2025-07-01', endDate: '2025-12-31', mandatory: false, status: 'ACTIVE', description: 'Đóng góp tự nguyện cho quỹ khuyến học', createdAt: '2025-06-01T08:00:00' },
  { id: 7, name: 'Phí dịch vụ tháng 05/2025', type: 'SERVICE', calcMethod: 'PER_AREA', rate: 8000, suggestedAmount: null, startDate: '2025-05-01', endDate: '2025-05-31', mandatory: true, status: 'INACTIVE', description: 'Phí dịch vụ hàng tháng (đã kết thúc)', createdAt: '2025-04-15T08:00:00' },
  { id: 8, name: 'Phí gửi xe máy tháng 06/2025', type: 'PARKING_MOTORBIKE', calcMethod: 'PER_VEHICLE', rate: 50000, suggestedAmount: null, startDate: '2025-06-01', endDate: '2025-06-30', mandatory: true, status: 'ACTIVE', description: 'Phí gửi xe máy hàng tháng', createdAt: '2025-05-15T08:00:00' },
  { id: 9, name: 'Phí gửi xe ô tô tháng 06/2025', type: 'PARKING_CAR', calcMethod: 'PER_VEHICLE', rate: 200000, suggestedAmount: null, startDate: '2025-06-01', endDate: '2025-06-30', mandatory: true, status: 'ACTIVE', description: 'Phí gửi xe ô tô hàng tháng', createdAt: '2025-05-15T08:00:00' },
];

let feeTypes = [...MOCK_FEE_TYPES];
let nextFeeTypeId = 10;

// Mock Residents data
const MOCK_RESIDENTS = [
  { id: 1, fullName: 'Nguyễn Minh Anh', phone: '0901234567', apartment: 'A-1201', relation: 'CHỦ HỘ', residencyStatus: 'LIVING', idCard: '001200001234', birthDate: '1985-03-15', email: 'minhanh@email.com', createdAt: '2024-01-15T08:00:00' },
  { id: 2, fullName: 'Trần Thị Bích', phone: '0901234568', apartment: 'A-1201', relation: 'VỢ', residencyStatus: 'LIVING', idCard: '001200001235', birthDate: '1988-07-22', email: 'bichtran@email.com', createdAt: '2024-01-15T08:00:00' },
  { id: 3, fullName: 'Nguyễn Minh Quân', phone: '0901234569', apartment: 'A-1201', relation: 'CON', residencyStatus: 'LIVING', idCard: '001200001236', birthDate: '2015-05-10', email: '', createdAt: '2024-01-15T08:00:00' },
  { id: 4, fullName: 'Lê Thu Hà', phone: '0912345678', apartment: 'A-1004', relation: 'CHỦ HỘ', residencyStatus: 'LIVING', idCard: '001200002345', birthDate: '1990-11-30', email: 'thuha@email.com', createdAt: '2024-01-10T08:00:00' },
  { id: 5, fullName: 'Lê Minh Đức', phone: '0912345679', apartment: 'A-1004', relation: 'CON', residencyStatus: 'LIVING', idCard: '001200002346', birthDate: '2018-02-14', email: '', createdAt: '2024-01-10T08:00:00' },
  { id: 6, fullName: 'Phạm Quang Huy', phone: '0923456789', apartment: 'B-0605', relation: 'CHỦ HỘ', residencyStatus: 'LIVING', idCard: '001200003456', birthDate: '1982-09-08', email: 'quanghuy@email.com', createdAt: '2024-01-20T08:00:00' },
  { id: 7, fullName: 'Đỗ Lan Phương', phone: '0934567890', apartment: 'A-0302', relation: 'CHỦ HỘ', residencyStatus: 'LIVING', idCard: '001200004567', birthDate: '1992-01-25', email: 'lanphuong@email.com', createdAt: '2024-01-12T08:00:00' },
];

let residents = [...MOCK_RESIDENTS];
let nextResidentId = 8;

// Helper: lọc người dân
function filterResidents(list, { search, residencyStatus }) {
  return list.filter(r => {
    if (search) {
      const s = search.toLowerCase();
      const matchesName = r.fullName.toLowerCase().includes(s);
      const matchesPhone = r.phone?.includes(search) || false;
      const matchesApt = r.apartment?.toLowerCase().includes(s) || false;
      if (!matchesName && !matchesPhone && !matchesApt) return false;
    }
    if (residencyStatus && r.residencyStatus !== residencyStatus) return false;
    return true;
  });
}

const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

// Helper: phân trang
function paginate(list, page, size) {
  const start = page * size;
  const end = start + size;
  return {
    content: list.slice(start, end),
    totalPages: Math.ceil(list.length / size),
    totalElements: list.length,
    number: page,
    size: size,
  };
}

// Helper: lọc
function filterApartments(list, { search, status }) {
  return list.filter(apt => {
    if (search) {
      const s = search.toLowerCase();
      const matchesNumber = apt.apartmentNumber.toLowerCase().includes(s);
      const matchesHead = apt.headName?.toLowerCase().includes(s) || false;
      if (!matchesNumber && !matchesHead) return false;
    }
    if (status && apt.status !== status) return false;
    return true;
  });
}

// Helper: lọc khoản thu
function filterFeeTypes(list, { search, type, status }) {
  return list.filter(ft => {
    if (search) {
      const s = search.toLowerCase();
      const matchesName = ft.name.toLowerCase().includes(s);
      const matchesDesc = ft.description?.toLowerCase().includes(s) || false;
      if (!matchesName && !matchesDesc) return false;
    }
    if (type && ft.type !== type) return false;
    if (status && ft.status !== status) return false;
    return true;
  });
}

// Mock API object
export const mockApi = {
  // POST /api/auth/login
  async login({ name, password }) {
    await sleep(DELAY);
    if (name === 'admin' && password === '1234567890') {
      const token = createMockToken(MOCK_USER);
      return { data: token }; // trả về string token như backend thật
    }
    const err = new Error('Tên đăng nhập hoặc mật khẩu không đúng.');
    err.response = { status: 401, data: { message: err.message } };
    throw err;
  },

  // GET /api/auth/me
  async getCurrentUser() {
    await sleep(DELAY);
    return { data: MOCK_USER };
  },

  // PUT /api/auth/me (cập nhật profile)
  async updateProfile(data) {
    await sleep(DELAY);
    // Cập nhật mock user (chỉ fullName)
    Object.assign(MOCK_USER, data);
    return { data: { ...MOCK_USER } };
  },

  // PUT /api/auth/password (đổi mật khẩu)
  async changePassword({ oldPassword, newPassword }) {
    await sleep(DELAY);
    if (oldPassword !== '1234567890') {
      const err = new Error('Mật khẩu hiện tại không đúng.');
      err.response = { status: 400, data: { message: err.message, errors: { oldPassword: err.message } } };
      throw err;
    }
    // Trong thực tế sẽ hash & lưu DB, ở đây chỉ giả lập thành công
    return { data: { message: 'Đổi mật khẩu thành công' } };
  },

  // GET /api/apartments
  async getApartments(params = {}) {
    await sleep(DELAY);
    const { page = 0, size = 10, search, status } = params;
    const filtered = filterApartments(apartments, { search, status });
    return { data: paginate(filtered, page, size) };
  },

  // POST /api/apartments
  async createApartment(data) {
    await sleep(DELAY);
    if (apartments.some(a => a.apartmentNumber === data.apartmentNumber)) {
      const err = new Error('Số căn hộ đã tồn tại.');
      err.response = { status: 400, data: { message: err.message, errors: { apartmentNumber: err.message } } };
      throw err;
    }
    const newApt = {
      id: nextId++,
      ...data,
      floor: Number(data.floor),
      area: Number(data.area),
      status: 'EMPTY',
      headName: null,
      residentCount: 0,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };
    apartments.unshift(newApt);
    return { data: newApt };
  },

  // POST /api/apartments/bulk-delete
  async bulkDelete(ids) {
    await sleep(DELAY);
    const failures = [];
    let ok = 0;
    for (const id of ids) {
      const idx = apartments.findIndex(a => a.id === id);
      if (idx === -1) {
        failures.push(`#${id}: Không tìm thấy`);
      } else {
        const apt = apartments[idx];
        if (apt.status === 'OCCUPIED' || apt.residentCount > 0) {
          failures.push(`${apt.apartmentNumber}: Không thể xóa căn hộ đang có người ở.`);
        } else {
          apartments.splice(idx, 1);
          ok++;
        }
      }
    }
    return { data: { success: ok, failed: failures.length, failures } };
  },

  // POST /api/apartments/batch-update-status
  async batchUpdateStatus({ ids, status }) {
    await sleep(DELAY);
    for (const id of ids) {
      const apt = apartments.find(a => a.id === id);
      if (apt) apt.status = status;
    }
    return { data: { success: ids.length } };
  },

  // GET /api/apartments/:id
  async getApartmentById(id) {
    await sleep(DELAY);
    const apt = apartments.find(a => a.id === id);
    if (!apt) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    return { data: apt };
  },

  // PUT /api/apartments/:id
  async updateApartment(id, data) {
    await sleep(DELAY);
    const idx = apartments.findIndex(a => a.id === id);
    if (idx === -1) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    if (data.apartmentNumber && apartments.some(a => a.apartmentNumber === data.apartmentNumber && a.id !== id)) {
      const err = new Error('Số căn hộ đã tồn tại.');
      err.response = { status: 400, data: { message: err.message, errors: { apartmentNumber: err.message } } };
      throw err;
    }
    apartments[idx] = { ...apartments[idx], ...data, updatedAt: new Date().toISOString() };
    return { data: apartments[idx] };
  },

  // DELETE /api/apartments/:id
  async deleteApartment(id) {
    await sleep(DELAY);
    const idx = apartments.findIndex(a => a.id === id);
    if (idx === -1) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    const apt = apartments[idx];
    if (apt.status === 'OCCUPIED' || apt.residentCount > 0) {
      const err = new Error('Không thể xóa căn hộ đang có người ở.');
      err.response = { status: 409, data: { message: err.message } };
      throw err;
    }
    apartments.splice(idx, 1);
    return { data: { message: 'Đã xóa' } };
  },

  // GET /api/fee-types
  async getFeeTypes(params = {}) {
    await sleep(DELAY);
    const { page = 0, size = 10, search, type, status } = params;
    const filtered = filterFeeTypes(feeTypes, { search, type, status });
    return { data: paginate(filtered, page, size) };
  },

  // POST /api/fee-types
  async createFeeType(data) {
    await sleep(DELAY);
    if (feeTypes.some(f => f.name === data.name)) {
      const err = new Error('Tên khoản thu đã tồn tại.');
      err.response = { status: 400, data: { message: err.message, errors: { name: err.message } } };
      throw err;
    }
    const newFeeType = {
      id: nextFeeTypeId++,
      ...data,
      rate: data.rate ? Number(data.rate) : null,
      suggestedAmount: data.suggestedAmount ? Number(data.suggestedAmount) : null,
      mandatory: data.mandatory !== undefined ? data.mandatory : data.type !== 'DONATION',
      status: data.status || 'ACTIVE',
      createdAt: new Date().toISOString(),
    };
    feeTypes.unshift(newFeeType);
    return { data: newFeeType };
  },

  // POST /api/fee-types/bulk-delete
  async bulkDeleteFeeTypes(ids) {
    await sleep(DELAY);
    const failures = [];
    let ok = 0;
    for (const id of ids) {
      const idx = feeTypes.findIndex(f => f.id === id);
      if (idx === -1) {
        failures.push(`#${id}: Không tìm thấy`);
      } else {
        feeTypes.splice(idx, 1);
        ok++;
      }
    }
    return { data: { success: ok, failed: failures.length, failures } };
  },

  // GET /api/fee-types/:id
  async getFeeTypeById(id) {
    await sleep(DELAY);
    const ft = feeTypes.find(f => f.id === id);
    if (!ft) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    return { data: ft };
  },

  // PUT /api/fee-types/:id
  async updateFeeType(id, data) {
    await sleep(DELAY);
    const idx = feeTypes.findIndex(f => f.id === id);
    if (idx === -1) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    if (data.name && feeTypes.some(f => f.name === data.name && f.id !== id)) {
      const err = new Error('Tên khoản thu đã tồn tại.');
      err.response = { status: 400, data: { message: err.message, errors: { name: err.message } } };
      throw err;
    }
    // Không cho đổi type (theo backend logic)
    if (data.type && data.type !== feeTypes[idx].type) {
      const err = new Error('Không thể đổi loại khoản thu.');
      err.response = { status: 400, data: { message: err.message, errors: { type: err.message } } };
      throw err;
    }
    feeTypes[idx] = {
      ...feeTypes[idx],
      ...data,
      rate: data.rate !== undefined ? (data.rate ? Number(data.rate) : null) : feeTypes[idx].rate,
      suggestedAmount: data.suggestedAmount !== undefined ? (data.suggestedAmount ? Number(data.suggestedAmount) : null) : feeTypes[idx].suggestedAmount,
      updatedAt: new Date().toISOString()
    };
    return { data: feeTypes[idx] };
  },

  // DELETE /api/fee-types/:id
  async deleteFeeType(id) {
    await sleep(DELAY);
    const idx = feeTypes.findIndex(f => f.id === id);
    if (idx === -1) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    feeTypes.splice(idx, 1);
    return { data: { message: 'Đã xóa' } };
  },

  // GET /api/residents
  async getResidents(params = {}) {
    await sleep(DELAY);
    const { page = 0, size = 10, search, residencyStatus } = params;
    const filtered = filterResidents(residents, { search, residencyStatus });
    return { data: paginate(filtered, page, size) };
  },

  // GET /api/residents/:id
  async getResidentById(id) {
    await sleep(DELAY);
    const r = residents.find(x => x.id === id);
    if (!r) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    return { data: r };
  },

  // PUT /api/residents/:id
  async updateResident(id, data) {
    await sleep(DELAY);
    const idx = residents.findIndex(r => r.id === id);
    if (idx === -1) {
      const err = new Error('Không tìm thấy');
      err.response = { status: 404, data: { message: err.message } };
      throw err;
    }
    residents[idx] = { ...residents[idx], ...data, updatedAt: new Date().toISOString() };
    return { data: residents[idx] };
  },

  // POST /api/residents
  async createResident(data) {
    await sleep(DELAY);
    if (residents.some(r => r.phone === data.phone)) {
      const err = new Error('Số điện thoại đã tồn tại.');
      err.response = { status: 400, data: { message: err.message, errors: { phone: err.message } } };
      throw err;
    }
    const newResident = {
      id: nextResidentId++,
      ...data,
      fullName: data.fullName.trim(),
      phone: data.phone.trim(),
      idCard: data.idCard?.trim() || '',
      apartment: data.apartment.trim(),
      relation: data.relation || 'HEAD',
      residencyStatus: data.residencyStatus || 'LIVING',
      createdAt: new Date().toISOString(),
    };
    residents.unshift(newResident);
    return { data: newResident };
  },
};

// Interceptor để chuyển hướng sang mock khi bật
export function setupMockInterceptor(api) {
  const useMock = localStorage.getItem('USE_MOCK_API') === 'true' ||
                  process.env.REACT_APP_USE_MOCK_API === 'true';

  if (!useMock) return;

  console.log('[MockAPI] Đang dùng Mock API - Backend không cần thiết');

  api.interceptors.request.use((config) => {
    const url = config.url || '';
    const method = (config.method || 'get').toLowerCase();

    // Mock endpoints cho Auth
    if (url === '/auth/login' && method === 'post') {
      return mockApi.login(config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/auth/me' && method === 'get') {
      return mockApi.getCurrentUser().then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/auth/me' && method === 'put') {
      return mockApi.updateProfile(config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/auth/password' && method === 'put') {
      return mockApi.changePassword(config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }

    // Mock endpoints cho Căn hộ
    if (url === '/apartments' && method === 'get') {
      return mockApi.getApartments(config.params).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/apartments' && method === 'post') {
      return mockApi.createApartment(config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/apartments/bulk-delete' && method === 'post') {
      return mockApi.bulkDelete(config.data.ids).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/apartments/batch-update-status' && method === 'post') {
      return mockApi.batchUpdateStatus(config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/apartments\/\d+$/) && method === 'get') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.getApartmentById(id).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/apartments\/\d+$/) && method === 'put') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.updateApartment(id, config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/apartments\/\d+$/) && method === 'delete') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.deleteApartment(id).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }

    // Mock endpoints cho Khoản thu (Fee Types)
    if (url === '/fee-types' && method === 'get') {
      return mockApi.getFeeTypes(config.params).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/fee-types' && method === 'post') {
      return mockApi.createFeeType(config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/fee-types/bulk-delete' && method === 'post') {
      return mockApi.bulkDeleteFeeTypes(config.data.ids).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/fee-types\/\d+$/) && method === 'get') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.getFeeTypeById(id).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/fee-types\/\d+$/) && method === 'put') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.updateFeeType(id, config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/fee-types\/\d+$/) && method === 'delete') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.deleteFeeType(id).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }

    // Mock endpoints cho Người dân (Residents)
    if (url === '/residents' && method === 'get') {
      return mockApi.getResidents(config.params).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url === '/residents' && method === 'post') {
      return mockApi.createResident(config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/residents\/\d+$/) && method === 'get') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.getResidentById(id).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }
    if (url.match(/^\/residents\/\d+$/) && method === 'put') {
      const id = parseInt(url.split('/').pop(), 10);
      return mockApi.updateResident(id, config.data).then(res => ({ ...config, adapter: () => Promise.resolve(res) }));
    }

    return config;
  });
}

// Helper để bật/tắt mock từ console
export function enableMockApi() {
  localStorage.setItem('USE_MOCK_API', 'true');
  console.log('Mock API đã bật. Reload trang để áp dụng.');
}
export function disableMockApi() {
  localStorage.removeItem('USE_MOCK_API');
  console.log('Mock API đã tắt. Reload trang để áp dụng.');
}

// Tự động expose ra window để dùng trong console
if (typeof window !== 'undefined') {
  window.MockAPI = { enable: enableMockApi, disable: disableMockApi };
}