package cn.donaldong.airlineticketreservationsystem;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.TreeMap;

public class CustomerDashboardFragment extends Fragment implements View.OnClickListener {
    protected Flight selected;
    protected User user = null;
    private MainActivity main;
    private TextView tv_username;
    private Spinner departure, arrival;
    private TableLayout tableLayout;
    private Button btn_reserve, btn_cancel, btn_logout;
    private ArrayList<Flight> flights;
    private ArrayList<View> flightViews;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        Flight.dashboard = this;
        selected = null;
        View fragment = inflater.inflate(R.layout.fragment_dashboard_customer, container, false);
        tv_username = fragment.findViewById(R.id.tv_customer_username);
        departure = fragment.findViewById(R.id.spinner_departure);
        arrival = fragment.findViewById(R.id.spinner_arrival);
        tableLayout = fragment.findViewById(R.id.table_customer_flights);
        btn_reserve = fragment.findViewById(R.id.btn_reserve);
        btn_cancel = fragment.findViewById(R.id.btn_cancel);
        btn_logout = fragment.findViewById(R.id.btn_logout);

        tv_username.setText(user.getUsername());
        ArrayAdapter<String> adapter = City.createAdapter(main);
        departure.setAdapter(adapter);
        arrival.setAdapter(adapter);
        flights = main.db.getFlights();
        flightViews = new ArrayList<>();
        for (Flight flight : flights) flightViews.add(flight.createView(main, container));
        for (View view : flightViews) tableLayout.addView(view);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String dep = departure.getSelectedItem().toString(), arr = arrival.getSelectedItem().toString();
                if (dep.equals(getString(R.string.select))) return;
                if (arr.equals(getString(R.string.select))) return;
                for (View v : flightViews) tableLayout.removeView(v);
                flightViews.clear();
                flights = main.db.getFlights(dep, arr);
                for (Flight flight : flights) flightViews.add(flight.createView(main, container));
                for (View v : flightViews) tableLayout.addView(v);
                selected = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        };
        departure.setOnItemSelectedListener(itemSelectedListener);
        arrival.setOnItemSelectedListener(itemSelectedListener);
        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == null) {
                    Toast.makeText(main, main.getResources().getString(
                            R.string.input_error_no_selection_flight),Toast.LENGTH_SHORT).show();
                    return;
                }
                final Reservation reservation = selected.createReservation();
                if (reservation.getTickets() == 0) {
                    Toast.makeText(main, main.getResources().getString(
                            R.string.input_error_no_ticket),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (reservation.getTickets() > selected.getSeats()) {
                    alertError(main.getResources().getString(R.string.input_error_no_seat));
                    return;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(main);
                alert.setTitle(getResources().getString(R.string.confirm_reservation));
                alert.setMessage(String.format(
                                "- Username: %s\n" +
                                "- Flight number: %s\n" +
                                "- Departure: %s (%s)\n" +
                                "- Arrival: %s\n" +
                                "- Number of tickets: %d\n" +
                                " - Reservation number: %d\n" +
                                "- Total amount: %.2f\n",
                        user.getUsername(), selected.getName(), selected.getDeparture().getNameEN(),
                        selected.getTime_departure(), selected.getArrival().getNameEN(),
                        reservation.getTickets(), main.db.nextReservationID(),
                        selected.getPrice() * reservation.getTickets()));
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reservation.setId((int) main.db.insert(reservation));
                        selected.addReservation(reservation);
                        Transaction transaction = new Transaction(
                                user, Transaction.Type.RESERVE_SEAT, Transaction.jsonify(reservation));
                        transaction.setId((int) main.db.insert(transaction));
                        Toast.makeText(main, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alert.setIcon(getResources().getDrawable(R.drawable.ic_question_mark));
                alert.show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Reservation> reservations = main.db.getReservations(user);
                if (reservations.size() == 0) {
                    alertError(getResources().getString(R.string.input_error_no_reservation));
                    return;
                }
                TreeMap<Integer, Reservation> map = new TreeMap<>();
                for (Reservation reservation : reservations) map.put(reservation.getId(), reservation);
                main.transact(Flight.dashboard,
                        new CancelReservationFragment().setReservations(map).setUser(user));
            }
        });
        btn_logout.setOnClickListener(this);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (view == btn_logout) {
            main.transact(this, new LoginFragment());
            return;
        }
    }

    CustomerDashboardFragment setUser(User user) {
        this.user = user;
        return this;
    }

    void clearFocus() {
        for (Flight flight : flights) flight.clearFocus();
    }

    void alertError(String error_msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(main);
        alert.setTitle(getResources().getString(R.string.error));
        alert.setMessage(error_msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
        });
        alert.setIconAttribute(android.R.attr.alertDialogIcon);
        alert.show();
    }
}
