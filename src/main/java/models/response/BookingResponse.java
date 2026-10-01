package models.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class BookingResponse {

    private String firstname;

    private String lastname;

    private int totalprice;

    private boolean depositpaid;

    @JsonProperty("bookingdates")
    private BookingDatesResponse bookingdates;

    private String additionalneeds;

    public BookingResponse() {
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