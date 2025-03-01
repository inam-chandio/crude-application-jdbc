package com.store.model;

import javax.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    private String location;
    
    @Column(name = "price_per_month")
    private Double pricePerMonth;
    
    @Column(name = "spoken_languages")
    private String spokenLanguages;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;

    // Add getters and setters for isAvailable
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getPricePerMonth() {
		return pricePerMonth;
	}

	public void setPricePerMonth(Double pricePerMonth) {
		this.pricePerMonth = pricePerMonth;
	}

	public String getSpokenLanguages() {
		return spokenLanguages;
	}

	public void setSpokenLanguages(String spokenLanguages) {
		this.spokenLanguages = spokenLanguages;
	}

	public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    // Previous getters and setters remain the same
}