import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class TicketPool {
    private final List<Integer> tickets;
    private final int capacity;
    private static final String LOG_FILE = "ticket_system.log";

    public TicketPool(int capacity) {
        this.capacity = capacity;
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void addTickets(int count) {
        while (tickets.size() + count > capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        for (int i = 0; i < count; i++) {
            tickets.add(i);
            logToFile("Ticket added: " + i); // Log ticket addition
            System.out.println("Ticket added: " + i);
        }
        notifyAll();
    }

    public synchronized Integer removeTicket() {
        while (tickets.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Integer ticket = tickets.remove(0);
        if (ticket != null) {
            logToFile("Ticket removed: " + ticket); // Log ticket removal
            System.out.println("Ticket removed: " + ticket);
        }
        notifyAll();
        return ticket;
    }

    public int getAvailableTickets() {
        return tickets.size();
    }

    private void logToFile(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.out.println("Failed to write to log file: " + e.getMessage());
        }
    }
}
