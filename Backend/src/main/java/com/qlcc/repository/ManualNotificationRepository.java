package com.qlcc.repository;

import com.qlcc.entity.ManualNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManualNotificationRepository extends JpaRepository<ManualNotification, Long> {

}