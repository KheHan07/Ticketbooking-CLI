public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate;
    private int totalTickets;
    private volatile boolean running = true;

    public Vendor(TicketPool ticketPool, int releaseRate, int totalTickets) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        while (running && totalTickets > 0) {
            ticketPool.addTickets(releaseRate);
            totalTickets -= releaseRate;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
