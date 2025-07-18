-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 18, 2025 at 07:11 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smartparcel_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `parcels`
--

CREATE TABLE `parcels` (
  `id` int(11) NOT NULL,
  `parcel_tracking_id` varchar(20) NOT NULL,
  `sender_name` varchar(100) NOT NULL,
  `sender_phone` varchar(20) NOT NULL,
  `sender_address` text NOT NULL,
  `receiver_name` varchar(100) NOT NULL,
  `receiver_phone` varchar(20) NOT NULL,
  `receiver_address` text NOT NULL,
  `pickup_point` varchar(255) NOT NULL,
  `drop_off_point` varchar(255) NOT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'Pending',
  `assigned_courier_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `parcels`
--

INSERT INTO `parcels` (`id`, `parcel_tracking_id`, `sender_name`, `sender_phone`, `sender_address`, `receiver_name`, `receiver_phone`, `receiver_address`, `pickup_point`, `drop_off_point`, `status`, `assigned_courier_id`, `created_at`, `updated_at`) VALUES
(1, 'P001', 'Ali', '0123456709', '123 Jalan Merdeka, KL', 'Siti', '0198765432', '456 Jalan Maju, Shah Alam', 'HQ', 'Shah Alam', 'Delivered', 1, '2025-07-18 12:40:34', '2025-07-18 13:10:00'),
(2, 'P002', 'Ahmad', '0112233445', '789 Lorong Damai, Johor', 'John', '0134567890', '101 Persiaran Gurney, Penang', 'Johor', 'Penang', 'Delivered', 1, '2025-07-18 12:40:34', '2025-07-18 12:50:27'),
(3, 'P003', 'Zara', '0176543210', '22 Jalan Bunga, Kuantan', 'Lim', '0167890123', '33 Jalan Raja, Ipoh', 'Kuantan', 'Ipoh', 'Delivered', 2, '2025-07-18 12:40:34', '2025-07-18 12:40:34'),
(4, 'P1752814160', 'John Tan', '0123456789', '10, Jalan Merdeka', '50000 Kuala Lumpur', 'Sarah Lim', '0198765432', '25, Lorong Maju, 46200 Petaling Jaya', 'KLCC Post Office', 'Pending', NULL, '2025-07-18 12:49:20', '2025-07-18 12:49:20');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` enum('courier','admin') NOT NULL DEFAULT 'courier',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password_hash`, `role`, `created_at`) VALUES
(1, 'courier1', 'pass123', 'courier', '2025-07-17 16:30:42'),
(2, 'courier2', 'pass456', 'courier', '2025-07-17 16:30:42');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `parcels`
--
ALTER TABLE `parcels`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `parcel_tracking_id` (`parcel_tracking_id`),
  ADD KEY `assigned_courier_id` (`assigned_courier_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `parcels`
--
ALTER TABLE `parcels`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `parcels`
--
ALTER TABLE `parcels`
  ADD CONSTRAINT `parcels_ibfk_1` FOREIGN KEY (`assigned_courier_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
