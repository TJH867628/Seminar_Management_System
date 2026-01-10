package model;

public abstract class User{

    protected int userID;
    protected String email;
    protected String name;
    protected String role;

    public User(int userID, String email, String name, String role) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}