package com.store.controller;

import com.store.model.Room;
import com.store.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Room room) {
        try {
            Room newRoom = roomService.createRoom(room);
            return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error creating room"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRooms(@RequestParam(required = false) Boolean availableOnly) {
        try {
            List<Room> rooms = Boolean.TRUE.equals(availableOnly) ? 
                roomService.getAllAvailableRooms() : 
                roomService.getAllRooms();
                
            return rooms.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error fetching rooms"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable Long roomId) {
        try {
            Room room = roomService.getRoomById(roomId);
            return new ResponseEntity<>(room, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error fetching room"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable Long roomId, @RequestBody Room room) {
        try {
            Room updatedRoom = roomService.updateRoom(roomId, room);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error updating room"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        try {
            roomService.deleteRoom(roomId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error deleting room"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/price")
    public ResponseEntity<?> searchRoomsByPrice(
            @RequestParam Double maxPrice,
            @RequestParam(required = false, defaultValue = "true") Boolean availableOnly) {
        try {
            List<Room> rooms = Boolean.TRUE.equals(availableOnly) ?
                roomService.findAvailableRoomsByMaxPrice(maxPrice) :
                roomService.findRoomsByMaxPrice(maxPrice);
            
            return rooms.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error searching rooms by price"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/location")
    public ResponseEntity<?> searchRoomsByLocation(
            @RequestParam String location,
            @RequestParam(required = false, defaultValue = "true") Boolean availableOnly) {
        try {
            List<Room> rooms = Boolean.TRUE.equals(availableOnly) ?
                roomService.findAvailableRoomsByLocation(location) :
                roomService.findRoomsByLocation(location);
            
            return rooms.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error searching rooms by location"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/language")
    public ResponseEntity<?> searchRoomsByLanguage(
            @RequestParam String language,
            @RequestParam(required = false, defaultValue = "true") Boolean availableOnly) {
        try {
            List<Room> rooms = Boolean.TRUE.equals(availableOnly) ?
                roomService.findAvailableRoomsByLanguage(language) :
                roomService.findRoomsByLanguage(language);
            
            return rooms.isEmpty() ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error searching rooms by language"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{roomId}/availability")
    public ResponseEntity<?> updateRoomAvailability(
            @PathVariable Long roomId,
            @RequestParam Boolean isAvailable) {
        try {
            Room updatedRoom = roomService.updateRoomAvailability(roomId, isAvailable);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error updating room availability"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomId}/availability")
    public ResponseEntity<?> checkRoomAvailability(@PathVariable Long roomId) {
        try {
            boolean isAvailable = roomService.isRoomAvailable(roomId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("available", isAvailable);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(createErrorResponse(e.getMessage()), 
                HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse("Error checking room availability"), 
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}