# 📦 Smart Parcel System – A Distributed Parcel Delivery Management Application

## Introduction

### Project Overview
Smart Parcel System is a distributed application designed to manage parcel delivery operations efficiently by separating responsibilities across multiple applications. This project was developed for the Distributed Application Development course (BITP 3123) and simulates a real-world courier service environment.

The system consists of:
- **Parcel Request App**: For staff to request parcel delivery by entering sender and receiver details.
- **Courier App**: For couriers to log in, view assigned parcels, and update their status.
- **Backend REST API**: Provides endpoints for user authentication, parcel management, and real-time status updates.
- **MySQL Database**: Stores users and parcel data securely.

This architecture promotes modularity, real-time data synchronization, and scalability.

---

## 🏗️ System Architecture

### High-Level Architecture Diagram
[Parcel Request App] ←→ [ RESTful Backend API ] ←→ [MySQL Database] ←→ [Courier App]

### Components

- **Frontend Applications**:
  - `Parcel Request App`: Allows staff to create delivery requests.
  - `Courier App`: Allows couriers to log in and manage parcel delivery status.
- **Backend**:
  - REST API built with Spring Boot
  - Handles parcel creation, status updates, login authentication
- **Database**:
  - MySQL with two main tables: `users`, `parcels`

---
## 🔑 Key Functionalities

| Module             | Description                                 |
|--------------------|---------------------------------------------|
| Create Parcel      | Staff inputs sender & receiver info         |
| Courier Login      | Courier logs in using credentials           |
| View Assigned      | Courier sees parcels marked "Pending"       |
| Update Status      | Courier marks parcels as "Delivered"        |
| Track Parcel       | Staff views status using parcel ID          |

---
## 🖥️ Backend Application

### Technology Stack
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: MySQL
- **Testing Tool**: Postman
- **IDE**: Eclipse

### API Documentation

| Endpoint            | Method | Description                         |
|---------------------|--------|-------------------------------------|
| `/api/login`        | POST   | Courier login using credentials     |
| `/api/parcels`      | GET    | View all parcels                    |
| `/api/parcels/:id`  | GET    | View parcel by ID                   |
| `/api/parcels`      | POST   | Create new parcel delivery request  |
| `/api/parcels/:id`  | PUT    | Update parcel status                |

