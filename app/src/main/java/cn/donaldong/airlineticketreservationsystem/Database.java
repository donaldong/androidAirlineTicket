package cn.donaldong.airlineticketreservationsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class Database {
    private static final String DB_NAME = "airline.db";
    private final int DB_VERSION = 1;

    private static final String USER_TABLE = "`user`";
    private static final String USER_ID = "id";
    private static final int USER_ID_COL = 0;
    private static final String USER_TYPE = "type";
    private static final int USER_TYPE_COL = 1;
    private static final String USER_USERNAME = "username";
    private static final int USER_USERNAME_COL = 2;
    private static final String USER_PASSWORD = "password";
    private static final int USER_PASSWORD_COL = 3;
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " (" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_TYPE + " INTEGER NOT NULL, " +
                    USER_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    USER_PASSWORD + " TEXT NOT NULL);";
    private static final String DROP_USER_TABLE =
            "DROP TABLE IF EXISTS " + USER_TABLE + ";";


    private static final String TRANS_TABLE = "`transaction`";
    private static final String TRANS_ID = "id";
    private static final int TRANS_ID_COL = 0;
    private static final String TRANS_USER_ID = "user_id";
    private static final int TRANS_USER_ID_COL = 1;
    private static final String TRANS_TIME = "time";
    private static final int TRANS_TIME_COL = 2;
    private static final String TRANS_TYPE = "type";
    private static final int TRANS_TYPE_COL = 3;
    private static final String TRANS_JSON = "json";
    private static final int TRANS_JSON_COL = 4;
    private static final String CREATE_TRANS_TABLE =
            "CREATE TABLE " + TRANS_TABLE + " (" +
                    TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TRANS_USER_ID + " INTEGER NOT NULL, " +
                    TRANS_TIME + " DATETIME NOT NULL, " +
                    TRANS_TYPE + " INTEGER NOT NULL, " +
                    TRANS_JSON + " TEXT," +
                    " FOREIGN KEY (" + TRANS_USER_ID +  ") " +
                    " REFERENCES " + USER_TABLE + "(id)" +
                    " ON DELETE CASCADE);";
    private static final String DROP_TRANS_TABLE =
            "DROP TABLE IF EXISTS " + TRANS_TABLE + ";";

    private static final String CITY_TABLE = "`city`";
    private static final String CITY_ID = "id";
    private static final int CITY_ID_COL = 0;
    private static final String CITY_NAME_EN = "name_EN";
    private static final int CITY_NAME_EN_COL = 1;
    private static final String CREATE_CITY_TABLE =
            "CREATE TABLE " + CITY_TABLE + " (" +
                    CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CITY_NAME_EN + " TEXT NOT NULL UNIQUE);";
    private static final String DROP_CITY_TABLE =
            "DROP TABLE IF EXISTS " + CITY_TABLE + ";";

    private static final String FLIGHT_TABLE = "`flight`";
    private static final String FLIGHT_ID = "id";
    private static final int FLIGHT_ID_COL = 0;
    private static final String FLIGHT_NAME = "name";
    private static final int FLIGHT_NAME_COL = 1;
    private static final String FLIGHT_CITY_DEPARTURE = "city_departure";
    private static final int FLIGHT_CITY_DEPARTURE_COL = 2;
    private static final String FLIGHT_CITY_ARRIVAL = "city_arrival";
    private static final int FLIGHT_CITY_ARRIVAL_COL = 3;
    private static final String FLIGHT_TIME_DEPARTURE = "time_departure";
    private static final int FLIGHT_TIME_DEPARTURE_COL = 4;
    private static final String FLIGHT_CAPACITY = "capacity";
    private static final int FLIGHT_CAPACITY_COL = 5;
    private static final String FLIGHT_PRICE = "price";
    private static final int FLIGHT_PRICE_COL = 6;
    private static final String CREATE_FLIGHT_TABLE =
            "CREATE TABLE " + FLIGHT_TABLE + " (" +
                    FLIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FLIGHT_NAME + " TEXT NOT NULL UNIQUE, " +
                    FLIGHT_CITY_DEPARTURE + " INTEGER NOT NULL, " +
                    FLIGHT_CITY_ARRIVAL + " INTEGER NOT NULL, " +
                    FLIGHT_TIME_DEPARTURE + " DATETIME NOT NULL, " +
                    FLIGHT_CAPACITY + " INTEGER NOT NULL, " +
                    FLIGHT_PRICE + " REAL NOT NULL, " +
                    " FOREIGN KEY (" + FLIGHT_CITY_DEPARTURE +  ") " +
                    " REFERENCES " + CITY_TABLE + "(id)" +
                    " ON DELETE CASCADE, " +
                    " FOREIGN KEY (" + FLIGHT_CITY_ARRIVAL +  ") " +
                    " REFERENCES " + CITY_TABLE + "(id)" +
                    " ON DELETE CASCADE);";
    private static final String DROP_FLIGHT_TABLE =
            "DROP TABLE IF EXISTS " + FLIGHT_TABLE + ";";

    private static final String RESERVATION_TABLE = "`reservation`";
    private static final String RESERVATION_ID = "id";
    private static final int RESERVATION_ID_COL = 0;
    private static final String RESERVATION_USER_ID = "user_id";
    private static final int RESERVATION_USER_ID_COL = 1;
    private static final String RESERVATION_FLIGHT_ID = "flight_id";
    private static final int RESERVATION_FLIGHT_ID_COL = 2;
    private static final String RESERVATION_TICKET = "ticket";
    private static final int RESERVATION_TICKET_COL = 3;
    private static final String RESERVATION_TIME = "time";
    private static final int RESERVATION_TIME_COL = 4;
    private static final String CREATE_RESERVATION_TABLE =
            "CREATE TABLE " + RESERVATION_TABLE + " (" +
                    RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RESERVATION_USER_ID + " INTEGER NOT NULL, " +
                    RESERVATION_FLIGHT_ID + " INTEGER NOT NULL, " +
                    RESERVATION_TICKET + " INTEGER NOT NULL, " +
                    RESERVATION_TIME + " DATETIME NOT NULL, " +
                    " FOREIGN KEY (" + RESERVATION_USER_ID +  ") " +
                    " REFERENCES " + USER_TABLE + "(id)" +
                    " ON DELETE CASCADE, " +
                    " FOREIGN KEY (" + RESERVATION_FLIGHT_ID +  ") " +
                    " REFERENCES " + FLIGHT_TABLE + "(id)" +
                    " ON DELETE CASCADE);";
    private static final String DROP_RESERVATION_TABLE =
            "DROP TABLE IF EXISTS " + RESERVATION_TABLE + ";";

    private static class DBHelper extends SQLiteOpenHelper {
        Context context;

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (1, %d, '%s', '%s')",
                    USER_TABLE, User.Type.ADMIN.getValue(), "admin2", "admin2"));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (2, %d, '%s', '%s')",
                    USER_TABLE, User.Type.CUSTOMER.getValue(), "alice5", "csumb100"));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (3, %d, '%s', '%s')",
                    USER_TABLE, User.Type.CUSTOMER.getValue(), "brian77", "123ABC"));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (4, %d, '%s', '%s')",
                    USER_TABLE, User.Type.CUSTOMER.getValue(), "chris21", "CHRIS21"));

            db.execSQL(CREATE_TRANS_TABLE);
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (1, 2, datetime(), %d, NULL)",
                    TRANS_TABLE, Transaction.Type.NEW_ACCOUNT.getValue()));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (2, 3, datetime(), %d, NULL)",
                    TRANS_TABLE, Transaction.Type.NEW_ACCOUNT.getValue()));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (3, 4, datetime(), %d, NULL)",
                    TRANS_TABLE, Transaction.Type.NEW_ACCOUNT.getValue()));

            db.execSQL(CREATE_CITY_TABLE);
            String cities[] = {"Monterey", "Los Angeles", "Seattle"};
            for (int i = 0; i < cities.length; i++) {
                db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (%d, '%s')",
                        CITY_TABLE, i + 1, cities[i]));
            }

            db.execSQL(CREATE_FLIGHT_TABLE);
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (1, '%s', %d, %d, '%s', %d, %f)",
                    FLIGHT_TABLE, "Otter101", 1, 2, "2017-12-24 10:00:00", 10, 150.0));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (2, '%s', %d, %d, '%s', %d, %f)",
                    FLIGHT_TABLE, "Otter102", 2, 1, "2017-12-24 13:00:00", 10, 150.0));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (3, '%s', %d, %d, '%s', %d, %f)",
                    FLIGHT_TABLE, "Otter201", 1, 3, "2017-12-24 11:00:00", 5, 200.5));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (4, '%s', %d, %d, '%s', %d, %f)",
                    FLIGHT_TABLE, "Otter205", 1, 3, "2017-12-24 15:00:00", 15, 150.0));
            db.execSQL(String.format(Locale.getDefault(),"INSERT INTO %s VALUES (5, '%s', %d, %d, '%s', %d, %f)",
                    FLIGHT_TABLE, "Otter202", 3, 1, "2017-12-24 14:00:00", 5, 200.5));

            db.execSQL(CREATE_RESERVATION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            System.err.println("Upgrading db from version " + oldVersion + " to " + newVersion);
            db.execSQL(DROP_USER_TABLE);
            db.execSQL(DROP_TRANS_TABLE);
            db.execSQL(DROP_CITY_TABLE);
            db.execSQL(DROP_FLIGHT_TABLE);
            db.execSQL(DROP_RESERVATION_TABLE);
            onCreate(db);
        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public Database(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWritableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null) db.close();
    }

    public long insert(User user) {
        ContentValues cv = new ContentValues();
        cv.put(USER_TYPE, user.getType().getValue());
        cv.put(USER_USERNAME, user.getUsername());
        cv.put(USER_PASSWORD, user.getPassword());

        this.openWritableDB();
        long rowID = db.insert(USER_TABLE, null, cv);
        this.closeDB();
        return rowID;
    }

    public long insert(Transaction transaction) {
        ContentValues cv = new ContentValues();
        cv.put(TRANS_USER_ID, transaction.getUserID());
        cv.put(TRANS_TIME, transaction.getTime());
        cv.put(TRANS_TYPE, transaction.getType().getValue());
        cv.put(TRANS_JSON, transaction.getJson());

        this.openWritableDB();
        long rowID = db.insert(TRANS_TABLE, null, cv);
        this.closeDB();
        return rowID;
    }

    long insert(Reservation reservation) {
        ContentValues cv = new ContentValues();
        cv.put(RESERVATION_USER_ID, reservation.getUserID());
        cv.put(RESERVATION_FLIGHT_ID, reservation.getFlightID());
        cv.put(RESERVATION_TICKET, reservation.getTickets());
        cv.put(RESERVATION_TIME, reservation.getTime());

        this.openWritableDB();
        long rowID = db.insert(RESERVATION_TABLE, null, cv);
        this.closeDB();
        return rowID;
    }

    long insert(Flight flight) {
        ContentValues cv = new ContentValues();
        cv.put(FLIGHT_CAPACITY, flight.getCapacity());
        cv.put(FLIGHT_CITY_ARRIVAL, flight.getArrival().getId());
        cv.put(FLIGHT_CITY_DEPARTURE, flight.getDeparture().getId());
        cv.put(FLIGHT_NAME, flight.getName());
        cv.put(FLIGHT_PRICE, flight.getPrice());
        cv.put(FLIGHT_TIME_DEPARTURE, flight.getTime_departure());

        this.openWritableDB();
        long rowID = db.insert(FLIGHT_TABLE, null, cv);
        this.closeDB();
        return rowID;
    }

    User getUser(int id) {
        try {
            String where = USER_ID + "= ?";
            String[] whereArgs = { String.valueOf(id) };
            return getUsers(USER_TABLE, null, where, whereArgs,
                    null, null, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }


    User getUser(String username) {
        try {
            String where = USER_USERNAME + "= ?";
            String[] whereArgs = { username };
            return getUsers(USER_TABLE, null, where, whereArgs,
                    null, null, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    User getUser(String username, String password)  {
        try {
            String where = USER_USERNAME + "= ? AND " + USER_PASSWORD + "= ?";
            String[] whereArgs = { username, password };
            return getUsers(USER_TABLE, null, where, whereArgs,
                    null, null, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    private static User getUser(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                return new User(
                        cursor.getInt(USER_ID_COL),
                        cursor.getInt(USER_TYPE_COL),
                        cursor.getString(USER_USERNAME_COL),
                        cursor.getString(USER_PASSWORD_COL));
            }
            catch (Exception e) {
                return null;
            }
        }
    }

    ArrayList<User> getUsers(String table, String[] columns, String selection,
                             String[] selectionArgs, String groupBy,
                             String having, String orderBy) {
        this.openReadableDB();
        Cursor cursor = db.query(table, columns, selection,
                selectionArgs, groupBy, having, orderBy);
        ArrayList<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            users.add(getUser(cursor));
        }
        cursor.close();
        this.closeDB();
        return users;
    }

    private Transaction getTransaction(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Transaction transaction = new Transaction(
                        cursor.getString(TRANS_TIME_COL),
                        getUser(cursor.getInt(TRANS_USER_ID_COL)),
                        cursor.getInt(TRANS_TYPE_COL),
                        cursor.getString(TRANS_JSON_COL));
                transaction.setId(cursor.getInt(TRANS_ID_COL));
                return transaction;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    ArrayList<Transaction> getTransactions() {
        this.openReadableDB();
        Cursor cursor = db.query(TRANS_TABLE, null, null, null,
                null, null, TRANS_TIME +" DESC");
        ArrayList<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()) {
            transactions.add(getTransaction(cursor));
        }
        cursor.close();
        this.closeDB();
        return transactions;
    }

    private City getCity(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                return new City(cursor.getInt(CITY_ID_COL), cursor.getString(CITY_NAME_EN_COL));
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    City getCity(int id) {
        try {
            String where = CITY_ID + "= ?";
            String[] whereArgs = { String.valueOf(id) };
            return getCities(CITY_TABLE, null, where, whereArgs,
                    null, null, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    City getCity(String name) {
        try {
            String where = CITY_NAME_EN + "= ?";
            String[] whereArgs = { name };
            return getCities(CITY_TABLE, null, where, whereArgs,
                    null, null, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    ArrayList<City> getCities() {
        return getCities(CITY_TABLE, null, null,null,
                null, null, null);
    }

    ArrayList<City> getCities(String table, String[] columns, String selection,
                              String[] selectionArgs, String groupBy,
                              String having, String orderBy) {

        this.openReadableDB();
        Cursor cursor = db.query(table, columns, selection,
                selectionArgs, groupBy, having, orderBy);
        ArrayList<City> cities = new ArrayList<>();
        while (cursor.moveToNext()) {
            cities.add(getCity(cursor));
        }
        cursor.close();
        this.closeDB();
        return cities;
    }

    private Flight getFlight(Cursor cursor) {
        // ToDo: Flight modification
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Flight flight = new Flight();
                flight.setId(cursor.getInt(FLIGHT_ID_COL));
                flight.setArrival(getCity(cursor.getInt(FLIGHT_CITY_ARRIVAL_COL)));
                flight.setCapacity(cursor.getInt(FLIGHT_CAPACITY_COL));
                flight.setDeparture(getCity(cursor.getInt(FLIGHT_CITY_DEPARTURE_COL)));
                flight.setName(cursor.getString(FLIGHT_NAME_COL));
                flight.setPrice(cursor.getDouble(FLIGHT_PRICE_COL));
                flight.setTime_departure(cursor.getString(FLIGHT_TIME_DEPARTURE_COL));
                Flight.mem.put(flight.getId(), flight);
                ArrayList<Reservation> reservations = getReservations(flight);
                for (Reservation reservation : reservations) {
                    reservation.setFlight(flight);
                    flight.addReservation(reservation);
                }
                return flight;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    Flight getFlight(int id) {
        try {
            String where = FLIGHT_ID + "= ?";
            String[] whereArgs = { String.valueOf(id) };
            return getFlights(FLIGHT_TABLE, null, where, whereArgs,
                    null, null, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    Flight getFlight(String flight_number) {
        try {
            String where = FLIGHT_NAME + "= ?";
            String[] whereArgs = { flight_number };
            return getFlights(FLIGHT_TABLE, null, where, whereArgs,
                    null, null, null).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    ArrayList<Flight> getFlights() {
        return getFlights(FLIGHT_TABLE, null, null,null,
                null, null, null);
    }

    ArrayList<Flight> getFlights(String departureCity, String arrivalCity) {
        try {
            String where = FLIGHT_CITY_DEPARTURE + "= ? AND " + FLIGHT_CITY_ARRIVAL + " = ?";
            City departure = getCity(departureCity);
            City arrival = getCity(arrivalCity);
            String[] whereArgs = { String.valueOf(departure.getId()), String.valueOf(arrival.getId())};
            return getFlights(FLIGHT_TABLE, null, where,whereArgs,
                    null, null, null);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    ArrayList<Flight> getFlights(String table, String[] columns, String selection,
                             String[] selectionArgs, String groupBy,
                             String having, String orderBy) {
        this.openReadableDB();
        Cursor cursor = db.query(table, columns, selection,
                selectionArgs, groupBy, having, orderBy);
        ArrayList<Flight> flights = new ArrayList<>();
        while (cursor.moveToNext()) {
            flights.add(getFlight(cursor));
        }
        cursor.close();
        this.closeDB();
        return flights;
    }

    private Reservation getReservation(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Reservation reservation = new Reservation();
                reservation.setId(cursor.getInt(RESERVATION_ID_COL));
                reservation.setTickets(cursor.getInt(RESERVATION_TICKET_COL));
                reservation.setTime(cursor.getString(RESERVATION_TIME_COL));
                reservation.setUser(getUser(cursor.getInt(RESERVATION_USER_ID_COL)));
                Flight flight = Flight.mem.get(cursor.getInt((RESERVATION_FLIGHT_ID_COL)));
                if (flight != null) reservation.setFlight(flight);
                return reservation;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    ArrayList<Reservation> getReservations(Flight flight) {
        try {
            String where = RESERVATION_FLIGHT_ID + "= ?";
            String[] whereArgs = { String.valueOf(flight.getId()) };
            return getReservations(RESERVATION_TABLE, null, where,whereArgs,
                    null, null, null);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<Reservation> getReservations(User user) {
        try {
            String where = RESERVATION_USER_ID + "= ?";
            String[] whereArgs = { String.valueOf(user.getId()) };
            return getReservations(RESERVATION_TABLE, null, where,whereArgs,
                    null, null, null);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    ArrayList<Reservation> getReservations(String table, String[] columns, String selection,
                                 String[] selectionArgs, String groupBy,
                                 String having, String orderBy) {
        this.openReadableDB();
        Cursor cursor = db.query(table, columns, selection,
                selectionArgs, groupBy, having, orderBy);
        ArrayList<Reservation> reservations = new ArrayList<>();
        while (cursor.moveToNext()) {
            reservations.add(getReservation(cursor));
        }
        cursor.close();
        this.closeDB();
        return reservations;
    }

    public int nextReservationID() {
        this.openReadableDB();
        int id;
        try {
            Cursor cursor = db.rawQuery("SELECT max(id) as id FROM " + RESERVATION_TABLE, null);
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(RESERVATION_ID));
            cursor.close();
        } catch (Exception e) {
            id = 0;
        }
        this.closeDB();
        return id + 1;
    }

    void delete(Reservation selected) {
        this.openWritableDB();
        String where = RESERVATION_ID + " =?";
        String[] whereArgs = { String.valueOf(selected.getId()) };
        db.delete(RESERVATION_TABLE, where, whereArgs);
        this.closeDB();
    }

    static String datetime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
