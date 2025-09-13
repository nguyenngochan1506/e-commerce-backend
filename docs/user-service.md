# User Service Documentation

## Overview
The User Service is responsible for managing user accounts, including registration, authentication, profile management, and user preferences. It provides a RESTful API for interacting with user data.
## Features
- Authentication & Authorization
- User management


## API Endpoints
### Authentication
- `POST /api/v1/auth/register`: Register a new user
- `POST /api/v1/auth/login`: Authenticate a user and return a JWT token
- `POST /api/v1/auth/logout`: Invalidate the user's session
- `POST /api/v1/auth/refresh`: Refresh the JWT token
- `POST /api/v1/auth/forgot-password`: Initiate password reset process
- `POST /api/v1/auth/reset-password`: Reset user password
- `PATCH /api/v1/auth/change-password`: Change user password
- `POST /api/v1/auth/verify-email`: Verify user email address

### Authorization Management (admin only)
- `GET /api/v1/permissions`: Get a list of all permissions
- `GET /api/v1/roles`: Get a list of all roles
- `POST /api/v1/roles`: Create a new role
- `PATCH /api/v1/roles/{id}`: Update a role by ID
- `DELETE /api/v1/roles/{id}`: Delete a role by ID

### User Management
- `PATCH /api/v1/users/me`: Update current user profile
- `GET /api/v1/users`: Get a list of all users (admin only)
- `GET /api/v1/users/{id}`: Get user details by ID
- `PATCH /api/v1/users/{id}`: Update user details by ID
- `DELETE /api/v1/users/{id}`: Delete user by ID (admin only)
- `DELETE /api/v1/users/batch`: Batch delete users (admin only)

### User Profile
- `GET /api/v1/addresses/me`: Get all addresses for the current user
- `POST /api/v1/addresses/me`: Create a new address for the current user
- `PATCH /api/v1/addresses/me/{id}`: Update an address for the current user
- `DELETE /api/v1/addresses/me/{id}`: Delete an address for the current user
- `GET /api/v1/addresses/me/default`: Get the default address for the current user


## Stages
### Stage 1:
#### Implement user registration
- Endpoint: `POST /api/v1/auth/register`

**Request Body** 
```json
{
  "username": "string",
  "email": "string",
  "phoneNumber": "string",
  "password": "string",
  "fullName": "string",
  "gender": "string", // optional
  "dateOfBirth": "string", // optional, format: dd-MM-yyyy
  "avatar": "string"  //optional
}
```

- Endpoint: `POST /api/v1/auth/login`
**Request Body**
```json
{
  "identifier": "string", // username or email
  "password": "string"
}
```