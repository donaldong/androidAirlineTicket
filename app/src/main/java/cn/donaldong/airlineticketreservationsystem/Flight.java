package cn.donaldong.airlineticketreservationsystem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Flight {
    static HashMap<Integer, Flight> mem = new HashMap<>();
    static CustomerDashboardFragment dashboard = null;
    private int id;
    private String name;
    private City departure, arrival;
    private String time_departure;
    private int capacity;
    private double price;
    private LinearLayout row;
    private TableRow above;
    private HashMap<Integer, Reservation> reservations = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getDeparture() {
        return departure;
    }

    public void setDeparture(City departure) {
        this.departure = departure;
    }

    public City getArrival() {
        return arrival;
    }

    public void setArrival(City arrival) {
        this.arrival = arrival;
    }

    public String getTime_departure() {
        return time_departure;
    }

    public void setTime_departure(String time_departure) {
        this.time_departure = time_departure;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    void addReservation(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        reservation.setFlight(this);
    }

    public View createView(final Context context, final ViewGroup container) {
        View view = LayoutInflater.from(context).inflate(R.layout.tablerow_flight, container, false);
        row = (LinearLayout) view;
        above = view.findViewById(R.id.tablerow_above);
        final Flight selected = this;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboard.clearFocus();
                View v = LayoutInflater.from(context).inflate(R.layout.edittext_ticket, above, false);
                above.addView(v);
                if (selected.getSeats() == 0) {
                    row.setBackgroundColor(dashboard.getResources().getColor(R.color.colorFull));
                } else {
                    row.setBackgroundColor(dashboard.getResources().getColor(R.color.white));
                }
                dashboard.selected = selected;
                TextView tv_from = v.findViewById(R.id.tv_from);
                tv_from.setText(departure.getNameEN());
                TextView tv_to = v.findViewById(R.id.tv_to);
                tv_to.setText(arrival.getNameEN());
            }
        });
        TextView tv_flight_name = view.findViewById(R.id.tv_flight_name);
        TextView tv_flight_capacity = view.findViewById(R.id.tv_flight_capacity);
        TextView tv_flight_price = view.findViewById(R.id.tv_flight_price);
        TextView tv_flight_time = view.findViewById(R.id.tv_flight_time);
        tv_flight_name.setText(name);
        tv_flight_capacity.setText(String.valueOf(capacity));
        tv_flight_price.setText(String.format(Locale.getDefault(),"$%.2f", price));
        tv_flight_time.setText(time_departure);
        return view;
    }

    int getSeats() {
        int res = capacity;
        for (Map.Entry entry : reservations.entrySet()) {
            Reservation reservation = (Reservation) entry.getValue();
            res -= reservation.getTickets();
        }
        return res;
    }

    Reservation createReservation() {
        EditText tickets = row.findViewById(R.id.et_ticket);
        Reservation reservation = new Reservation();
        reservation.setUser(dashboard.user);
        reservation.setFlight(this);
        try {
            reservation.setTickets(Integer.parseInt(tickets.getText().toString()));
        } catch (Exception e) {
            reservation.setTickets(0);
        }
        reservation.setTime(Database.datetime());
        return reservation;
    }

    void clearFocus() {
        above.removeAllViews();
        row.setBackgroundColor(Color.TRANSPARENT);
    }

    public void removeReservation(Reservation selected) {
        reservations.remove(selected.getId());
    }
}