package cn.donaldong.airlineticketreservationsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONObject;

class Transaction {
    private int id;
    private String time;
    private User user;
    private Type type;
    private String json;

    public enum Type {
        NEW_ACCOUNT(0), RESERVE_SEAT(1), CANCEL_RESERVATION(2);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Type valueOf(int i) {
            if (i == 0) return NEW_ACCOUNT;
            if (i == 1) return RESERVE_SEAT;
            return CANCEL_RESERVATION;
        }

        public String toString(Context context) {
            if (value == 0) return context.getResources().getString(R.string.new_account);
            if (value == 1) return context.getResources().getString(R.string.reserve_seat);
            return context.getResources().getString(R.string.cancel_reservation);
        }
    }

    public static String jsonify(Reservation reservation) {
        return String.format("{" +
                        "\"Flight Number\":\"%s\"," +
                        "\"Departure City\":\"%s\"," +
                        "\"Departure Time\":\"%s\"," +
                        "\"Arrival City\":\"%s\"," +
                        "\"Number of Tickets\":%d," +
                        "\"Reservation Number\":%d," +
                        "\"Total Amount\":%.2f" +
                        "}",
                reservation.getFlight().getName(),
                reservation.getFlight().getDeparture().getNameEN(),
                reservation.getFlight().getTime_departure(),
                reservation.getFlight().getArrival().getNameEN(),
                reservation.getTickets(),
                reservation.getId(),
                reservation.getFlight().getPrice() * reservation.getTickets());
    }

    public View createView(Context context, final ViewGroup container) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_log, container, false);
        StringBuilder str = new StringBuilder();
        str.append(String.format("[%s] user: %s\n  %s\n", time, user.getUsername(), type.toString(context)));
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (type == Type.RESERVE_SEAT || type == Type.CANCEL_RESERVATION) {
                str.append(String.format(
                                "   Flight Number:      %s\n" +
                                "   Departure City:     %s\n" +
                                "   Departure Time:     %s\n" +
                                "   Arrival City:       %s\n" +
                                "   Number of Tickets:  %d\n" +
                                "   Reservation Number: %d\n" +
                                "   Total Amount:       %.2f\n",
                        jsonObject.getString("Flight Number"),
                        jsonObject.getString("Departure City"),
                        jsonObject.getString("Departure Time"),
                        jsonObject.getString("Arrival City"),
                        jsonObject.getInt("Number of Tickets"),
                        jsonObject.getInt("Reservation Number"),
                        jsonObject.getDouble("Total Amount")
                ));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        TextView textView = view.findViewById(R.id.tv_log);
        textView.setText(str);
        return view;
    }

    Transaction(User user, Type type, String json) {
        this.time = Database.datetime();
        this.user = user;
        this.type = type;
        this.json = json;
    }

    Transaction(String time, User user, int type, String json) {
        this.time = time;
        this.user = user;
        this.type = Type.valueOf(type);
        this.json = json;
    }

    public Type getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public int getUserID() {
        return user.getId();
    }

    public String getJson() {
        return json;
    }

    public void setId(int id) {
        this.id = id;
    }
}
