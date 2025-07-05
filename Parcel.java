package smartparcel;

public class Parcel {
    private String parcelId;
    private String senderName;
    private String receiverName;
    private String pickupPoint;
    private String dropPoint;
    private String status;

    // Constructor
    public Parcel(String parcelId, String senderName, String receiverName,
                  String pickupPoint, String dropPoint, String status) {
        this.parcelId = parcelId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.pickupPoint = pickupPoint;
        this.dropPoint = dropPoint;
        this.status = status;
    }

    // Getters
    public String getParcelId() { return parcelId; }
    public String getSenderName() { return senderName; }
    public String getReceiverName() { return receiverName; }
    public String getPickupPoint() { return pickupPoint; }
    public String getDropPoint() { return dropPoint; }
    public String getStatus() { return status; }
}
