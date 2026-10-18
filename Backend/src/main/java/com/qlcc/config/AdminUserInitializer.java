package com.qlcc.config;

import com.qlcc.entity.Apartment;
import com.qlcc.entity.Resident;
import com.qlcc.entity.User;
import com.qlcc.repository.ApartmentRepository;
import com.qlcc.repository.ResidentRepository;
import com.qlcc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:1234567890}")
    private String adminPassword;

    @Value("${app.admin.email:admin@gmail.com}")
    private String adminEmail;

    @Value("${app.admin.phone:0123456789}")
    private String adminPhone;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByName(adminUsername)) {
            // Schema V2: residents.apartment_id NOT NULL -> admin dung
            // apartment placeholder 'N/A'; tao neu DB moi chua co.
            Apartment placeholder = apartmentRepository.findByApartmentNumber("N/A");
            if (placeholder == null) {
                placeholder = new Apartment();
                placeholder.setApartmentNumber("N/A");
                placeholder.setFloor(1);
                placeholder.setArea(1.0);
                placeholder = apartmentRepository.save(placeholder);
            }

            Resident adminResident = new Resident();
            adminResident.setFullName("Admin User");
            adminResident.setPhone(adminPhone);
            adminResident.setEmail(adminEmail);
            adminResident.setIdCard("ADMIN-SEED");
            adminResident.setApartment(placeholder);
            residentRepository.save(adminResident);

            User adminUser = new User();
            adminUser.setResidentId(adminResident.getId());
            adminUser.setName(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole("ADMIN");
            adminUser.setActivation(true);
            adminUser.setFullName("Admin User");
            adminUser.setEmail(adminEmail);
            adminUser.setDateCreated(java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            userRepository.save(adminUser);

            System.out.println("Admin user '" + adminUsername + "' created successfully!");
        }
    }
}
