package cinema.models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Seats {
    private int rows, columns;
    private List<Seat> seats;

    public Seats(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        seats = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                Seat newseat = new Seat(i, j);
                seats.add(newseat);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int available() {
        return seats.size();
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public boolean contains(Seat seat) {
        return seats.contains(seat);
    }
    public void removeSeat(int row, int column) {
        Seat removedSeat = new Seat(row, column);
        seats.remove(removedSeat);
    }

    public void returnSeat(Seat seat) {
        seats.add(seat);
    }

    public String toJson() {
        return new JSONObject(this).toString();
    }
}
