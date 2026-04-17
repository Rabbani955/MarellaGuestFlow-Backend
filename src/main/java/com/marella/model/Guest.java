package com.marella.model;
import jakarta.validation.constraints.*;

public class Guest {

	@NotBlank(message = "Name is required")
    private String name;
	
	@NotBlank(message = "Phone is required")
    private String phone;
	
	@NotBlank(message = "Room number is required")
    private String roomNumber;
	
    private String checkInTime;
    private String checkOutTime;
   

	private String status;
	@NotBlank(message = "Guest count required")
    private String guests;
	@NotBlank(message = "Booking type required")
    private String bookingType;
	 @Positive(message = "Amount must be positive")
    private double amount;

    // ✅ Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getGuests() {
		return guests;
	}

	public void setGuests(String guests) {
		this.guests = guests;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}
	public double getAmount() {
	    return amount;
	}

	public void setAmount(double amount) {
	    this.amount = amount;
	}
}