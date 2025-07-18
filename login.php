<?php

include 'db_connect.php';

// We expect a POST request with username and the password_hash
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'] ?? '';
    // We are now treating the incoming 'password' as the hash itself for comparison
    $submitted_hash = $_POST['password'] ?? '';

    if (empty($username) || empty($submitted_hash)) {
        echo json_encode(["success" => false, "message" => "Username and password hash are required."]);
        exit;
    }

    // Prepare statement to prevent SQL Injection
    $stmt = $conn->prepare("SELECT id, username, password_hash, role FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($user = $result->fetch_assoc()) {
        // User found, now directly compare the submitted hash with the one in the database.
        // --- INSECURE CHANGE IS HERE ---
        if ($submitted_hash === $user['password_hash']) {
            // Hashes match. This is NOT a secure way to verify a user.
            
            // IMPORTANT: Do not send the password hash back to the client.
            unset($user['password_hash']); 
            
            echo json_encode(["success" => true, "message" => "Login successful (INSECURE MODE)", "user" => $user]);
        } else {
            // Hashes do not match
            echo json_encode(["success" => false, "message" => "Invalid username or password hash"]);
        }
        // The original, secure way to do this is:
        // if (password_verify($password, $user['password_hash'])) { ... }
    } else {
        // No user found with that username
        echo json_encode(["success" => false, "message" => "Invalid username or password hash"]);
    }
    $stmt->close();
} else {
     echo json_encode(["success" => false, "message" => "Invalid request method"]);
}
$conn->close();
?>