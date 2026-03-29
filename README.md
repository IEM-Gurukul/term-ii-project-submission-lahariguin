[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/pG3gvzt-)
# PCCCS495 – Term II Project

## Project Title
Hotel Booking System
---

## Problem Statement (max 150 words)
The project aims to develop a terminal-based hotel booking system that allows users to search for available rooms, make reservations, view booking details, and cancel bookings efficiently. Many small hotels or budget accommodations lack affordable digital systems and still rely on manual booking processes, which can lead to errors, double bookings, and poor record management. This system provides a simple, cost-effective solution using Java and Object-Oriented Programming principles. It ensures organized data handling, reduces human error, and improves the booking experience for both customers and administrators. The application will run entirely in the terminal, making it lightweight and accessible without requiring complex graphical interfaces.
---

## Target User
Small hotel owners or managers
Reception staff
Customers (basic interaction through terminal interface)
---

## Core Features
Room availability checking
Book room (check-in & check-out management)
Cancel booking
View booking details
Customer information management
Basic billing calculation
- 
- 
- 

---

## OOP Concepts Used

Abstraction:
Abstract classes/interfaces like User or Room to define common behavior.
Inheritance:
Classes like Customer and Admin inherit from a base User class.
Polymorphism:
Method overriding (e.g., different booking behaviors for room types like Deluxe, Standard).
Exception Handling:
Handles invalid input, booking conflicts, and runtime errors using try-catch blocks.
Collections / Threads:
Uses collections like ArrayList or HashMap to store room and booking data.
(Optional) Threads for handling multiple bookings simultaneously.

---

## Proposed Architecture Description
The system follows a modular architecture:

Main Class: Entry point with menu-driven interface
Model Classes: Room, Customer, Booking
Service Classes: BookingService, RoomService for business logic
Data Storage: In-memory storage using collections (can be extended to file/database later)

Flow:
User → Menu → Service Layer → Data Storage → Output
---

## How to Run
Install Java (JDK 8 or above)

Compile the program:

javac Main.java

Run the program:

java Main
Follow on-screen menu options to use the system
---

## Git Discipline Notes
Minimum 10 meaningful commits required.
