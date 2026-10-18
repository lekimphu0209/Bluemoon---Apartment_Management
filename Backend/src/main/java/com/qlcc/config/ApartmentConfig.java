package com.qlcc.config;

import com.qlcc.entity.Apartment;
import com.qlcc.enums.ApartmentStatus;
import com.qlcc.repository.ApartmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Configuration
public class ApartmentConfig {

    @Autowired
    private ApartmentRepository apartmentRepository;

    private Random random = new Random();

    @PostConstruct
    @Transactional
    public void initApartments() {
        if (apartmentRepository.count() > 0) {
            System.out.println("Apartments đã được khởi tạo trước đó.");
            return;
        }

        List<Apartment> apartments = new ArrayList<>();

        int apartmentCounter = 1;

        for (int floor = 1; floor <= 10; floor++) {
            for (int i = 1; i <= 5; i++) {
                Apartment apartment = new Apartment();

                // Format: A0001 -> A0050
                String apartmentNumber = String.format("A%04d", apartmentCounter++);
                apartment.setApartmentNumber(apartmentNumber);
                apartment.setFloor(floor);

                // Schema V2: bo room_number/type -> chi con area
                double area;
                if (floor == 1) {
                    area = 20 + random.nextInt(21); // 20-40 m²
                } else {
                    area = 45 + random.nextInt(46); // 45-90 m²
                }
                apartment.setArea(area);

                apartment.setStatus(ApartmentStatus.EMPTY);

                apartments.add(apartment);
            }
        }

        apartmentRepository.saveAll(apartments);
        System.out.println("Đã khởi tạo 50 căn hộ!");
    }
}
