package com.store.repository;

import com.store.model.RoomApplication;  // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<RoomApplication, Long> {
    List<RoomApplication> findByUser_UserIdOrderByApplicationIdDesc(Long userId);
    List<RoomApplication> findByRoom_RoomId(Long roomId);
    List<RoomApplication> findByStatusAndUser_UserId(String status, Long userId);
    boolean existsByRoom_RoomIdAndStatusAndUser_UserId(Long roomId, String status, Long userId);
}