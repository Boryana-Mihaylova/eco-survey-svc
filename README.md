# 🌿 ecoSurvey-svc

`ecoSurvey-svc` is a standalone REST API microservice that allows users to express support for key environmental topics. It is part of the **FriendlyLend** project and is designed to promote awareness and sustainable behavior through a simple voting mechanism.

---

## 📘 About the Service

This service exposes endpoints for:

- Submitting a vote for one of three predefined ecological topics
- Retrieving total vote statistics across all topics

Each vote is stored in a dedicated database. The microservice is designed to be lightweight, focused, and easily integrable into larger systems via REST.

---

## 🧠 Use Case

The service is integrated with the [FriendlyLend App](https://github.com/your-username/friendly-lend-app), where authenticated users can:

- View three available topics: **SUSTAINABILITY**, **ENVIRONMENT**, and **RECYCLING**
- Submit their support for one topic they most strongly identify with (one vote per user)
- View aggregated voting statistics (admin access only)

### ✅ Advantages

- **Collects real opinions** – Provides insight into public attitudes and interest in ecological topics
- **Encourages sustainable behavior** – Analyzing user support helps promote responsible choices in daily life

---

## 🔗 Endpoints

| Method | Endpoint                   | Description                        |
|--------|----------------------------|------------------------------------|
| `POST` | `/api/surveys/vote`        | Submit a vote (topic + user ID)   |
| `GET`  | `/api/surveys/stats`       | Get total votes per topic         |

---

## 🛠️ Technologies

- Java 17
- Spring Boot
- Spring Web (REST)
- Spring Data JPA
- Hibernate
- MySQL
- Maven

---

## 🧱 Architecture

- **Controller Layer** – Handles HTTP requests and validations
- **Service Layer** – Business logic for voting and statistics
- **Repository Layer** – JPA repositories for persistence
- **Model** – Simple entity classes for user votes

---

## ⚙️ Running the Service

### 1. Clone the repository:
```bash
git clone https://github.com/your-username/ecoSurvey-svc.git
cd ecoSurvey-svc

2. Configure your database credentials:
Edit src/main/resources/application.properties:

spring.datasource.username=your_user
spring.datasource.password=your_password


3. Run the application:
mvn spring-boot:run

The service will be available at:
http://localhost:8081

🚀 Sample JSON Payload
{
  "subject": "SUSTAINABILITY",
  "support": "SUPPORT",
  "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
}

👩‍💻 Author
Developed as part of the final project in the Spring Advanced course at SoftUni (2025), in support of the FriendlyLend application.

📄 License
This project is licensed under the MIT License – see the LICENSE file for details.