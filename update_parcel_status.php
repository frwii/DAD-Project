<?php

include 'db_connect.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // --- NEW --- Get the courier_id from the POST data
    $parcel_id = $_POST['parcel_id'] ?? '';
    $status = $_POST['status'] ?? '';
    $courier_id = $_POST['courier_id'] ?? ''; // <-- NEW

    // --- NEW --- Add validation for the courier_id
    if (empty($parcel_id) || empty($status) || empty($courier_id) || !is_numeric($courier_id)) {
        echo json_encode(["success" => false, "message" => "Parcel ID, status, and a valid courier ID are required."]);
        exit;
    }
    
    // --- UPDATED --- The SQL query now also updates the 'assigned_courier_id' column
    $stmt = $conn->prepare("UPDATE parcels SET status = ?, assigned_courier_id = ? WHERE parcel_tracking_id = ?");
    
    // --- UPDATED --- Bind the new courier_id parameter. The types are "s"tring, "i"nteger, "s"tring
    $stmt->bind_param("sis", $status, $courier_id, $parcel_id);

    if ($stmt->execute()) {
        if ($stmt->affected_rows > 0) {
            echo json_encode(["success" => true, "message" => "Parcel status updated successfully."]);
        } else {
            // This can happen if the parcel ID doesn't exist.
            echo json_encode(["success" => false, "message" => "Parcel not found or no changes were made."]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Error updating status: " . $stmt->error]);
    }

    $stmt->close();
} else {
     echo json_encode(["success" => false, "message" => "Invalid request method"]);
}
$conn->close();
?>