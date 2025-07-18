<?php

include 'db_connect.php'; // Includes the database connection

// Check if parcel_id is provided in the URL (e.g., get_parcel.php?parcel_id=P001)
if (isset($_GET['parcel_id'])) {
    $parcelId = $_GET['parcel_id'];

    // Use prepared statements to prevent SQL injection
    $stmt = $conn->prepare("SELECT parcel_tracking_id, sender_name, receiver_name, pickup_point, drop_off_point, status FROM parcels WHERE parcel_tracking_id = ?");
    $stmt->bind_param("s", $parcelId); // "s" means the parameter is a string

    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Fetch the data and encode it as JSON
        $parcel = $result->fetch_assoc();
        echo json_encode(["success" => true, "data" => $parcel]);
    } else {
        // No parcel found
        echo json_encode(["success" => false, "message" => "Parcel not found"]);
    }

    $stmt->close();
} else {
    // Parcel ID not provided in the URL
    echo json_encode(["success" => false, "message" => "Parcel ID is required"]);
}

$conn->close();
?>