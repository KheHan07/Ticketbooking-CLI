import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        TicketService ticketService = new TicketService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        ExecutorService executorService = Executors.newCachedThreadPool();

        System.out.println("Welcome to the Real-Time Ticketing System CLI!");
        System.out.println("Use the following commands to interact with the system:");
        System.out.println("1. 'configure' - Set up the system (e.g., total tickets, rates)");
        System.out.println("2. 'start' - Start the ticketing system");
        System.out.println("3. 'st' or 'stop' - Stop the system");
        System.out.println("4. 'status' - View the current ticket pool status");
        System.out.println("5. 'save' - Save the system configuration to a file");
        System.out.println("6. 'exit' - Exit the application");

        // Declare variables to store configuration globally
        int totalTickets = 0;
        int ticketReleaseRate = 0;
        int customerRetrievalRate = 0;
        int maxTicketCapacity = 0;

        while (running) {
            System.out.print("\nEnter a command: ");
            String command = scanner.next().toLowerCase();

            switch (command) {
                case "configure":
                    System.out.println("Configuring the ticketing system...");
                    boolean validInput = false;

                    while (!validInput) {
                        try {
                            System.out.print("Enter the total number of tickets: ");
                            totalTickets = scanner.nextInt();
                            System.out.print("Enter the ticket release rate (tickets per second): ");
                            ticketReleaseRate = scanner.nextInt();
                            System.out.print("Enter the customer retrieval rate (tickets per second): ");
                            customerRetrievalRate = scanner.nextInt();
                            System.out.print("Enter the maximum ticket pool capacity: ");
                            maxTicketCapacity = scanner.nextInt();

                            if (totalTickets > 0 && ticketReleaseRate > 0 && customerRetrievalRate > 0 && maxTicketCapacity > 0) {
                                ticketService.configure(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                                System.out.println("Configuration completed successfully!");
                                validInput = true;
                            } else {
                                System.out.println("Error: All values must be greater than 0. Please try again.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: Invalid input. Please enter numeric values.");
                            scanner.next(); // clear invalid input
                        }
                    }
                    break;

                case "start":
                    System.out.println("Starting the ticketing system...");
                    ticketService.startSystem(executorService);
                    System.out.println("System is now running!");
                    break;

                case "st":
                case "stop":
                    System.out.println("Stopping the ticketing system...");
                    ticketService.stopSystem();
                    System.out.println("System has been stopped successfully!");
                    running = false;
                    executorService.shutdown();
                    break;

                case "status":
                    System.out.println("Fetching the current ticket pool status...");
                    ticketService.printStatus();
                    break;

                case "save":
                    System.out.print("Enter file name to save configuration: ");
                    String saveFileName = scanner.next();
                    saveConfiguration(saveFileName, totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                    break;

                case "exit":
                    System.out.println("Exiting the application. Goodbye!");
                    running = false;
                    executorService.shutdown();
                    break;

                default:
                    System.out.println("Error: Invalid command. Please try again.");
            }
        }
        scanner.close();
    }

    private static void saveConfiguration(String fileName, int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("totalTickets=" + totalTickets + "\n");
            writer.write("ticketReleaseRate=" + ticketReleaseRate + "\n");
            writer.write("customerRetrievalRate=" + customerRetrievalRate + "\n");
            writer.write("maxTicketCapacity=" + maxTicketCapacity + "\n");
            System.out.println("Configuration saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error: Failed to save configuration. " + e.getMessage());
        }
    }
}
