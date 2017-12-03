package cn.donaldong.airlineticketreservationsystem;

class User {
    private int id;
    private String username, pwd;
    private Type type;

    public enum Type {
        ADMIN(0), CUSTOMER(1);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Type valueOf(int i) {
            if (i == 0) return ADMIN;
            return CUSTOMER;
        }
    }

    User(int id, int type, String username, String password) {
        this.id = id;
        this.type = Type.valueOf(type);
        this.username = username;
        this.pwd = password;
    }

    User(int type, String username, String password) {
        this.type = Type.valueOf(type);
        this.username = username;
        this.pwd = password;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return pwd;
    }

    public void setId(int id) {
        this.id = id;
    }
}
