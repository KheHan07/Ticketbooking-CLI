import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;
import java.io.FileWriter;
import java.io.IOException;

public class TicketService {
    private TicketPool ticketPool;
    private Vendor[] vendors;
    private Customer[] customers;
    private ReentrantLock lock = new ReentrantLock();
    private static final String LOG_FILE = "ticket_system.log";

    public void configure(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.ticketPool = new TicketPool(maxTicketCapacity);
        int numberOfVendors = 2; // Simulating multiple vendors
        int numberOfCustomers = 3; // Simulating multiple customers
        this.vendors = new Vendor[numberOfVendors];
        this.customers = new Customer[numberOfCustomers];

        for (int i = 0; i < numberOfVendors; i++) {
            this.vendors[i] = new Vendor(ticketPool, ticketReleaseRate, totalTickets / numberOfVendors);
        }
        for (int i = 0; i < numberOfCustomers; i++) {
            this.customers[i] = new Customer(ticketPool, customerRetrievalRate);
        }
        logToFile("Configuration completed.");
        System.out.println("Configuration completed.");
    }

    public void startSystem(ExecutorService executorService) {
        if (vendors != null && customers != null) {
            for (Vendor vendor : vendors) {
                executorService.execute(vendor);
            }
            for (Customer customer : customers) {
                executorService.execute(customer);
            }
            logToFile("System started successfully.");
            System.out.println("System started successfully.");

            // Start a new thread to monitor the ticket pool status in real-time
            new Thread(() -> {
                while (!executorService.isShutdown()) {
                    try {
                        Thread.sleep(2000); // Print status every 2 seconds
                        printStatus();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        } else {
            System.out.println("Please configure the system first.");
        }
    }

    public void stopSystem() {
        for (Vendor vendor : vendors) {
            vendor.stop();
        }
        for (Customer customer : customers) {
            customer.stop();
        }
        logToFile("System stopped.");
        System.out.println("System stopped.");
    }

    public void printStatus() {
        String status = "Current tickets in pool: " + ticketPool.getAvailableTickets();
        logToFile(status);
        System.out.println(status);
    }

    public void saveConfiguration(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("totalTickets=" + ticketPool.getAvailableTickets() + "\n");
            writer.write("vendors=" + vendors.length + "\n");
            writer.write("customers=" + customers.length + "\n");
            System.out.println("Configuration saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }

    private void logToFile(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.out.println("Failed to write to log file: " + e.getMessage());
        }
    }
}
