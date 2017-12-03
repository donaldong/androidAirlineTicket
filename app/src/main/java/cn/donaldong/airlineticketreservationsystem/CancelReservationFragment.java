package cn.donaldong.airlineticketreservationsystem;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.TreeMap;

public class CancelReservationFragment extends Fragment {
    protected User user = null;
    protected Reservation selected = null;
    private MainActivity main;
    private TreeMap<Integer, Reservation> reservations;
    private TextView tv_username;
    private TableLayout tableLayout;
    private Button btn_cancel, btn_back;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        Reservation.table = this;
        View fragment = inflater.inflate(R.layout.fragment_cancel_reservation, container, false);
        tv_username = fragment.findViewById(R.id.tv_customer_username);
        tableLayout = fragment.findViewById(R.id.table_customer_reservations);
        btn_cancel = fragment.findViewById(R.id.btn_cancel);
        btn_back = fragment.findViewById(R.id.btn_back);

        final Fragment frag = this;
        tv_username.setText(user.getUsername());
        buildRows(container);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.transact(frag, new CustomerDashboardFragment().setUser(user));
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == null) {
                    Toast.makeText(main, main.getResources().getString(
                            R.string.input_error_no_selection_reservation),Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(main);
                alert.setTitle(getResources().getString(R.string.confirm_cancellation));
                final Flight flight = selected.getFlight();
                alert.setMessage(String.format(
                                "- Username: %s\n" +
                                "- Flight number: %s\n" +
                                "- Departure: %s (%s)\n" +
                                "- Arrival: %s\n" +
                                "- Number of tickets: %d\n" +
                                " - Reservation number: %d\n" +
                                "- Total amount: %.2f\n",
                        user.getUsername(), flight.getName(), flight.getDeparture().getNameEN(),
                        flight.getTime_departure(), flight.getArrival().getNameEN(),
                        selected.getTickets(), selected.getId(),
                        flight.getPrice() * selected.getTickets()));
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flight.removeReservation(selected);
                        main.db.delete(selected);
                        Transaction transaction = new Transaction(
                                user, Transaction.Type.CANCEL_RESERVATION, Transaction.jsonify(selected));
                        transaction.setId((int) main.db.insert(transaction));
                        Toast.makeText(main, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                        tableLayout.removeViews(1, reservations.size());
                        reservations.remove(selected.getId());
                        buildRows(container);
                        selected = null;
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alert.setIcon(getResources().getDrawable(R.drawable.ic_question_mark));
                alert.show();
            }
        });
        return fragment;
    }

    CancelReservationFragment setUser(User user) {
        this.user = user;
        return this;
    }

    CancelReservationFragment setReservations(TreeMap<Integer, Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    void clearFocus() {
        for (Map.Entry entry : reservations.entrySet()) {
            Reservation reservation = (Reservation) entry.getValue();
            reservation.clearFocus();
        }
    }

    void buildRows(ViewGroup container) {
        for (Map.Entry entry : reservations.entrySet()) {
            Reservation reservation = (Reservation) entry.getValue();
            tableLayout.addView(reservation.createView(main, container));
        }
    }
}
