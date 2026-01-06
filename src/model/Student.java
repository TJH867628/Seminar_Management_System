package model;

public class Student extends User {

    private String program;
    private int year;

    public Student(int userID, String email, String name,String program, int year, int sessionID) {

        super(userID, email, name, "Student");

        this.program = program;
        this.year = year;
    }

    public String getProgram() {
        return program;
    }

    public int getYear() {
        return year;
    }
}