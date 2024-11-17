# Citronix: Lemon Farm Management System 🍋

A comprehensive farm management application built for lemon orchards, enabling farmers to track production, harvest, and sales while optimizing tree productivity based on age. Developed with Java and following best practices in object-oriented programming and domain-driven design.

![Project Status](https://img.shields.io/badge/Status-Done-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)

## 📑 Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Key Features](#key-features)
- [Getting Started](#getting-started)
- [Development](#development)
- [Testing](#testing)
- [API Documentation](#api-documentation)

## Overview

Citronix is a farm management application designed to help lemon farmers track their farm, fields, trees, harvests, and sales. The system also calculates tree productivity based on their age, ensuring efficient farm operations. This tool provides insights into harvest volumes, sales revenues, and performance over time, all while facilitating detailed data management.

## Architecture

The project follows a layered architecture inspired by Domain-Driven Design (DDD), ensuring clear separation of concerns and modularity.

### Bounded Contexts:
- **Farm Management (Core Domain)**: Farm, fields, trees, harvests, and sales management.
- **Reporting and Analytics (Supporting Domain)**: Revenue and performance tracking.

## Tech Stack

### Core Technologies:
- **Java 21**
- **Spring Boot 3.2.0**
- **PostgreSQL 16**
- **Docker & Docker Compose**

### Development Tools & Practices:
- **Test-Driven Development (TDD)**
- **Domain-Driven Design (DDD)**
- **Maven**
- **JUnit 5**
- **AssertJ**

### API & Documentation:
- **Swagger UI** for interactive API documentation

## Key Features

- 🍋 **Farm Management**: Manage farm information including name, location, and area.
- 🌳 **Field & Tree Management**: Track fields, their sizes, and trees with automated productivity based on age.
- 📅 **Harvest Management**: Record and manage seasonal harvests with detailed tracking by tree.
- 💵 **Sales Management**: Track sales with prices, clients, and harvest details.
- 📊 **Performance Tracking**: Analyze farm performance and tree productivity over time.

## Getting Started

### Prerequisites:
- **JDK 21**
- **Docker & Docker Compose**

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/citronix.git
cd citronix
```

2. Build and start the application using Docker Compose:
```bash
make up
```

This will start the application and the PostgreSQL database in Docker containers. You can now access the application at `http://localhost:8080`.

3. To stop the application and clean up the Docker containers:
```bash
make down
```

This will stop the application and remove the containers.

## Development

### Project Structure

```
citronix/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/citronix/
│   │   │       ├── farm/
│   │   │       ├── harvest/
│   │   │       ├── sales/
│   │   │       └── tree/
│   │   └── resources/
│   └── test/
└── docker/
```

### API Endpoints

#### Farm Management:
```
# Farms
GET    /api/v1/farms                   # List all farms
POST   /api/v1/farms                   # Create a new farm
GET    /api/v1/farms/{id}              # Get farm by ID
PUT    /api/v1/farms/{id}              # Update farm information
DELETE /api/v1/farms/{id}              # Delete farm

# Fields
GET    /api/v1/farms/{id}/fields       # List fields for a farm
POST   /api/v1/farms/{id}/fields       # Create a new field
GET    /api/v1/fields/{id}             # Get field details
PUT    /api/v1/fields/{id}             # Update field details
DELETE /api/v1/fields/{id}             # Delete field

# Trees
GET    /api/v1/fields/{id}/trees       # List trees in a field
POST   /api/v1/trees                   # Create a new tree
GET    /api/v1/trees/{id}              # Get tree details
PUT    /api/v1/trees/{id}              # Update tree details
DELETE /api/v1/trees/{id}              # Delete tree
```

#### Harvest Management:
```
# Harvests
GET    /api/v1/harvests                # List all harvests
POST   /api/v1/harvests                # Record a new harvest
GET    /api/v1/harvests/{id}           # Get harvest details
PUT    /api/v1/harvests/{id}           # Update harvest details
DELETE /api/v1/harvests/{id}           # Delete harvest

# Harvest Details (per tree)
GET    /api/v1/harvests/{id}/trees     # List harvested trees for a given harvest
POST   /api/v1/harvests/{id}/trees     # Add harvested tree details
```

#### Sales Management:
```
# Sales
GET    /api/v1/sales                   # List all sales
POST   /api/v1/sales                   # Record a new sale
GET    /api/v1/sales/{id}              # Get sale details
PUT    /api/v1/sales/{id}              # Update sale details
DELETE /api/v1/sales/{id}              # Delete sale
```

Example Request/Response:

```json
# POST /api/v1/farms
Request:
{
    "name": "Citron Farm",
    "location": "Region A",
    "area": 500,
    "createdAt": "2024-01-01"
}

Response:
{
    "id": 1,
    "name": "Citron Farm",
    "location": "Region A",
    "area": 500,
    "createdAt": "2024-01-01"
}
```

### Tree Productivity Calculation:

- Young Tree (< 3 years): 2.5 kg per season
- Mature Tree (3-10 years): 12 kg per season
- Old Tree (> 10 years): 20 kg per season

## Testing

The project follows TDD practices. To run the tests:

```bash
# Run unit tests
./mvnw test

# Run integration tests
./mvnw verify -P integration-test
```

## API Documentation

Once the application is running, you can access the Swagger UI:
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Conclusion

Citronix offers a complete and efficient solution for managing lemon farms, from field management to tree productivity and sales tracking. This tool allows farmers to monitor their farm’s performance and optimize operations, ultimately improving their harvest yields and revenue.
