package com.example.demo.config;

import com.example.demo.entity.Resident;
import com.example.demo.entity.User;
import com.example.demo.repository.ResidentRepository;
import com.example.demo.repository.UserRepository;
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
            Resident adminResident = new Resident();
            adminResident.setFullName("Admin User");
            adminResident.setPhone(adminPhone);
            adminResident.setEmail(adminEmail);
            residentRepository.save(adminResident);

            User adminUser = new User();
            adminUser.setResidentId(adminResident.getId());
            adminUser.setName(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole("ADMIN");
            adminUser.setActivation(true);
            adminUser.setDateCreated(java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            userRepository.save(adminUser);

            System.out.println("Admin user '" + adminUsername + "' created successfully!");
        }
    }
}
