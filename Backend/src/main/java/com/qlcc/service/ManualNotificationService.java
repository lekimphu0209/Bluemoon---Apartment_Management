package com.qlcc.service;


import com.qlcc.entity.ManualNotification;
import com.qlcc.entity.Resident;
import com.qlcc.repository.ManualNotificationRepository;
import com.qlcc.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManualNotificationService {
    @Autowired
    ResidentRepository residentRepository;

    private final ManualNotificationRepository manualNotificationRepository;

    @Autowired
    public ManualNotificationService(ManualNotificationRepository manualNotificationRepository) {
        this.manualNotificationRepository = manualNotificationRepository;
    }

    public List<ManualNotification> getAllManualNotifications() {
        return manualNotificationRepository.findAll();
    }

    public ManualNotification createManualNotification(List<Long> residentIds, String message) {
        ManualNotification manualNotification = new ManualNotification();
        List<Resident> residents = residentRepository.findAllById(residentIds);
        manualNotification.setResidents(residents);
        manualNotification.setMessage(message);
        manualNotification.setCreatedAt(LocalDateTime.now());

        return manualNotificationRepository.save(manualNotification);
    }
}
