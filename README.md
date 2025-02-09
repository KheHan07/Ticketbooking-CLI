# Ticketbooking-CLI
Real-Time Event Ticketing System built in Java, focusing on the Producer-Consumer Pattern with multi-threading for concurrency control and synchronization. The system efficiently manages multiple vendors (producers) and customers (consumers) concurrently, utilizing a shared ticket pool.


Real-Time Ticket Booking System (CLI)
    A Java-based command-line application for managing a real-time event ticketing system. 
    This project demonstrates the use of Object-Oriented Programming (OOP), multithreading,
    and synchronization to create a robust and interactive ticket management system.

Features
    Configure System: Set up the ticket pool, vendor, and customer behavior.
    Real-Time Multithreading: Vendors add tickets, and customers retrieve tickets in real-time.
    Status Monitoring: View the current status of tickets in the system.
    File-Based Configuration: Save and load system configurations to/from files.
    Graceful Shutdown: Stop the system safely and release resources.

Setup Instructions
    Prerequisites
        Java Development Kit (JDK): Ensure JDK 8 or later is installed.
        IDE/Text Editor: Any Java-compatible IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code.
        Command Line: You can run the application directly using a terminal or command prompt.
    Steps
        Clone or download the project repository to your local machine.
        Open the project in your preferred IDE.
        Compile the project:        javac Main.java
        Run the application:        java Main

Usage Instructions
    Launch the Application:
        Run the Main class to start the CLI application.
        You will see:
            Welcome to the Real-Time Ticketing System CLI!
            Please enter a command (configure, start, stop [st], view status [status], save configuration [save], or exit):
    Commands:
        configure: Set up the ticket pool and system parameters (e.g., total tickets, release rate).
        start: Begin the ticketing system with vendors and customers running in real-time.
        st or stop: Safely stop the system.
        status: View the current number of available tickets.
        save: Save the system configuration to a file.
        exit: Exit the application.
    Follow prompts for each command to input system configurations or actions.

Class Descriptions
    Main
        Purpose: Entry point of the application. Handles user input and commands for configuring and managing the system.
        Key Methods:
            main(): Starts the CLI, processes user commands like configure, start, stop, etc.
            saveConfiguration(): Saves the system configuration to a file.
    TicketService
        Purpose: Core service for managing vendors, customers, and the ticket pool.
        Key Methods:
            configure(): Configures the system with tickets, vendors, and customers.
            startSystem(): Starts multithreading for vendors and customers.
            stopSystem(): Stops the system gracefully.
            printStatus(): Displays the current ticket pool status.
    TicketPool
        Purpose: Manages the ticket inventory, ensuring thread safety.
        Key Methods:
            addTickets(): Adds tickets to the pool (used by vendors).
            removeTicket(): Removes tickets from the pool (used by customers).
            getAvailableTickets(): Returns the current number of tickets available.
    Vendor
        Purpose: Simulates a vendor that adds tickets to the system in real-time.
        Key Methods:
            run(): Adds tickets at a defined rate.
            stop(): Stops the vendor thread.
    Customer
        Purpose: Simulates a customer that retrieves tickets from the system.
        Key Methods:
            run(): Retrieves tickets at a defined rate.
            stop(): Stops the customer thread.

