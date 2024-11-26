public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int retrievalRate;
    private volatile boolean running = true;

    public Customer(TicketPool ticketPool, int retrievalRate) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        while (running) {
            for (int i = 0; i < retrievalRate; i++) {
                ticketPool.removeTicket();
            }
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
