package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Event {

    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String location;
    private int maxParticipants;

    private ArrayList<Student> registeredStudents;
    private Queue<Student> waitlist;

    public Event(int eventId, String eventName,
                 String eventDate, String eventTime,
                 String location, int maxParticipants) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.location = location;
        this.maxParticipants = maxParticipants;

        registeredStudents = new ArrayList<>();
        waitlist = new LinkedList<>();
    }

    public void registerStudent(Student student) {

        // Prevent duplicate registration tracking
        for (Student s : registeredStudents) {
            if (s.getStudentNumber().equalsIgnoreCase(student.getStudentNumber())) {
                System.out.println("Error: Student is already registered for this event.");
                return;
            }
        }
        for (Student s : waitlist) {
            if (s.getStudentNumber().equalsIgnoreCase(student.getStudentNumber())) {
                System.out.println("Error: Student is already on the waitlist for this event.");
                return;
            }
        }

        if (registeredStudents.size() < maxParticipants) {

            registeredStudents.add(student);

            System.out.println(student.getStudentNumber()
                    + " registered successfully.");

        } else {

            waitlist.add(student);

            System.out.println("Event full. Student added to waitlist.");
        }
    }

    public void cancelRegistration(String studentNumber) {
        Student foundStudent = null;

        // Step 1: Search active participants
        for (Student s : registeredStudents) {
            if (s.getStudentNumber().equals(studentNumber)) {
                foundStudent = s;
                break;
            }
        }

        if (foundStudent != null) {
            registeredStudents.remove(foundStudent);
            System.out.println("Registration cancelled.");

            // Promote the next person waiting in line (FIFO)
            if (!waitlist.isEmpty()) {
                Student promoted = waitlist.poll();
                registeredStudents.add(promoted);
                System.out.println("Student " + promoted.getStudentNumber() + " promoted from waitlist.");
            }
            return; // Exit method early since we found and handled the cancellation
        }

        // Step 2: If not found in active registrations, check the waitlist queue
        // Queue's removeIf handles searching and removing the match elegantly
        boolean removedFromWaitlist = waitlist.removeIf(s -> s.getStudentNumber().equals(studentNumber));

        if (removedFromWaitlist) {
            System.out.println("Waitlist entry cancelled successfully.");
        } else {
            System.out.println("Student not found in registrations or waitlist.");
        }
    }

    public void displayFullEventDetails() {
        // 1. Print General Event Info Header
        System.out.println("\n" + "=" .repeat(60));
        System.out.println("EVENT DETAILS: " + eventName.toUpperCase());
        System.out.println("=" .repeat(60));
        System.out.printf("ID: %-15d | Date: %-12s | Time: %s\n", eventId, eventDate, eventTime);
        System.out.printf("Location: %-9s | Max Capacity: %d\n", location, maxParticipants);
        System.out.println("-" .repeat(60));

        // 2. Print Registered Students Table
        System.out.println("\n[ REGISTERED PARTICIPANTS ] (" + registeredStudents.size() + "/" + maxParticipants + ")");
        if (registeredStudents.isEmpty()) {
            System.out.println("   No students currently registered.");
        } else {
            System.out.println("   " + "-".repeat(50));
            System.out.printf("   %-15s | %-30s\n", "Student No.", "Full Name");
            System.out.println("   " + "-".repeat(50));
            for (Student s : registeredStudents) {
                String fullName = s.getName() + " " + s.getSurname();
                System.out.printf("   %-15s | %-30.30s\n", s.getStudentNumber(), fullName);
            }
            System.out.println("   " + "-".repeat(50));
        }

        // 3. Print Waitlist Queue Table
        System.out.println("\n[ WAITLIST QUEUE ] (" + waitlist.size() + " in line)");
        if (waitlist.isEmpty()) {
            System.out.println("   Waitlist is empty.");
        } else {
            System.out.println("   " + "-".repeat(50));
            System.out.printf("   %-5s | %-12s | %-25s\n", "Pos", "Student No.", "Full Name");
            System.out.println("   " + "-".repeat(50));
            
            int position = 1;
            for (Student s : waitlist) {
                String fullName = s.getName() + " " + s.getSurname();
                System.out.printf("   %-5d | %-12s | %-25.25s\n", position++, s.getStudentNumber(), fullName);
            }
            System.out.println("   " + "-".repeat(50));
        }
        System.out.println("=" .repeat(60) + "\n");
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
    return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getLocation() {
        return location;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Student> getRegisteredStudents() {
        return registeredStudents;
    }   

        public Queue<Student> getWaitlist() {
            return waitlist;
        }

    @Override
    public String toString() {
        return "Event ID: " + eventId +
            "\nName: " + eventName +
            "\nDate: " + eventDate +
            "\nTime: " + eventTime +
            "\nLocation: " + location +
            "\nMax Participants: " + maxParticipants +
            "\nRegistered: " + registeredStudents.size() +
            "\nWaitlist: " + waitlist.size() +
            "\n----------------------";
    }
}


