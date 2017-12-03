package cn.donaldong.airlineticketreservationsystem;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new Database(this);
        setContentView(R.layout.activity_main);
        activate(new LoginFragment());
    }

    void transact(Fragment from, Fragment to) {
        deactivate(from);
        activate(to);
    }

    int activate(Fragment fragment) {
        return getFragmentManager().beginTransaction().add(R.id.root, fragment).commit();
    }

    int deactivate(Fragment fragment) {
        return getFragmentManager().beginTransaction().remove(fragment).commit();
    }
}
