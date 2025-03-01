package com.store.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "applications")
public class RoomApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @NotNull(message = "Room cannot be null")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @Column(nullable = false)
    @NotNull(message = "Status cannot be null")
    private String status;

    // Default constructor
    public RoomApplication() {
    }

    // Constructor with parameters
    public RoomApplication(Room room, User user, String status) {
        this.room = room;
        this.user = user;
        this.status = status;
    }

    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "RoomApplication{" +
                "applicationId=" + applicationId +
                ", room=" + room +
                ", user=" + user +
                ", status='" + status + '\'' +
                '}';
    }
}