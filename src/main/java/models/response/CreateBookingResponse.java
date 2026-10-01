package models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import models.requests.Booking;

@Data
public class CreateBookingResponse {
	
	@JsonProperty("bookingid")
	private Integer bookingid;
	
	 @JsonProperty("booking")
	private BookingResponse booking;
	
	public CreateBookingResponse() {
		
	}
	
	public CreateBookingResponse(Integer bookingId, BookingResponse booking) {
		this.bookingid = bookingId;
		this.booking = booking;
	}
	
	
	public Integer getBookingid() {
		return bookingid;
	}

	public void setBookingid(Integer bookingid) {
		this.bookingid = bookingid;
	}

	public BookingResponse getBooking() {
		return booking;
	}

	public void setBooking(BookingResponse booking) {
		this.booking = booking;
	}

	
	

}
