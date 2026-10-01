package models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetBookingByIdResponse {

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("totalprice")
    private int totalprice;

    @JsonProperty("depositpaid")
    private boolean depositpaid;

    @JsonProperty("bookingdates")
    private BookingDatesResponse bookingdates;

    @JsonProperty("additionalneeds")
    private String additionalneeds;
    
    
    public GetBookingByIdResponse() {
    	
    }
    
    public GetBookingByIdResponse(String firstname, String lastname, int totalprice, boolean depositpaid, BookingDatesResponse bookingdates, String additionalneeds ) {
    		this.firstname = firstname;
    		this.lastname = lastname;
    		this.totalprice = totalprice;
    		this.depositpaid = depositpaid;
    		this.bookingdates = bookingdates;
    		this.additionalneeds = additionalneeds;
    }

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}

	public boolean isDepositpaid() {
		return depositpaid;
	}

	public void setDepositpaid(boolean depositpaid) {
		this.depositpaid = depositpaid;
	}

	public BookingDatesResponse getBookingdates() {
		return bookingdates;
	}

	public void setBookingdates(BookingDatesResponse bookingdates) {
		this.bookingdates = bookingdates;
	}

	public String getAdditionalneeds() {
		return additionalneeds;
	}

	public void setAdditionalneeds(String additionalneeds) {
		this.additionalneeds = additionalneeds;
	}
    
    
    
}
