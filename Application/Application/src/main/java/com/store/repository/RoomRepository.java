package com.store.repository;

import com.store.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // Find all available rooms
    List<Room> findByIsAvailableTrue();
    
    // Find available rooms by max price
    List<Room> findByPricePerMonthLessThanEqualAndIsAvailableTrue(Double maxPrice);
    
    // Find available rooms by location
    List<Room> findByLocationContainingIgnoreCaseAndIsAvailableTrue(String location);
    
    // Find available rooms by language
    List<Room> findBySpokenLanguagesContainingIgnoreCaseAndIsAvailableTrue(String language);
}