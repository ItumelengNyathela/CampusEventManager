package models;

public class Student extends Users {

    private String studentNumber;

    public Student(String name, String surname, String studentNumber) {

        super(name, surname);

        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    @Override
    public String toString() {
        return studentNumber + " - " + name + " " + surname;
    }
}