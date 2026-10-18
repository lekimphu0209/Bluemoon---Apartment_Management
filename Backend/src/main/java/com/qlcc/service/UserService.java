package com.qlcc.service;

import com.qlcc.dto.ManualUserDTO;
import com.qlcc.dto.ResidentDTO;
import com.qlcc.dto.UserDTO;
import com.qlcc.entity.Apartment;
import com.qlcc.entity.Resident;
import com.qlcc.entity.User;
import com.qlcc.repository.ApartmentRepository;
import com.qlcc.repository.ResidentRepository;
import com.qlcc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    /**
     * Schema V2: residents.apartment_id va id_card NOT NULL.
     * DTO cu khong co idCard -> sinh placeholder; apartment rong -> 'N/A'.
     */
    public void addUser(UserDTO request) {
        Resident resident = new Resident();
        resident.setFullName(request.getFullName());
        resident.setPhone(request.getPhone());
        resident.setEmail(request.getEmail());
        resident.setIdCard("PENDING-" + UUID.randomUUID().toString().substring(0, 8));
        resident.setApartmentNumbers(request.getApartmentNumbers());
        resident.setApartment(resolveApartment(request.getApartmentNumbers()));
        residentRepository.save(resident);

        User user = new User();
        user.setResidentId(resident.getId());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
    }
    public void addUser(ManualUserDTO request) {
        Resident resident = new Resident();
        resident.setFullName(request.getFullName());
        resident.setPhone(request.getPhone());
        resident.setEmail(request.getEmail());
        resident.setIdCard("PENDING-" + UUID.randomUUID().toString().substring(0, 8));
        resident.setApartmentNumbers(request.getApartmentNumbers());
        resident.setApartment(resolveApartment(request.getApartmentNumbers()));
        resident.setStatus(request.getStatus());
        residentRepository.save(resident);

        User user = new User();
        user.setResidentId(resident.getId());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        userRepository.save(user);

        apartmentService.updateResident(resident);
    }

    /**
     * Resolve Set<String> apartmentNumbers (compat) -> Apartment entity.
     * Schema V2 bat apartment_id NOT NULL: fallback ve can ho placeholder
     * 'N/A' (duoc V2 tao khi co orphan, hoac tao o day neu DB moi).
     */
    private Apartment resolveApartment(Set<String> apartmentNumbers) {
        if (apartmentNumbers != null && !apartmentNumbers.isEmpty()) {
            Apartment apt = apartmentRepository.findByApartmentNumber(apartmentNumbers.iterator().next());
            if (apt != null) return apt;
        }
        Apartment placeholder = apartmentRepository.findByApartmentNumber("N/A");
        if (placeholder == null) {
            placeholder = new Apartment();
            placeholder.setApartmentNumber("N/A");
            placeholder.setFloor(1);
            placeholder.setArea(1.0);
            placeholder = apartmentRepository.save(placeholder);
        }
        return placeholder;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean activateUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.isActivation()) {  // Chỉ kích hoạt nếu chưa kích hoạt
                user.setActivation(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean deactivateUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isActivation()) {
                user.setActivation(false);
//                System.out.println(user);
                userRepository.save(user);
//                System.out.println(user);
                return true;
            }
        }
        return false;
    }

    public boolean updateUser(Long id, ResidentDTO user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userUpdate = userOptional.get();
            userUpdate.setRole(user.getRole());

            Resident resident = residentRepository.findById(userUpdate.getResidentId()).get();
            resident.setApartmentNumbers(user.getApartmentNumbers());
            resident.setApartment(resolveApartment(user.getApartmentNumbers()));
            residentRepository.save(resident);

            userRepository.save(userUpdate);
            return true;
        }
        return false;
    }


    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Resident resident = residentRepository.findById(user.getResidentId()).orElse(null);
            if (resident == null) {
                return false;
            }
            apartmentService.deleteResident(resident);
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public String changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "error_user_not_found";
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return "error_wrong_old_password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "success";
    }



    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public List<User> findAllById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }
    public User getUserByName(String username) {
        return userRepository.findByName(username);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}