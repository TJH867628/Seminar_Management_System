package model;

public class Coordinator extends User{
    protected int coordinatorID;
    protected String department;

    public Coordinator(int userID, String email, String name, int coordinatorID, String department) {
        super(userID, email, name, "Coordinator");

        this.coordinatorID = coordinatorID;
        this.department = department;
    }

    public int getCoordinatorID() {
        return coordinatorID;
    }

    public String getDepartment() {
        return department;
    }
}
