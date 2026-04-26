# 🎾 Tennis Scoreboard

A web application for tracking tennis matches. Allows creating new matches, recording scores during play, and viewing completed matches.

## 📋 Overview

Tennis Scoreboard is a web application for keeping track of tennis matches. Players can create matches, track scores (points, games, sets), and view the history of completed matches with pagination and filtering by player name.

## 🌐 Endpoints

### New Match

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/new-match` | Form for creating a new match |
| `POST` | `/new-match` | Create a match (parameters: player1, player2) |

### Match Score

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/match-score?uuid={uuid}` | View current match score |
| `POST` | `/match-score?uuid={uuid}` | Record a point for a player (parameter: playerId) |

### Completed Matches

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/matches` | List of completed matches |
| `GET` | `/matches?page=2` | Pagination (page 2) |
| `GET` | `/matches?filter_by_player_name=John` | Filter by player name |

## 🗄️ Database

The project uses H2 (in-memory database).

### players Table

| Column | Type | Description |
|--------|------|-------------|
| ID | INT | Player ID, auto-increment, primary key |
| NAME | VARCHAR | Unique player name |

### matches Table

| Column | Type | Description |
|--------|------|-------------|
| ID | INT | Match ID, auto-increment, primary key |
| PLAYER1 | INT | First player ID, foreign key |
| PLAYER2 | INT | Second player ID, foreign key |
| WINNER | INT | Winner ID, foreign key (can be NULL) |

## 🎯 Score Calculation Logic

The application implements standard tennis scoring rules:

1. **Points:** 0 → 15 → 30 → 40 → Game
2. **Games:** Won when player has 4 points with at least 2-point lead
3. **Sets:** Won when player has 6 games with at least 2-game lead
4. **Match:** Won when player wins 2 sets

## 🚀 Installation and Deployment

The application is compiled as a `.war` artifact and is designed to run in a servlet container (e.g., Apache Tomcat).

### Requirements

- Java 17+
- Maven
- Apache Tomcat 10.x (Jakarta EE 10)

### Running the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/CTY6A/tennis-scoreboard.git
   cd tennis-scoreboard
   ```

2. Build the `.war` artifact with Maven:
   ```bash
   mvn clean package
   ```

3. Copy the built file from `target/tennis-scoreboard-1.0.war` to your Tomcat's `webapps/` folder.

4. Start Tomcat.

5. The application will be available at: `http://localhost:8080/tennis-scoreboard-1.0`

## 🛠️ Technologies

- **Java 17**
- **Maven**
- **Jakarta Servlets 6.0 (Tomcat 10+)**
- **Hibernate ORM 6.5.2**
- **H2 Database**
- **MapStruct**
- **Lombok**
- **JSTL**
- **JSP**

## 📁 Project Structure

```
├───main
│   ├───java
│   │   └───com.stubedavd
│   │       ├───exception              // Custom exceptions
│   │       ├───filter                 // Servlet filters
│   │       ├───listener               // Application initialization
│   │       ├───util                   // Utilities
│   │       ├───match                  // Match module
│   │       │   ├───controller         // Controllers (servlets)
│   │       │   ├───dto                // Match DTOs
│   │       │   │   └───response       // Response DTOs
│   │       │   ├───entity             // Database entities
│   │       │   ├───mapper             // MapStruct mappers
│   │       │   ├───model              // Score models
│   │       │   ├───repository         // Repository (DB access)
│   │       │   └───service            // Business logic
│   │       └───player                 // Player module
│   │           ├───dto                // Player DTOs
│   │           │   └───request        // Request DTOs
│   │           ├───entity             // Player entity
│   │           ├───mapper             // Player mappers
│   │           └───repository         // Player repository
│   ├───resources
│   │   ├───hibernate.cfg.xml          // Hibernate configuration
│   │   └───import.sql                 // SQL script for DB initialization
│   └───webapp                         // Web context
│       ├───WEB-INF
│       │   └───jsp                    // JSP pages
│       ├───css                        // Styles
│       ├───images                     // Images
│       ├───js                         // JavaScript
│       └───index.jsp                  // Main page