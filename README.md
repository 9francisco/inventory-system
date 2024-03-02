# Inventory System

## Overview
The Inventory System is a Java application built using Java Swing for the graphical user interface and JDBC for database interaction. It allows users to manage warehouses and products, as well as generate reports based on the inventory data.

## Features
- **Warehouse Management:** Add, update, and delete warehouses with defined minimum and maximum product levels.
- **Product Management:** Add, update, and delete products assigned to warehouses.
- **Report Generation:** View reports detailing product information and warehouse status.

## Requirements
- Java 1.8
- Eclipse IDE
- JDBC driver compatible with your chosen database (MySQL, Oracle, etc.)

## Project Structure
The project follows the MVC (Model-View-Controller) design pattern:
- **Model:** Contains Java classes representing entities such as warehouses and products.
- **View:** User interface components implemented using Java Swing.
- **Controller:** Handles user interactions and communicates with the model and view.

## Usage
1. Clone the repository to your local machine.
2. Import the project into your preferred Java IDE.
3. Configure the database connection details in the `config.properties` file.
4. Build and run the application.