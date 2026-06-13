package models;

public class Users {

    protected String name;
    protected String surname;

    public Users(String name, String surname) {

        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}