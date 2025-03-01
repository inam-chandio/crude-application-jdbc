package com.store.service;

import com.store.model.Room;
import com.store.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Create a new room
    @Transactional
    public Room createRoom(Room room) {
        validateRoom(room);
        return roomRepository.save(room);
    }

    // Get all available rooms
    public List<Room> getAllAvailableRooms() {
        return roomRepository.findByIsAvailableTrue();
    }

    // Get all rooms (including unavailable ones)
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by ID
    public Room getRoomById(Long roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
    }

    // Update room
    @Transactional
    public Room updateRoom(Long roomId, Room roomDetails) {
        validateRoom(roomDetails);
        Room existingRoom = getRoomById(roomId);
        
        existingRoom.setLocation(roomDetails.getLocation());
        existingRoom.setPricePerMonth(roomDetails.getPricePerMonth());
        existingRoom.setSpokenLanguages(roomDetails.getSpokenLanguages());
        existingRoom.setIsAvailable(roomDetails.getIsAvailable());

        return roomRepository.save(existingRoom);
    }

    // Delete room
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = getRoomById(roomId);
        roomRepository.delete(room);
    }

    // Find available rooms by maximum price
    public List<Room> findAvailableRoomsByMaxPrice(Double maxPrice) {
        if (maxPrice == null || maxPrice <= 0) {
            throw new IllegalArgumentException("Invalid price value");
        }
        return roomRepository.findByPricePerMonthLessThanEqualAndIsAvailableTrue(maxPrice);
    }

    // Find all rooms by maximum price (including unavailable)
    public List<Room> findRoomsByMaxPrice(Double maxPrice) {
        if (maxPrice == null || maxPrice <= 0) {
            throw new IllegalArgumentException("Invalid price value");
        }
        return roomRepository.findAll().stream()
                .filter(room -> room.getPricePerMonth() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Find available rooms by location
    public List<Room> findAvailableRoomsByLocation(String location) {
        validateSearchString(location);
        return roomRepository.findByLocationContainingIgnoreCaseAndIsAvailableTrue(location.trim());
    }

    // Find all rooms by location (including unavailable)
    public List<Room> findRoomsByLocation(String location) {
        validateSearchString(location);
        return roomRepository.findAll().stream()
                .filter(room -> room.getLocation().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Find available rooms by language
    public List<Room> findAvailableRoomsByLanguage(String language) {
        validateSearchString(language);
        return roomRepository.findBySpokenLanguagesContainingIgnoreCaseAndIsAvailableTrue(language.trim());
    }

    // Find all rooms by language (including unavailable)
    public List<Room> findRoomsByLanguage(String language) {
        validateSearchString(language);
        return roomRepository.findAll().stream()
                .filter(room -> room.getSpokenLanguages().toLowerCase().contains(language.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Update room availability
    @Transactional
    public Room updateRoomAvailability(Long roomId, boolean isAvailable) {
        Room room = getRoomById(roomId);
        room.setIsAvailable(isAvailable);
        return roomRepository.save(room);
    }

    // Check if room is available
    public boolean isRoomAvailable(Long roomId) {
        Room room = getRoomById(roomId);
        return room.getIsAvailable();
    }

    // Validation helpers
    private void validateRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (room.getLocation() == null || room.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        if (room.getPricePerMonth() == null || room.getPricePerMonth() <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }
        if (room.getSpokenLanguages() == null || room.getSpokenLanguages().trim().isEmpty()) {
            throw new IllegalArgumentException("Spoken languages cannot be empty");
        }
    }

    private void validateSearchString(String searchString) {
        if (searchString == null || searchString.trim().isEmpty()) {
            throw new IllegalArgumentException("Search string cannot be empty");
        }
    }
}