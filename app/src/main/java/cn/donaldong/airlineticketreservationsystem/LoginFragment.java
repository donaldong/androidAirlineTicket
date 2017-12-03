package cn.donaldong.airlineticketreservationsystem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private MainActivity main;
    private Button btn_login, btn_sign_up;
    private Component username, pwd;
    private TextView error_msg_login_failure;
    private ImageButton btn_help;

    private class Component {
        private EditText editText;

        Component(EditText editText) {
            this.editText = editText;
            reset();
        }

        void reset() {
            editText.setBackgroundResource(R.drawable.edittext_normal);
            editText.setText("");
            editText.clearFocus();
        }

        void displayError() {
            editText.setBackgroundResource(R.drawable.edittext_error);
        }

        @Override
        public String toString() {
            return editText.getText().toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        View fragment = inflater.inflate(R.layout.fragment_login, container, false);
        username = new Component(
                (EditText) fragment.findViewById(R.id.et_login_username)
        );
        pwd = new Component(
                (EditText) fragment.findViewById(R.id.et_login_password)
        );
        error_msg_login_failure = fragment.findViewById(R.id.error_msg_login_failure);
        btn_login = fragment.findViewById(R.id.btn_login);
        btn_sign_up = fragment.findViewById(R.id.btn_sign_up);
        btn_help = fragment.findViewById(R.id.btn_help);
        btn_login.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main, getString(R.string.author),Toast.LENGTH_SHORT).show();
            }
        });
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (view == btn_login) {
            User user = main.db.getUser(username.toString(), pwd.toString());
            if (user == null) {
                error_msg_login_failure.setText(getResources().getString(R.string.input_error_login_failure));
                pwd.reset();
                pwd.displayError();
                return;
            }
            if (user.getType() == User.Type.CUSTOMER)
                main.transact(this, new CustomerDashboardFragment().setUser(user));
            else
                main.transact(this, new AdminDashboardFragment().setUser(user));
            Toast.makeText(main, String.format("%s %s",
                    getResources().getString(R.string.welcome_back), username.toString()), Toast.LENGTH_SHORT).show();
            return;
        }
        if (view == btn_sign_up) {
            main.transact(this, new CreateAccountFragment());
        }
    }
}
