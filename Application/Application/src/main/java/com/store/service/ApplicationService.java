package com.store.service;

import com.store.model.RoomApplication;  // Updated import
import com.store.model.Room;
import com.store.model.User;
import com.store.repository.ApplicationRepository;
import com.store.repository.RoomRepository;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public RoomApplication applyForRoom(Long userId, Long roomId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Room> room = roomRepository.findById(roomId);

        if (user.isPresent() && room.isPresent()) {
            Room roomEntity = room.get();
            
            if (!roomEntity.getIsAvailable()) {
                throw new IllegalStateException("Room is not available");
            }

            boolean hasPendingApplication = applicationRepository
                .existsByRoom_RoomIdAndStatusAndUser_UserId(roomId, "Pending", userId);
            if (hasPendingApplication) {
                throw new IllegalStateException("User already has a pending application for this room");
            }

            RoomApplication application = new RoomApplication();
            application.setUser(user.get());
            application.setRoom(roomEntity);
            application.setStatus("Pending");

            return applicationRepository.save(application);
        }
        return null;
    }

    @Transactional
    public RoomApplication cancelApplication(Long applicationId, Long userId) {
        Optional<RoomApplication> application = applicationRepository.findById(applicationId);
        if (application.isPresent() && application.get().getUser().getUserId().equals(userId)) {
            RoomApplication app = application.get();
            if ("Pending".equals(app.getStatus())) {
                app.setStatus("Cancelled");
                return applicationRepository.save(app);
            }
        }
        return null;
    }

    public List<RoomApplication> findAllApplicationsByUserId(Long userId) {
        return applicationRepository.findByUser_UserIdOrderByApplicationIdDesc(userId);
    }

    public List<RoomApplication> findApplicationsByStatus(String status, Long userId) {
        return applicationRepository.findByStatusAndUser_UserId(status, userId);
    }

    @Transactional
    public RoomApplication updateApplicationStatus(Long applicationId, String status) {
        Optional<RoomApplication> application = applicationRepository.findById(applicationId);
        if (application.isPresent()) {
            RoomApplication app = application.get();
            app.setStatus(status);
            
            if ("Approved".equals(status)) {
                Room room = app.getRoom();
                room.setIsAvailable(false);
                roomRepository.save(room);
            }
            
            return applicationRepository.save(app);
        }
        return null;
    }
}