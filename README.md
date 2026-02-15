# 🚖 RideBookingApp (QuickTrip)

> A horizontally scalable, event-driven ride-hailing backend built using modern distributed systems principles.

![Java](https://img.shields.io/badge/Java-21-orange) 
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-green) 
![gRPC](https://img.shields.io/badge/gRPC-Protobuf-blue) 
![Kafka](https://img.shields.io/badge/Apache_Kafka-Event_Driven-red) 
![Redis](https://img.shields.io/badge/Redis-GeoSpatial-red) 
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-ACID-blue)

---

# 📖 Overview

**RideBookingApp (QuickTrip)** is a distributed backend system inspired by real-world ride-hailing platforms.

The architecture separates:

- Ride Orchestration
- Driver Matching
- Trip Lifecycle Management
- Real-time Location Tracking
- Authentication & User Management

This separation enables independent scaling of high-load components such as matching and trip tracking.

---

# 🏗 System Architecture

## 🔹 High-Level Architecture

```mermaid
graph TD

    Client[Mobile / Web Client] -->|REST/HTTP| BFF[BFF Gateway]

    subgraph Core Microservices
        BFF -->|gRPC| AuthService
        BFF -->|gRPC| UserService
        BFF -->|gRPC| RideService
        BFF -->|gRPC| TripService
        BFF -->|gRPC| MatchingService
        BFF -->|gRPC| LocationService
    end

    RideService -->|Publish RideRequested| Kafka[(Apache Kafka)]
    Kafka -->|Consume| MatchingService
    MatchingService -->|Driver Assigned Event| Kafka
    Kafka -->|Consume| TripService

    subgraph Data Layer
        AuthService --> AuthDB[(PostgreSQL)]
        UserService --> UserDB[(PostgreSQL)]
        RideService --> RideDB[(PostgreSQL)]
        TripService --> TripDB[(PostgreSQL)]
        AuthService --> RedisAuth[(Redis Cache)]
        LocationService --> GeoRedis[(Redis GEO)]
    end
```

---

# 🔄 Ride → Match → Trip Flow

```mermaid
sequenceDiagram
    participant Client
    participant BFF
    participant Ride
    participant Matching
    participant Location
    participant Trip
    participant Kafka

    Client->>BFF: Create Ride Request
    BFF->>Ride: gRPC CreateRide()
    Ride->>Kafka: Publish RIDE_REQUESTED

    Kafka->>Matching: Consume Event
    Matching->>Location: Find Nearby Drivers
    Location-->>Matching: Driver List
    Matching->>Kafka: Publish DRIVER_ASSIGNED

    Kafka->>Trip: Consume DRIVER_ASSIGNED
    Trip->>Trip: Create Active Trip
    Trip-->>BFF: Trip Created
    BFF-->>Client: Confirmation
```

---

# 🧩 Microservices Breakdown

## 🔐 Auth Service
- JWT issuance & validation
- Redis token blacklisting
- Authentication middleware

## 👤 User Service
- Rider & Driver profiles
- Ride history
- Role management

## 🚘 Ride Service (Request Orchestrator)
- Creates ride requests
- Validates input
- Publishes `RIDE_REQUESTED` event
- Persists ride metadata

## 🎯 Matching Service
- Consumes `RIDE_REQUESTED`
- Fetches nearby drivers from LocationService
- Applies matching algorithm
- Publishes `DRIVER_ASSIGNED` event
- Designed for high scalability

## 🛣 Trip Service
- Consumes `DRIVER_ASSIGNED`
- Creates active trip
- Handles trip lifecycle:
  - STARTED
  - IN_PROGRESS
  - COMPLETED
  - CANCELLED
- Persists trip state
- Handles fare finalization

## 📍 Location Service
- High-frequency driver updates
- Redis GEO indexing
- Radius-based driver lookup

## 🌉 BFF Gateway
- Client entry point
- HTTP → gRPC translation
- Centralized error handling
- JWT validation

---

# 📊 Scalability Strategy

| Component | Scaling Strategy |
|------------|------------------|
| MatchingService | Horizontally scalable (stateless consumers) |
| TripService | Scaled per active trip load |
| RideService | Moderate scaling |
| LocationService | Redis cluster scaling |
| Kafka | Partition-based scaling |
| PostgreSQL | Service-level isolation |

---

# 🧠 Architectural Advantages

## Separation of Concerns

- RideService → Request creation
- MatchingService → Driver selection
- TripService → Active trip lifecycle

This prevents:

- Heavy matching logic blocking ride creation
- Trip state management interfering with matching
- Tight coupling between services

---

# 🏗 Infrastructure View

```mermaid
graph LR

    subgraph Clients
        Mobile
        Web
    end

    subgraph Application Layer
        BFF
        Auth
        User
        Ride
        Matching
        Trip
        Location
    end

    subgraph Messaging
        Kafka
    end

    subgraph Storage
        PostgreSQL
        Redis
    end

    Mobile --> BFF
    Web --> BFF

    BFF --> Ride
    BFF --> Trip
    BFF --> Auth
    BFF --> User
    BFF --> Matching
    BFF --> Location

    Ride --> Kafka
    Kafka --> Matching
    Matching --> Kafka
    Kafka --> Trip

    Ride --> PostgreSQL
    Trip --> PostgreSQL
    Auth --> PostgreSQL
    User --> PostgreSQL

    Auth --> Redis
    Location --> Redis
```

---

# 📦 Local Setup

```bash
git clone https://github.com/DhananjayHirey/RideBookingApp.git
cd RideBookingApp
docker-compose up
mvn clean install
```

Start in order:

1. AuthService  
2. UserService  
3. LocationService  
4. RideService  
5. MatchingService  
6. TripService  
7. BFF  

---

# 🔮 Future Enhancements

- Payment Service
- Notification Service
- Surge Pricing Engine
- Driver Rating System
- Kubernetes Deployment
- CI/CD Pipeline
- Distributed Tracing
- Circuit Breakers

---

# ❤️ Author

**Dhananjay Hirey**  
Backend & Distributed Systems Enthusiast  
Focused on building scalable architectures.
