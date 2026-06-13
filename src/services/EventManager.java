package services;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import models.Event;
import models.Student;

public class EventManager {

    private ArrayList<Event> events = new ArrayList<>();
    private final String FILE_PATH = "Events_data.txt";

    public EventManager() {
        events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public Event findEventById(int id) {

        for (Event e : events) {

            if (e.getEventId() == id) {
                return e;
            }
        }

        return null;
    }

    public void displayEvents() {
        if (events.isEmpty()) {
            System.out.println("No events currently scheduled.");
            return;
        }

        System.out.println("\n" + "=" .repeat(95));
        System.out.println("                          CAMPUS EVENTS SCHEDULE                              ");
        System.out.println("=" .repeat(95));
        
        // Define the Table Header with specific column spacing
        // %-10s = Left-justified String with 10 characters width
        // %-25s = Left-justified String with 25 characters width
        // %-12s = Left-justified String with 12 characters width
        // %-8s  = Left-justified String with 8 characters width
        // %-15s = Left-justified String with 15 characters width
        // %-12s = Left-justified String with 12 characters width
        System.out.printf("%-10s | %-25s | %-12s | %-8s | %-15s | %-12s\n", 
                "ID", "Event Name", "Date", "Time", "Location", "Reg/Max (Wait)");
        System.out.println("-" .repeat(95));

        // Print each event as a formatted row
        for (Event e : events) {
            String attendanceTracker = String.format("%d/%d (%d)", 
                    e.getRegisteredStudents().size(), 
                    e.getMaxParticipants(), 
                    e.getWaitlist().size());

            System.out.printf("%-10d | %-25.25s | %-12s | %-8s | %-15.15s | %-12s\n", 
                    e.getEventId(), 
                    e.getEventName(), 
                    e.getEventDate(), 
                    e.getEventTime(), 
                    e.getLocation(), 
                    attendanceTracker);
        }
        System.out.println("=" .repeat(95) + "\n");
    }

    public void removeEvent(int id) {

        Event foundEvent = findEventById(id);

        if (foundEvent != null) {

            events.remove(foundEvent);

            System.out.println("Event cancelled successfully.");

        } else {

            System.out.println("Event not found.");
        }

        
    }

    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write human-readable metadata headers at the top of the file
            writer.write("# =========================================================\n");
            writer.write("# CAMPUS EVENT MANAGEMENT SYSTEM DATABASE\n");
            writer.write("# =========================================================\n");
            writer.write("# Format Reminders:\n");
            writer.write("# EVENT | ID | Name | Date | Time | Location | MaxCapacity\n");
            writer.write("# REG   | First Name | Surname | Student Number\n");
            writer.write("# WAIT  | First Name | Surname | Student Number\n");
            writer.write("# =========================================================\n\n");

            for (Event e : events) {
                // Write event details cleanly separated by uniform pipes
                writer.write(String.format("EVENT | %d | %s | %s | %s | %s | %d\n",
                        e.getEventId(), e.getEventName(), e.getEventDate(),
                        e.getEventTime(), e.getLocation(), e.getMaxParticipants()));
                
                // Write registered students indented slightly for human scannability
                for (Student s : e.getRegisteredStudents()) {
                    writer.write(String.format("  REG | %s | %s | %s\n", 
                            s.getName(), s.getSurname(), s.getStudentNumber()));
                }
                
                // Write waitlisted students
                for (Student s : e.getWaitlist()) {
                    writer.write(String.format(" WAIT | %s | %s | %s\n", 
                            s.getName(), s.getSurname(), s.getStudentNumber()));
                }
                // Add a clean blank line between different events
                writer.write("\n");
            }
            System.out.println("System data successfully saved to disk.");
        } catch (IOException e) {
            System.out.println("Persistence Error: Failure saving system state. " + e.getMessage());
        }
    }

    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Event currentEvent = null;

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                // Skip empty lines or comment reminder blocks
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                // Split by pipe and clear extra whitespace out of individual fields
                String[] tokens = trimmedLine.split("\\|");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim();
                }

                switch (tokens[0]) {
                    case "EVENT":
                        int id = Integer.parseInt(tokens[1]);
                        String name = tokens[2];
                        String date = tokens[3];
                        String time = tokens[4];
                        String loc = tokens[5];
                        int max = Integer.parseInt(tokens[6]);
                        
                        currentEvent = new Event(id, name, date, time, loc, max);
                        events.add(currentEvent);
                        break;

                    case "REG":
                        if (currentEvent != null) {
                            Student s = new Student(tokens[1], tokens[2], tokens[3]);
                            currentEvent.getRegisteredStudents().add(s);
                        }
                        break;

                    case "WAIT":
                        if (currentEvent != null) {
                            Student s = new Student(tokens[1], tokens[2], tokens[3]);
                            currentEvent.getWaitlist().add(s);
                        }
                        break;
                }
            }
            System.out.println("System data auto-loaded successfully. Total events loaded: " + events.size());
        } catch (Exception e) {
            System.out.println("Persistence Error: Failure reconstructing system state. File may be corrupted.");
        }
    }

    public void sortByName() {

        events.sort(
            Comparator.comparing(Event::getEventName)
        );

        System.out.println("Events sorted by name.");
    }

    public void sortByDate() {
        events.sort((e1, e2) -> {
            // Expecting "dd/mm/yyyy" format splits
            String[] date1 = e1.getEventDate().split("/");
            String[] date2 = e2.getEventDate().split("/");
            
            // Compare Years first
            int yearCompare = date1[2].compareTo(date2[2]);
            if (yearCompare != 0) return yearCompare;
            
            // Compare Months second
            int monthCompare = date1[1].compareTo(date2[1]);
            if (monthCompare != 0) return monthCompare;
            
            // Compare Days last
            return date1[0].compareTo(date2[0]);
        });

        System.out.println("Events sorted chronologically by date.");
    }

    public void searchByEventName(String partialName) {
        boolean found = false;
        System.out.println("\n--- Search Results for '" + partialName + "' ---");
        for (Event e : events) {
            if (e.getEventName().toLowerCase().contains(partialName.toLowerCase())) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No matching events found by name.");
    }

    public void searchByEventDate(String date) {
        boolean found = false;
        System.out.println("\n--- Search Results for Date: " + date + " ---");
        for (Event e : events) {
            if (e.getEventDate().equals(date)) {
                System.out.println(e);
                found = true;
            }
        }
        if (!found) System.out.println("No matching events found for that date.");
    }
}