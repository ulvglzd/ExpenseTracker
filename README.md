# Expense Tracker App

Welcome to the Expense Tracker App repository! It is a simple web application which allows users to manage and track their expenses. 

## Demo Video

Check out this video to see a live demonstration of the Expense Tracker app:
[Expense Tracker Demo](https://youtu.be/IlUs0ESfFK4)

## Features

- Adding, updating, deleting operations for expenses.
- View summaries of expenses in a table, and a summary of the total amount spent.
- Filtering expenses by year, month, and expense type.
- Pagination
- Customize your expense categories
- Download expenses as CSV file.
- Responsive design 

## Technologies Used

- Java 17.0
- Spring Boot 3
- Spring Data JPA (for data access)
- Thymeleaf (for server-side templating)
- MySQL (as the database)
- Maven (for build and dependency management)
- HTML, CSS, Bootstrap
- Server is Tomcat, built-in server of Spring Boot.

## Getting Started

1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE.
3. Make sure you have MySQL installed on your machine
4. Create a MySQL database named `budgetpal` or update the database configuration in `src/main/resources/application.properties` with your own database settings.
6. Build and run the application using Maven via `mvn spring-boot:run` command or via IDE.
7. Access the app in your browser at `http://localhost:9191`. You can change the serverport in application.properties file.



