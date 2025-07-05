package smartparcel;

public class ApiClient {

    // Simulated API call to backend - you can connect to real API later
    public static Parcel getParcelStatus(String parcelId) {
        // Dummy Data
        if (parcelId.equals("P123")) {
            return new Parcel("P123", "Alice", "Bob",
                    "Counter A", "Drop Point B",
                    "In Transit");
        } else if (parcelId.equals("P124")) {
            return new Parcel("P124", "John", "Jane",
                    "Counter B", "Drop Point C",
                    "Delivered");
        } else {
            return null; // Not found
        }
    }
}
