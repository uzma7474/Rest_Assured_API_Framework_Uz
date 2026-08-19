package docs;

public class CreateEventRequest {

	private String title;
	private String description;
	private String category;
	private String venue;
	private String city;
	private String eventDate;
	private double price;
	private int totalSeats;
	private String imageUrl;

	public CreateEventRequest() {
	}

	public CreateEventRequest(String title, String description, String category, String venue, String city,
			String eventDate, double price, int totalSeats, String imageUrl) {

		this.title = title;
		this.description = description;
		this.category = category;
		this.venue = venue;
		this.city = city;
		this.eventDate = eventDate;
		this.price = price;
		this.totalSeats = totalSeats;
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}