package cn.donaldong.airlineticketreservationsystem;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Locale;

public class AddFlightFragment extends Fragment implements View.OnClickListener {
    private MainActivity main;
    private User user = null;
    private Component flight_number;
    private TextView tv_username, error_msg_departure_time;
    private Button btn_back, btn_add_flight;
    private Spinner departure, arrival;
    private EditText et_hour, et_minute, et_year, et_month, et_day, et_price, et_capacity;
    private boolean valid_datetime;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        View fragment = inflater.inflate(R.layout.fragment_add_flight, container, false);
        valid_datetime = false;
        tv_username = fragment.findViewById(R.id.tv_admin_username);
        departure = fragment.findViewById(R.id.spinner_departure);
        arrival = fragment.findViewById(R.id.spinner_arrival);
        et_hour = fragment.findViewById(R.id.et_hour);
        et_minute = fragment.findViewById(R.id.et_minute);
        et_year = fragment.findViewById(R.id.et_year);
        et_month = fragment.findViewById(R.id.et_month);
        et_day = fragment.findViewById(R.id.et_day);
        et_capacity = fragment.findViewById(R.id.et_capacity);
        et_price = fragment.findViewById(R.id.et_price);
        btn_back = fragment.findViewById(R.id.btn_back);
        btn_add_flight = fragment.findViewById(R.id.btn_add_flight);
        error_msg_departure_time = fragment.findViewById(R.id.error_msg_departure_time);
        flight_number = new Component(
                (EditText) fragment.findViewById(R.id.et_flight_no),
                (TextView) fragment.findViewById(R.id.error_msg_flight_no),
                getResources().getString(R.string.input_error_duplicated_flight_number)
        );

        tv_username.setText(user.getUsername());
        ArrayAdapter<String> adapter = City.createAdapter(main);
        departure.setAdapter(adapter);
        arrival.setAdapter(adapter);
        flight_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                flight_number.getText();
                if (main.db.getFlight(flight_number.text) != null) {
                    flight_number.displayError();
                    return;
                }
                flight_number.confirm();
            }
        });
        View.OnFocusChangeListener date_time_lister = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String hour = et_hour.getText().toString();
                String minute = et_minute.getText().toString();
                String year = et_year.getText().toString();
                String month = et_month.getText().toString();
                String day = et_day.getText().toString();
                if (hour.isEmpty() || minute.isEmpty()) return;
                if (year.isEmpty() || month.isEmpty() || day.isEmpty()) return;
                if (!valid(year, month, day, hour, minute)) {
                    valid_datetime = false;
                    error_msg_departure_time.setText(
                            getResources().getString(R.string.input_error_invalid_departure_date));
                    return;
                }
                valid_datetime = true;
                error_msg_departure_time.setText("");
            }
        };
        et_hour.setOnFocusChangeListener(date_time_lister);
        et_minute.setOnFocusChangeListener(date_time_lister);
        et_year.setOnFocusChangeListener(date_time_lister);
        et_month.setOnFocusChangeListener(date_time_lister);
        et_day.setOnFocusChangeListener(date_time_lister);
        final Fragment frag = this;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.transact(frag, new AdminDashboardFragment().setUser(user));
            }
        });
        btn_add_flight.setOnClickListener(this);
        return fragment;
    }

    AddFlightFragment setUser(User user) {
        this.user = user;
        return this;
    }

    boolean valid(String year, String month, String day, String hour, String minute) {
        try {
            if (Integer.valueOf(year) < 2000) return false;
            if (Integer.valueOf(month) > 12 || Integer.valueOf(month) <= 0) return false;
            if (Integer.valueOf(day) > 31 || Integer.valueOf(day) <= 0) return false;
            if (Integer.valueOf(hour) >= 24 || Integer.valueOf(hour) < 0) return false;
            return Integer.valueOf(minute) < 60 && Integer.valueOf(minute) >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btn_add_flight) {
            if (!flight_number.valid) flight_number.displayError();
            flight_number.clearFocus();
            et_capacity.clearFocus();
            et_price.clearFocus();
            et_minute.clearFocus();
            et_hour.clearFocus();
            et_day.clearFocus();
            et_month.clearFocus();
            et_year.clearFocus();
            if (!valid_datetime) error_msg_departure_time.setText(getString(R.string.input_error_invalid_departure_date));
            if (!(flight_number.valid && valid_datetime)) return;
            if (et_capacity.getText().toString().isEmpty()) {
                Toast.makeText(main, getString(R.string.input_error_no_capacity), Toast.LENGTH_SHORT).show();
                return;
            }
            if (et_price.getText().toString().isEmpty()) {
                Toast.makeText(main, getResources().getString(
                        R.string.input_error_no_price), Toast.LENGTH_SHORT).show();
                return;
            }
            if (departure.getSelectedItem().toString().equals(getString(R.string.select)) ||
                    arrival.getSelectedItem().toString().equals(getString(R.string.select))) {
                Toast.makeText(main, getResources().getString(
                        R.string.input_error_no_city), Toast.LENGTH_SHORT).show();
                return;
            }
            if (departure.getSelectedItem().toString().equals(
                    arrival.getSelectedItem().toString())) {
                Toast.makeText(main, getResources().getString(
                        R.string.input_error_same_city), Toast.LENGTH_SHORT).show();
                return;
            }
            final Flight flight = new Flight();
            flight.setTime_departure(String.format(Locale.getDefault(),
                    "%04d-%02d-%02d %02d:%02d:00",
                    Integer.valueOf(et_year.getText().toString()),
                    Integer.valueOf(et_month.getText().toString()),
                    Integer.valueOf(et_day.getText().toString()),
                    Integer.valueOf(et_hour.getText().toString()),
                    Integer.valueOf(et_minute.getText().toString())
                    ));
            flight.setPrice(Double.parseDouble(et_price.getText().toString()));
            flight.setCapacity(Integer.parseInt(et_capacity.getText().toString()));
            flight.setName(flight_number.text);
            flight.setDeparture(main.db.getCity(departure.getSelectedItem().toString()));
            flight.setArrival(main.db.getCity(arrival.getSelectedItem().toString()));
            AlertDialog.Builder alert = new AlertDialog.Builder(main);
            alert.setTitle(getResources().getString(R.string.confirm_new_flight));
            alert.setMessage(String.format(
                            "- Flight number: %s\n" +
                            "- Departure: %s (%s)\n" +
                            "- Arrival: %s\n" +
                            "- Capacity: %d\n" +
                            "- Price: %.2f",
                    flight.getName(), flight.getDeparture().getNameEN(),
                    flight.getTime_departure(), flight.getArrival().getNameEN(),
                    flight.getCapacity(), flight.getPrice()));
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    flight.setId((int) main.db.insert(flight));
                    Flight.mem.put(flight.getId(), flight);
                    Toast.makeText(main, getString(R.string.success), Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {}
            });
            alert.setIcon(getResources().getDrawable(R.drawable.ic_question_mark));
            alert.show();
        }
    }
}
