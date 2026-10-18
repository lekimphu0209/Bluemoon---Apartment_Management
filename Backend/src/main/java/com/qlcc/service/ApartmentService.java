package com.qlcc.service;

import com.qlcc.dto.ApartmentDTO;
import com.qlcc.dto.ResidentDTO;
import com.qlcc.entity.Apartment;
import com.qlcc.entity.ApartmentFeeUnit;
import com.qlcc.entity.Resident;
import com.qlcc.enums.ApartmentStatus;
import com.qlcc.enums.ResidentStatus;
import com.qlcc.repository.ApartmentRepository;
import com.qlcc.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private BillService billService;
    @Autowired
    private ApartmentFeeUnitService apartmentFeeUnitService;
    @Autowired
    private ResidentRepository residentRepository;

    // Lấy danh sách tất cả các căn hộ
    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public Apartment findById(Long id) {
        Optional<Apartment> apartment = apartmentRepository.findById(id);
        return apartment.orElseThrow(() -> new RuntimeException("Căn hộ không tồn tại"));
    }
    public List<Apartment> findAllById(List<Long> id) {
        return apartmentRepository.findAllById(id);
    }
    // Lấy thông tin căn hộ theo ID
    public Optional<Apartment> getApartmentById(Long id) {
        return apartmentRepository.findById(id);
    }
    public Apartment save(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }
    public List<Apartment> save(List<Apartment> apartment) {
        return apartmentRepository.saveAll(apartment);
    }

    public List<String> getApartmentNumbers() {
        List<Apartment> apartments = apartmentRepository.findAll();
        List<String> apartmentNumbers = new ArrayList<>();
        for (Apartment apartment : apartments) {
            apartmentNumbers.add(apartment.getApartmentNumber());
        }
        return apartmentNumbers;
    }

    public Apartment getApartmentByNumber(String apartmentNumber) {
        return apartmentRepository.findByApartmentNumber(apartmentNumber);
    }

    // Schema V2: chi con number/floor/area/status
    public void saveApartment(ApartmentDTO apartmentDTO) {
        Apartment apartment = new Apartment();
        apartment.setApartmentNumber(apartmentDTO.getApartmentNumber());
        apartment.setFloor(apartmentDTO.getFloor());
        apartment.setArea(apartmentDTO.getArea());
        apartment.setStatus(apartmentDTO.getStatus() != null ? apartmentDTO.getStatus() : ApartmentStatus.EMPTY);

        apartmentRepository.save(apartment);
    }

    public void updateApartment(ApartmentDTO apartmentDTO) {
        Apartment apartment = getApartmentByNumber(apartmentDTO.getApartmentNumber());
        apartment.setFloor(apartmentDTO.getFloor());
        apartment.setArea(apartmentDTO.getArea());
        if (apartmentDTO.getStatus() != null) {
            apartment.setStatus(apartmentDTO.getStatus());
        }

        apartmentRepository.save(apartment);
    }

    /**
     * Go resident khoi cac apartment (set resident.apartment = null neu trung).
     * Apartment trong -> EMPTY.
     */
    @Transactional
    public void deleteResident(Set<String> apartmentNumbers, Resident resident) {
        if (apartmentNumbers == null || apartmentNumbers.isEmpty()) return;
        for (String apartmentNumber : apartmentNumbers) {
            Apartment apartment = getApartmentByNumber(apartmentNumber);
            if (apartment == null) continue;

            // Resident moi chi thuoc 1 apartment: go neu hien dang o day
            if (resident.getApartment() != null
                    && apartment.getId().equals(resident.getApartment().getId())) {
                resident.setApartment(null);
                resident.getApartmentNumbersInput().remove(apartmentNumber);
                residentRepository.save(resident);
            }

            // Danh gia lai trang thai can ho sau khi go
            refreshApartmentStatus(apartment);
        }
    }

    /**
     * Gan resident vao apartment dau tien trong set (schema moi: 1 resident
     * chi thuoc 1 apartment). Cap nhat trang thai can ho + tao bill ban dau
     * nhu behavior cu.
     */
    @Transactional
    public void updateResident(Set<String> apartmentNumbers, Resident resident) {
        if (apartmentNumbers == null || apartmentNumbers.isEmpty()) return;

        // Lay apartment dau tien (trong schema cu resident co the thuoc nhieu
        // apartment; schema moi chi 1 -> uu tien phan tu dau)
        String apartmentNumber = apartmentNumbers.iterator().next();
        Apartment apartment = getApartmentByNumber(apartmentNumber);
        if (apartment == null) return;

        List<Resident> existing = residentRepository.findByApartment_Id(apartment.getId());
        boolean isFirstResident = existing == null || existing.isEmpty();

        resident.setApartment(apartment);
        if (resident.getStatus() == null) {
            resident.setStatus(ResidentStatus.LIVING);
        }
        residentRepository.save(resident);

        if (apartment.getStatus() != ApartmentStatus.OCCUPIED) {
            apartment.setStatus(ApartmentStatus.OCCUPIED);
            apartmentRepository.save(apartment);
        }

        if (isFirstResident) {
            ApartmentFeeUnit apartmentFeeUnit = apartmentFeeUnitService.getFeeUnit();
            billService.saveApartmentBill(apartment, "phí dịch vụ căn hộ" + apartment.getApartmentNumber(), apartmentFeeUnit.getServiceFeePerM2());
            billService.saveApartmentBill(apartment, "phí quản lý căn hộ" + apartment.getApartmentNumber(), 7000L);
        }
    }

    /**
     * Cap nhat apartment cua resident theo DTO: schema moi chi giu 1
     * apartment -> lay phan tu dau tien cua set.
     */
    @Transactional
    public void updateResident(Resident resident, ResidentDTO userDTO) {
        Set<String> requested = userDTO.getApartmentNumbers();
        Apartment current = resident.getApartment();
        String requestedNumber = (requested == null || requested.isEmpty()) ? null : requested.iterator().next();

        if (requestedNumber == null) {
            // Khong yeu cau apartment nao -> go khoi apartment hien tai
            if (current != null) {
                resident.setApartment(null);
                residentRepository.save(resident);
                refreshApartmentStatus(current);
            }
            resident.setApartmentNumbers(new HashSet<>());
            return;
        }

        if (current == null || !requestedNumber.equals(current.getApartmentNumber())) {
            Apartment target = getApartmentByNumber(requestedNumber);
            if (target != null) {
                resident.setApartment(target);
                resident.setApartmentNumbers(Set.of(requestedNumber));
                residentRepository.save(resident);

                if (target.getStatus() != ApartmentStatus.OCCUPIED) {
                    target.setStatus(ApartmentStatus.OCCUPIED);
                    apartmentRepository.save(target);
                }
                if (current != null) {
                    refreshApartmentStatus(current);
                }
            }
        }
    }

    @Transactional
    public void deleteResident(Resident resident) {
        Apartment current = resident.getApartment();
        if (current != null) {
            resident.setApartment(null);
            residentRepository.save(resident);
            refreshApartmentStatus(current);
        }
        resident.setApartmentNumbers(new HashSet<>());
    }

    @Transactional
    public void updateResident(Resident resident) {
        // Resolve tu input set (compat) sang apartment entity
        Set<String> requested = resident.getApartmentNumbersInput();
        if (requested != null && !requested.isEmpty()) {
            updateResident(requested, resident);
        }
    }

    private void refreshApartmentStatus(Apartment apartment) {
        if (apartment == null) return;
        List<Resident> remaining = residentRepository.findByApartment_Id(apartment.getId());
        boolean hasLiving = remaining != null && remaining.stream()
                .anyMatch(r -> r.getStatus() != ResidentStatus.MOVED_OUT);
        apartment.setStatus(hasLiving ? ApartmentStatus.OCCUPIED : ApartmentStatus.EMPTY);
        apartmentRepository.save(apartment);
    }

    // Xóa căn hộ theo ID
    public void deleteApartment(Long id) {
        apartmentRepository.deleteById(id);
    }

    // Phương thức filter căn hộ (schema V2: bo roomNumber/type)
    @SuppressWarnings("unchecked")
    public List<Apartment> filterApartments(Map<String, Object> filterParams) {
        String filterLogic = (String) filterParams.getOrDefault("filterLogic", "AND");

        String apartmentNumber = (String) filterParams.get("apartmentNumber");
        List<Integer> floors = (List<Integer>) filterParams.get("floors");
        Double minArea = (Double) filterParams.get("minArea");
        Double maxArea = (Double) filterParams.get("maxArea");
        List<ApartmentStatus> statuses = (List<ApartmentStatus>) filterParams.get("status");

        if ("OR".equalsIgnoreCase(filterLogic)) {
            Set<Apartment> results = new HashSet<>();

            if (apartmentNumber != null && !apartmentNumber.isEmpty()) {
                results.addAll(apartmentRepository.findByApartmentNumberContainingIgnoreCase(apartmentNumber));
            }

            if (floors != null && !floors.isEmpty()) {
                results.addAll(apartmentRepository.findByFloorIn(floors));
            }

            if (minArea != null) {
                results.addAll(apartmentRepository.findByAreaGreaterThanEqual(minArea));
            }

            if (maxArea != null) {
                results.addAll(apartmentRepository.findByAreaLessThanEqual(maxArea));
            }

            if (statuses != null && !statuses.isEmpty()) {
                results.addAll(apartmentRepository.findByStatusIn(statuses));
            }

            return new ArrayList<>(results);
        } else {
            Specification<Apartment> spec = null;

            if (apartmentNumber != null && !apartmentNumber.isEmpty()) {
                Specification<Apartment> apartmentNumberSpec = (root, query, cb) ->
                    cb.like(cb.lower(root.get("apartmentNumber")), "%" + apartmentNumber.toLowerCase() + "%");
                spec = spec == null ? apartmentNumberSpec : spec.and(apartmentNumberSpec);
            }

            if (floors != null && !floors.isEmpty()) {
                Specification<Apartment> floorSpec = (root, query, cb) -> root.get("floor").in(floors);
                spec = spec == null ? floorSpec : spec.and(floorSpec);
            }

            if (minArea != null) {
                Specification<Apartment> minAreaSpec = (root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("area"), minArea);
                spec = spec == null ? minAreaSpec : spec.and(minAreaSpec);
            }

            if (maxArea != null) {
                Specification<Apartment> maxAreaSpec = (root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("area"), maxArea);
                spec = spec == null ? maxAreaSpec : spec.and(maxAreaSpec);
            }

            if (statuses != null && !statuses.isEmpty()) {
                Specification<Apartment> statusSpec = (root, query, cb) -> root.get("status").in(statuses);
                spec = spec == null ? statusSpec : spec.and(statusSpec);
            }

            return spec == null ? apartmentRepository.findAll() : apartmentRepository.findAll(spec);
        }
    }

    public List<Apartment> findByResidentIdsContaining(Long id) {
        return apartmentRepository.findByResidentIdsContaining(id);
    }

    public Apartment findByApartmentNumber(String apartmentNumber) {
        return apartmentRepository.findByApartmentNumber(apartmentNumber);
    }
}
