package model;

public class Student extends User {

    private int studentID;   // students.id
    private String program;
    private int year;

    // ===== CORRECT constructor (USE WHEN students.id IS KNOWN)
    public Student(int studentID, int userID, String email, String name, String program, int year) {
        super(userID, email, name, "Student");
        this.studentID = studentID;
        this.program = program;
        this.year = year;
    }

    // ===== BACKWARD-COMPATIBLE constructor (USED BY EXISTING CODE)
    public Student(int userID, String email, String name, String program, int year) {
        super(userID, email, name, "Student");
        this.program = program;
        this.year = year;
        this.studentID = -1; // unresolved for now
    }

    // ===== Lightweight constructor (unchanged)
    public Student(int studentID, String program) {
        super(0, "", "", "Student");
        this.studentID = studentID;
        this.program = program;
    }

    // ===== GETTERS
    public int getStudentID() {
        return studentID;
    }

    public String getProgram() {
        return program;
    }

    public int getYear() {
        return year;
    }

    // ===== SETTERS
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
