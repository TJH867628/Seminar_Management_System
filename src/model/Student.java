package model;

public class Student extends User {

    private String program;
    private int year;
    private int studentID;

    public Student(int userID, String email, String name,String program, int year) {

        super(userID, email, name, "Student");

        this.program = program;
        this.year = year;
    }

    public Student (int studentID,  String program) {
        super(0, "", "", "Student"); // Default values for User fields
        this.studentID = studentID;
        this.program = program;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getProgram() {
        return program;
    }

    public int getYear() {
        return year;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return getName() + " - " + program;
    }
}