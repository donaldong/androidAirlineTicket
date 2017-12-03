package cn.donaldong.airlineticketreservationsystem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccountFragment extends Fragment implements View.OnClickListener {
    private MainActivity main;
    private Component username, pwd, pwd_confirm;
    private Button btn_register, btn_back_to_login;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        View fragment = inflater.inflate(R.layout.fragment_create_account, container, false);
        username = new Component(
                (EditText) fragment.findViewById(R.id.et_create_account_username),
                (TextView) fragment.findViewById(R.id.error_msg_username),
                getResources().getString(R.string.format_error_username)
        );
        pwd = new Component(
                (EditText) fragment.findViewById(R.id.et_create_account_pwd),
                (TextView) fragment.findViewById(R.id.error_msg_password),
                getResources().getString(R.string.format_error_password)
        );
        pwd_confirm = new Component(
                (EditText) fragment.findViewById(R.id.et_create_account_pwd_confirm),
                (TextView) fragment.findViewById(R.id.error_msg_password_confirm),
                getResources().getString(R.string.input_error_password_confirm)
        );
        btn_register = fragment.findViewById(R.id.btn_register);
        btn_back_to_login = fragment.findViewById(R.id.btn_back_to_login);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) return;
                username.getText();
                if (!valid(username.text)) {
                    username.displayError();
                    return;
                }
                if (main.db.getUser(username.text) != null) {
                    username.displayError(getResources().getString(R.string.input_error_duplicated_username));
                    return;
                }
                username.confirm();
            }
        });
        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) return;
                pwd.getText();
                pwd_confirm.reset();
                if (!valid(pwd.text)) {
                    pwd.displayError();
                    return;
                }
                pwd.confirm();
            }
        });
        pwd_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) return;
                pwd_confirm.getText();
                if (!pwd.text.equals(pwd_confirm.text)) {
                    pwd_confirm.displayError();
                    return;
                }
                pwd_confirm.confirm();
            }
        });
        btn_register.setOnClickListener(this);
        btn_back_to_login.setOnClickListener(this);
        return fragment;
    }

    boolean valid(String input) {
        int count_letter = 0;
        int count_number = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'z') count_letter++;
            if (c >= 'A' && c <= 'Z') count_letter++;
            if (c >= '0' && c <= '9') count_number++;
        }
        return count_letter >= 3 && count_number >= 1;
    }

    @Override
    public void onClick(View view) {
        if (view == btn_back_to_login) {
            main.transact(this, new LoginFragment());
            return;
        }
        if (view == btn_register) {
            if (!username.valid) username.displayError();
            if (!pwd.valid) pwd.displayError();
            if (!pwd_confirm.valid) pwd_confirm.displayError();
            username.clearFocus();
            pwd.clearFocus();
            pwd_confirm.clearFocus();
            if (!(username.valid && pwd.valid && pwd_confirm.valid)) return;
            User user = new User(User.Type.CUSTOMER.getValue(), username.text, pwd.text);
            Transaction transaction = new Transaction(user, Transaction.Type.NEW_ACCOUNT, null);
            user.setId((int) main.db.insert(user));
            transaction.setId((int) main.db.insert(transaction));
            main.transact(this, new CustomerDashboardFragment().setUser(user));
            Toast.makeText(main, String.format("%s! %s",
                    getResources().getString(R.string.welcome), username.text), Toast.LENGTH_SHORT).show();
        }
    }
}