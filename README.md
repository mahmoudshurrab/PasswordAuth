# PasswordAuth

A simple Java program for password authentication using SHA-256 hashing and a random salt.

## Project Description

This project was developed for the Software Security course. It demonstrates how passwords can be hashed and how user credentials can be stored and verified using Java.

## Features

- Register new users with a username and password.
- Generate a random salt for each user.
- Use SHA-256 to hash the password combined with the salt.
- Store the username, salt, and password hash in a text file.
- Verify user credentials during login.
- Display a message for successful or failed login attempts.
- Load stored user data when the program starts.

## Technologies Used

- Java 21
- IntelliJ IDEA
- SHA-256
- SecureRandom
- Text file storage

## How to Run

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Make sure Java 21 is configured.
4. Open `PasswordAuthentication.java`.
5. Run the `main()` method.
6. Choose an option from the menu.

## Program Menu

```text
=== Password Authentication ===
1. Register
2. Login
3. Exit
Choose an option:
```

## Data Storage

The program stores user information in a file named `users.txt`.

Each record contains:

- Username
- Random salt
- SHA-256 password hash

The original password is not stored in the file.

## Testing

The program was tested with the following cases:

1. Registering a new user.
2. Logging in with the correct password.
3. Attempting to log in with an incorrect password.
4. Logging in after restarting the program.
5. Checking the stored user information in `users.txt`.

## Purpose

The purpose of this project is to understand password hashing, random salts, file-based storage, and basic user authentication.
