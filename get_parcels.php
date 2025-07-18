<?php
include 'db_connect.php';

// Base query
$sql = "SELECT id, parcel_tracking_id, sender_name, receiver_name, pickup_point, drop_off_point, status FROM parcels";
$params = [];
$types = "";

// Optional filtering by status
if (isset($_GET['status'])) {
    $sql .= " WHERE status = ?";
    $params[] = $_GET['status'];
    $types .= "s";
}

$sql .= " ORDER BY created_at DESC"; // Show newest first

$stmt = $conn->prepare($sql);

// Bind parameters if any exist
if (!empty($params)) {
    $stmt->bind_param($types, ...$params);
}

$stmt->execute();
$result = $stmt->get_result();

$parcels = [];
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $parcels[] = $row; // Add each parcel to the array
    }
}

echo json_encode(["success" => true, "data" => $parcels]);

$stmt->close();
$conn->close();
?>