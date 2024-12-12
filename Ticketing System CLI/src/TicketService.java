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
        if (totalTickets <= 0 || ticketReleaseRate <= 0 || customerRetrievalRate <= 0 || maxTicketCapacity <= 0) {
            throw new IllegalArgumentException("All configuration parameters must be positive numbers.");
        }
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
        if (ticketPool == null || vendors == null || customers == null) {
            System.out.println("Please configure the system first.");
            return;
        }

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
    }

    public void stopSystem() {
        if (vendors == null || customers == null) {
            System.out.println("System is not started or has already been stopped.");
            return;
        }

        for (Vendor vendor : vendors) {
            if (vendor != null) {
                vendor.stop();
            }
        }
        for (Customer customer : customers) {
            if (customer != null) {
                customer.stop();
            }
        }
        logToFile("System stopped.");
        System.out.println("System stopped.");
    }

    public void printStatus() {
        if (ticketPool == null) {
            System.out.println("System is not configured. Please configure the system first.");
            return;
        }

        String status = "Current tickets in pool: " + ticketPool.getAvailableTickets();
        logToFile(status);
        System.out.println(status);
    }

    public void saveConfiguration(String fileName) {
        if (ticketPool == null) {
            System.out.println("System is not configured. Cannot save configuration.");
            return;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("totalTickets=" + ticketPool.getAvailableTickets() + "\n");
            writer.write("vendors=" + (vendors != null ? vendors.length : 0) + "\n");
            writer.write("customers=" + (customers != null ? customers.length : 0) + "\n");
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
