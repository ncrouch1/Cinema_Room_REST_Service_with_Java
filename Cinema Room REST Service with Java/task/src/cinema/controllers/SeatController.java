package cinema.controllers;

import cinema.models.Seat;
import cinema.models.Seats;
import cinema.models.Ticket;
import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class SeatController {

    private Seats seatlist;
    private int income;
    private Map<UUID, Seat> purchases;
    public SeatController() {
        seatlist = new Seats(9, 9);
        purchases = new HashMap<>();
        income = 0;
    }

    @GetMapping("/seats")
    public String getSeats() {
        return seatlist.toJson();
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseSeat(@RequestBody String body) {
        JSONObject jo = new JSONObject(body);
        int row = jo.getInt("row");
        int column = jo.getInt("column");
        Seat checkingSeat = new Seat(row, column);
        if (row > seatlist.getRows() || column > seatlist.getColumns()
        || row < 1 || column < 1) {
            String responsebody = new JSONObject()
                    .put("error", "The number of a row or a column is out of bounds!")
                    .toString();
            return new ResponseEntity<String>(
                    responsebody,
                    HttpStatusCode.valueOf(400)
            );
        }
        if (!seatlist.contains(checkingSeat)) {
            String responsebody = new JSONObject()
                    .put("error", "The ticket has been already purchased!")
                    .toString();
            return new ResponseEntity<String>(
                    responsebody,
                    HttpStatusCode.valueOf(400)
            );
        }
        seatlist.removeSeat(row, column);
        income += checkingSeat.getPrice();
        Ticket ticket = new Ticket(checkingSeat);
        purchases.put(ticket.getToken(), ticket.getTicket());
        String responsebody = new JSONObject(ticket).toString();
        return new ResponseEntity<String>(
                responsebody, HttpStatusCode.valueOf(200)
        );
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnSeat(@RequestBody String body) {
        JSONObject jo = new JSONObject(body);
        if (!jo.has("token")) {
            String reponsebody = new JSONObject()
                    .put("error", "Wrong token!")
                    .toString();
            return new ResponseEntity<String>(
                    reponsebody, HttpStatusCode.valueOf(400)
            );
        }
        UUID token = UUID.fromString(jo.getString("token"));
        if (!purchases.containsKey(token)) {
            String reponsebody = new JSONObject()
                    .put("error", "Wrong token!")
                    .toString();
            return new ResponseEntity<String>(
                    reponsebody, HttpStatusCode.valueOf(400)
            );
        }
        Seat returningSeat = purchases.get(token);
        income -= returningSeat.getPrice();
        purchases.remove(token);
        seatlist.returnSeat(returningSeat);
        JSONObject seatjson = new JSONObject(returningSeat);
        String responsebody = new JSONObject()
                .put("ticket", seatjson)
                .toString();
        return new ResponseEntity<String>(
                responsebody, HttpStatusCode.valueOf(200)
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStats(@RequestParam(required = false) String password) {
        if (password == null || !password.equals("super_secret")) {
            String responsebody = new JSONObject()
                    .put("error", "The password is wrong!")
                    .toString();
            return new ResponseEntity<String>(
                    responsebody,
                    HttpStatusCode.valueOf(401)
            );
        }
        String responsebody = new JSONObject()
                .put("income", income)
                .put("available", seatlist.available())
                .put("purchased", purchases.size())
                .toString();
        return new ResponseEntity<String>(
                responsebody, HttpStatusCode.valueOf(200)
        );
    }
}
