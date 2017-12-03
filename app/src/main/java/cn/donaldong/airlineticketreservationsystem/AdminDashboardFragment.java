package cn.donaldong.airlineticketreservationsystem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class AdminDashboardFragment extends Fragment implements View.OnClickListener {
    private MainActivity main;
    private User user = null;
    private TextView tv_username;
    private LinearLayout linearLayout_transactions;
    private Button btn_add_flight, btn_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        View fragment = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);
        tv_username = fragment.findViewById(R.id.tv_admin_username);
        linearLayout_transactions = fragment.findViewById(R.id.linear_admin_transactions);
        btn_add_flight = fragment.findViewById(R.id.btn_add_flight);
        btn_logout = fragment.findViewById(R.id.btn_logout);

        tv_username.setText(user.getUsername());
        ArrayList<Transaction> transactions = main.db.getTransactions();
        for (Transaction transaction : transactions) {
            linearLayout_transactions.addView(transaction.createView(main, container));
        }
        btn_add_flight.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (view == btn_logout) {
            main.transact(this, new LoginFragment());
            return;
        }
        main.transact(this, new AddFlightFragment().setUser(user));
    }

    AdminDashboardFragment setUser(User user) {
        this.user = user;
        return this;
    }
}
