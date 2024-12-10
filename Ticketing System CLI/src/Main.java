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

        int totalTickets = 0;
        int ticketReleaseRate = 0;
        int customerRetrievalRate = 0;
        int maxTicketCapacity = 0;
        while (running) {
            System.out.println("Please enter a command (configure, start, stop [st], view status [status], save configuration [save], or exit):");
            String command = scanner.next().toLowerCase();

            switch (command) {
                case "configure":
                    boolean validInput = false;

                    while (!validInput) {
                        try {
                            System.out.println("Enter total tickets:");
                            totalTickets = scanner.nextInt();
                            System.out.println("Enter ticket release rate (tickets per second):");
                            ticketReleaseRate = scanner.nextInt();
                            System.out.println("Enter customer retrieval rate (tickets per second):");
                            customerRetrievalRate = scanner.nextInt();
                            System.out.println("Enter maximum ticket capacity:");
                            maxTicketCapacity = scanner.nextInt();
                            if (totalTickets > 0 && ticketReleaseRate > 0 && customerRetrievalRate > 0 && maxTicketCapacity > 0) {
                                validInput = true;
                            } else {
                                System.out.println("All values must be greater than 0. Please try again.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter numeric values.");
                            scanner.next(); // clear the invalid input
                        }
                    }

                    ticketService.configure(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                    break;
                case "start":
                    ticketService.startSystem(executorService);
                    break;
                case "st":
                    ticketService.stopSystem();
                    running = false;
                    executorService.shutdown();
                    break;
                case "status":
                    ticketService.printStatus();
                    break;
                case "save":
                    System.out.println("Enter file name to save configuration:");
                    String saveFileName = scanner.next();
                    saveConfiguration(saveFileName, totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                    break;
                case "exit":
                    running = false;
                    executorService.shutdown();
                    break;
                default:
                    System.out.println("Invalid command.");
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
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }
}
