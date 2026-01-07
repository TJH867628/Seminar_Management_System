package model;

import model.User;

public class Admin extends User {
    private int id;
    private int userID;
    private String email;
    private String name;

    public Admin(int id, int userID, String email, String name) {
        super(userID, email, name, "Admin");
        this.id = id;
        this.userID = userID;
        this.email = email;
        this.name = name;
    }
}
