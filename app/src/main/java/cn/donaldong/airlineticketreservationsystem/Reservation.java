package cn.donaldong.airlineticketreservationsystem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Reservation {
    static CancelReservationFragment table = null;
    private int id;
    private User user;
    private Flight flight;
    private int tickets;
    private String time;
    private LinearLayout row;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserID() {
        return user.getId();
    }

    public int getFlightID() {
        return flight.getId();
    }

    public View createView(final Context context, final ViewGroup container) {
        View view = LayoutInflater.from(context).inflate(R.layout.tablerow_reservation, container, false);
        row = (LinearLayout) view;
        final Reservation selected = this;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.clearFocus();
                row.setBackgroundColor(table.getResources().getColor(R.color.white));
                table.selected = selected;
            }
        });
        TextView tv_reservation_id = view.findViewById(R.id.tv_reservation_id);
        TextView tv_flight_name = view.findViewById(R.id.tv_flight_name);
        TextView tv_flight_time = view.findViewById(R.id.tv_flight_time);
        TextView tv_ticket = view.findViewById(R.id.tv_ticket);
        tv_flight_name.setText(flight.getName());
        tv_reservation_id.setText(String.valueOf(id));
        tv_ticket.setText(String.valueOf(tickets));
        tv_flight_time.setText(flight.getTime_departure());
        return view;
    }

    void clearFocus() {
        row.setBackgroundColor(Color.TRANSPARENT);
    }
}