package cn.donaldong.airlineticketreservationsystem;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class City {
    private int id;
    private String nameEN;

    City(int id, String nameEN) {
        this.id = id;
        this.nameEN = nameEN;
    }

    public int getId() {
        return id;
    }

    public String getNameEN() {
        return nameEN;
    }

    static ArrayAdapter<String> createAdapter(MainActivity main) {
        ArrayList<City> cities = main.db.getCities();
        ArrayList<String> options = new ArrayList<>();
        String select = main.getResources().getString(R.string.select);
        options.add(select);
        for (City city : cities) options.add(city.getNameEN());
        return new ArrayAdapter<>(main, android.R.layout.simple_spinner_item, options);
    }
}
