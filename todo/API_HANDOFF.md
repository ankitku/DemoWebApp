# API Handoff Document: Greeting Service

Welcome to the Greeting Service! This document provides all the necessary information for the UI team to interact with the backend API.

## Local Setup

- **Backend URL**: `http://localhost:8080`
- **Frontend URL**: The backend is configured to accept requests from `http://localhost:3000`.
- **Interactive API Docs (Swagger UI)**: For live testing and exploration of the API, you can visit http://localhost:8080/swagger-ui.html while the backend is running.

---

## Data Models

### Greeting Object

This is the main data object returned by the API.

| Field       | Type     | Description                                  |
| ----------- | -------- | -------------------------------------------- |
| `id`        | Long     | The unique identifier for the greeting.      |
| `message`   | String   | The content of the greeting message.         |
| `createdAt` | ISO Date | The timestamp when the greeting was created. |

**Example:**

```json
{
  "id": 1,
  "message": "Hello, World!",
  "createdAt": "2024-10-27T10:15:30.123456"
}
```

### Greeting DTO (Data Transfer Object)

This is the object used when creating or updating a greeting.

| Field     | Type   | Description                          |
| --------- | ------ | ------------------------------------ |
| `message` | String | The content of the greeting message. |

**Example:**

```json
{
  "message": "A new greeting."
}
```

---

## API Endpoints

### 1. Get All Greetings

Retrieves a list of all greeting messages.

- **Method**: `GET`
- **Endpoint**: `/greetings`
- **Request Body**: None
- **Success Response**:

  - **Code**: `200 OK`
  - **Content**: An array of Greeting objects.

  ```json
  [
    {
      "id": 1,
      "message": "First greeting",
      "createdAt": "2024-10-27T10:15:30.123456"
    },
    {
      "id": 2,
      "message": "Second greeting",
      "createdAt": "2024-10-27T10:16:45.789012"
    }
  ]
  ```

### 2. Create a New Greeting

Adds a new greeting to the database.

- **Method**: `POST`
- **Endpoint**: `/greetings`
- **Request Body**: A Greeting DTO.

  ```json
  {
    "message": "This is a brand new greeting!"
  }
  ```

- **Success Response**:

  - **Code**: `201 Created`
  - **Content**: The newly created Greeting object.

  ```json
  {
    "id": 3,
    "message": "This is a brand new greeting!",
    "createdAt": "2024-10-27T10:18:00.555555"
  }
  ```

### 3. Get a Single Greeting by ID

Retrieves a specific greeting by its unique ID.

- **Method**: `GET`
- **Endpoint**: `/greetings/{id}`
- **Request Body**: None
- **Success Response**:

  - **Code**: `200 OK`
  - **Content**: The requested Greeting object.

  ```json
  {
    "id": 1,
    "message": "First greeting",
    "createdAt": "2024-10-27T10:15:30.123456"
  }
  ```

- **Error Response**:
  - **Code**: `404 Not Found` (If the ID does not exist)

### 4. Update a Greeting

Updates the message of an existing greeting.

- **Method**: `PUT`
- **Endpoint**: `/greetings/{id}`
- **Request Body**: A Greeting DTO with the new message.

  ```json
  {
    "message": "This is the updated message."
  }
  ```

- **Success Response**:

  - **Code**: `200 OK`
  - **Content**: The updated Greeting object.

  ```json
  {
    "id": 1,
    "message": "This is the updated message.",
    "createdAt": "2024-10-27T10:15:30.123456"
  }
  ```

- **Error Response**:
  - **Code**: `404 Not Found` (If the ID does not exist)

### 5. Delete a Greeting

Deletes a greeting from the database.

- **Method**: `DELETE`
- **Endpoint**: `/greetings/{id}`
- **Request Body**: None
- **Success Response**:
  - **Code**: `204 No Content`
- **Error Response**:
  - **Code**: `404 Not Found` (If the ID does not exist)
