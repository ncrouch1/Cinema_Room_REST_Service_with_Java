package cinema.models;

import java.util.UUID;

public class Ticket {
    private UUID token;
    private Seat ticket;

    public Ticket(Seat ticket) {
        token = UUID.randomUUID();
        this.ticket = ticket;
    }

    public UUID getToken() {
        return token;
    }

    public Seat getTicket() {
        return ticket;
    }
}
