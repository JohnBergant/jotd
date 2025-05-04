# Joke of the Day (JOTD) Service

A Spring Boot application that serves jokes, including a daily featured jokeDocument. This service provides a RESTful API for creating, retrieving, updating, and deleting jokes, with special endpoints for random jokes and the jokeDocument of the day.

## Features

- 🌞 **Joke of the Day**: Automatically selects and caches a daily jokeDocument
- 🎲 **Random Jokes**: Retrieve random jokes from the entire collection or by category
- 🔍 **Category Filtering**: Find jokes by specific categories
- 🔒 **Secured Endpoints**: Authentication for content management
- 📝 **Full CRUD Operations**: Create, read, update, and delete jokes
- 📊 **Actuator Endpoints**: Monitor application health and metrics
- 📚 **OpenAPI Documentation**: Interactive API documentation with Swagger UI

## Technologies

- **Spring Boot 3.4.5**: Application framework
- **Spring Security**: Authentication and authorization
- **Spring Data MongoDB**: Data persistence
- **Spring Boot Actuator**: Monitoring and metrics
- **Lombok**: Boilerplate code reduction
- **OpenAPI/Swagger**: API documentation
- **MongoDB**: Document database

## Prerequisites

- Java 17 or higher
- Gradle 8.x (or use the included Gradle wrapper)
- MongoDB 5.x or higher (local installation or MongoDB Atlas)

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/jotd.git
cd jotd
```

### 2. Configure MongoDB

Make sure MongoDB is running locally on port 27017, or update `application.properties` with your MongoDB connection details.

For a local MongoDB installation:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=jotddb
```

Make sure you run the mongo docker image

```bash
docker compose up -d 
```

For MongoDB Atlas:

### 3. Build the application

```bash
./gradlew clean build
```

## Running the Application

Start the application using Gradle:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The application will be available at `http://localhost:8080`.

### Default Credentials

To make elevated API Requests and API Key is needed. A default one is setup in the properties file.

**Note**: This should only be used for development purposes
Before going to production this should be replaced with an authentication method that does not expose the secret.
HMAC signing would be a good option or Bearer auth using signed JWTs.

**Note**: These should be changed in production environments.

## API Documentation

The API documentation is available via Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

### API Endpoints Overview

| Method | Endpoint                       | Description                         | Access          |
|--------|--------------------------------|-------------------------------------|-----------------|
| GET    | /api/jokes/{id}                | Get jokeDocument by ID                      | Authenticated   |
| GET    | /api/jokes/daily               | Get jokeDocument of the day                 | Public          |
| POST   | /api/jokes                     | Create a new jokeDocument                   | Authenticated   |
| PUT    | /api/jokes/{id}                | Update a jokeDocument                       | Authenticated   |
| DELETE | /api/jokes/{id}                | Delete a jokeDocument                       | Authenticated   |

## Security

The application uses Spring Security with API Key based Authentication. Two levels of access are provided:

1. **Public Access**:
   - Joke of the day

2. **Authenticated Access**:
   - All CRUD operations on jokes

To secure production deployments:
- Change default credentials
- Use HTTPS
- Consider implementing JWT for stateless authentication
- Restrict CORS settings to trusted domains

## Development Guidelines

### Implementing new features

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Running tests

```bash
./gradlew test
```
### API Tests

[http tests](http/controller-tests.http)

These can be ran in Intellij or by downloading the Intellij Http Client [here](https://www.jetbrains.com/help/idea/http-client-cli.html).

## Project Structure

API - All the controllers and request/response DTOs
Domain - Business Logic
Infrastructure - Interface with the datastore

## License

This project is licensed under the MIT License - see the LICENSE file for details.

