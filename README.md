# MessengerPOE

A Java console application for user registration and login validation.

## Features

* User Registration: Validates username format, password complexity, and cell phone number format.
* User Login: Authenticates existing user credentials.
* Unit Testing: Contains tests for all validation, login and messaging methods.
* QuickChat (Part 2): After logging in, the user is welcomed to QuickChat and can send messages.
  * Choose how many messages to enter, then enter a recipient number (with international code) and a message of 250 characters or less.
  * Each message gets a random ten-digit ID and a message hash (e.g. `00:0:HITONIGHT`).
  * For each message the user can send it, disregard it, or store it. Stored messages are saved in `messages.json`.
  * The full details of each sent message and the total number of messages sent are displayed.
  * "Show recently sent messages" displays "Coming Soon."
* Automated tests: A GitHub Action (`.github/workflows/TestJava.yml`) runs all unit tests on every push.

## Requirements

* Java Development Kit (JDK)
* NetBeans IDE
* JUnit 4 library (junit-4.13.2.jar)
* Hamcrest library (hamcrest-core-1.3.jar)

## How to Setup and Run

1. Open the project in NetBeans.
2. Right-click **Test Libraries** in the left menu and add **JUnit 4** and **Hamcrest**.
3. Run `Main.java` to start the program.
4. Right-click `LoginTest.java` and select **Test File** to run the test suite.
5. To run the tests the same way GitHub does, use Maven: `mvn test` (uses `pom.xml`).
