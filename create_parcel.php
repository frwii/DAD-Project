<?php

include 'db_connect.php';

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get the POST data
    $sender_name = $_POST['sender_name'] ?? '';
    $sender_phone = $_POST['sender_phone'] ?? '';
    $sender_address = $_POST['sender_address'] ?? '';
    $receiver_name = $_POST['receiver_name'] ?? '';
    $receiver_phone = $_POST['receiver_phone'] ?? '';
    $receiver_address = $_POST['receiver_address'] ?? '';
    $pickup_point = $_POST['pickup_point'] ?? '';
    $drop_off_point = $_POST['drop_off_point'] ?? '';

    // Simple validation
    if (empty($sender_name) || empty($receiver_name)) {
        echo json_encode(["success" => false, "message" => "Required fields are missing."]);
        exit;
    }

    // Generate a unique parcel tracking ID
    $parcel_tracking_id = "P" . time(); // Using timestamp for uniqueness

    // Prepare an INSERT statement
    $stmt = $conn->prepare("INSERT INTO parcels (parcel_tracking_id, sender_name, sender_phone, sender_address, receiver_name, receiver_phone, receiver_address, pickup_point, drop_off_point) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssssss", $parcel_tracking_id, $sender_name, $sender_phone, $sender_address, $receiver_name, $receiver_phone, $receiver_address, $pickup_point, $drop_off_point);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Parcel created successfully", "parcel_tracking_id" => $parcel_tracking_id]);
    } else {
        echo json_encode(["success" => false, "message" => "Error creating parcel: " . $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Invalid request method. Please use POST."]);
}

$conn->close();
?>
