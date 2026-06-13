import java.util.Scanner;
import models.Event;
import models.Student;
import services.EventManager;
import services.ValidationService;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        EventManager manager = new EventManager();
        manager.loadData(); // Automatically load saved records on startup

        while (true) {
            System.out.println("\n===== CAMPUS EVENT MANAGEMENT =====");
            System.out.println("1. Staff");
            System.out.println("2. Student");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
                continue; 
            }

            switch (choice) {
                case 1:
                    staffMenu(manager);
                    break;
                case 2:
                    studentMenu(manager);
                    break;
                case 3:
                    System.out.println("Saving data and exiting system...");
                    manager.saveData(); 
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void staffMenu(EventManager manager) {
        while (true) { 
            System.out.println("\n--- STAFF MENU ---");
            System.out.println("1. Create Event");
            System.out.println("2. View Events");
            System.out.println("3. Update Event Details");
            System.out.println("4. Cancel/Remove Event");
            System.out.println("5. Sort Events");
            System.out.println("6. Search Events");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
                continue; 
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Event ID: ");
                    int id;
                    try {
                        id = scanner.nextInt();
                        scanner.nextLine();
                        if (id <= 0 || manager.findEventById(id) != null) {
                            System.out.println("Error: ID must be positive and completely unique.");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid numeric type caught.");
                        scanner.nextLine();
                        continue;
                    }

                    System.out.print("Enter Event Name: ");
                    String name = scanner.nextLine();
                    if (!ValidationService.isValidString(name)) {
                        System.out.println("Error: Field cannot remain blank.");
                        continue;
                    }

                    System.out.print("Enter Event Date (dd/mm/yyyy): ");
                    String date = scanner.nextLine();
                    if (!ValidationService.isValidDate(date)) {
                        System.out.println("Error: Formatting must precisely match dd/mm/yyyy.");
                        continue;
                    }

                    System.out.print("Enter Event Time (HH:mm): ");
                    String time = scanner.nextLine();
                    if (!ValidationService.isValidTime(time)) {
                        System.out.println("Error: Format must use a 24-hr layout (HH:mm).");
                        continue;
                    }

                    System.out.print("Enter Location: ");
                    String location = scanner.nextLine();
                    if (!ValidationService.isValidString(location)) {
                        System.out.println("Error: Location string field required.");
                        continue;
                    }

                    System.out.print("Enter Max Participants: ");
                    int max;
                    try {
                        max = scanner.nextInt();
                        scanner.nextLine();
                        if (max <= 0) {
                            System.out.println("Error: Event capacities must exceed 0.");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid capacity entry.");
                        scanner.nextLine();
                        continue;
                    }

                    Event event = new Event(id, name, date, time, location, max);
                    manager.addEvent(event);
                    manager.saveData(); 
                    System.out.println("Event initialized successfully.");
                    break;

                
                case 2:
                    manager.displayEvents(); // Displays the clean master summary table
                    
                    System.out.print("Enter Event ID to view full registrations/waitlist (or 0 to go back): ");
                    int viewId;
                    try {
                        viewId = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                        scanner.nextLine();
                        continue;
                    }

                    if (viewId != 0) {
                        Event selectedEvent = manager.findEventById(viewId);
                        if (selectedEvent != null) {
                            selectedEvent.displayFullEventDetails(); // Triggers the student tables view
                        } else {
                            System.out.println("Event not found.");
                        }
                    }
                    break;

                case 3:
                    updateEventMenu(manager);
                    manager.saveData(); 
                    break;

                case 4:
                    System.out.print("Enter Event ID to cancel: ");
                    int cancelId = scanner.nextInt();
                    scanner.nextLine();
                    manager.removeEvent(cancelId); 
                    manager.saveData(); 
                    break;

                case 5:
                    sortEventsMenu(manager);
                    break;

                case 6:
                    handleSearchMenu(manager);
                    break;

                case 7:
                    return; 

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void studentMenu(EventManager manager) {
        while (true) { 
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. Register for Event");
            System.out.println("2. View Events");
            System.out.println("3. Cancel Registration");
            System.out.println("4. Search Events");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    manager.displayEvents();
                    System.out.print("Enter Event ID to register: ");
                    int eventId = scanner.nextInt();
                    scanner.nextLine();

                    Event foundEvent = manager.findEventById(eventId);
                    if (foundEvent == null) {
                        System.out.println("Event not found.");
                        continue;
                    }

                    System.out.print("Enter First Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Surname: ");
                    String surname = scanner.nextLine();
                    System.out.print("Enter Student Number: ");
                    String studentNumber = scanner.nextLine();

                    if (!ValidationService.isValidString(name) || 
                        !ValidationService.isValidString(surname) || 
                        !ValidationService.isValidString(studentNumber)) {
                        System.out.println("Error: Registration details cannot be blank.");
                        continue;
                    }

                    Student student = new Student(name, surname, studentNumber);
                    foundEvent.registerStudent(student);
                    manager.saveData(); 
                    break;

                
                case 2:
                    manager.displayEvents(); // Displays the master summary table
                    
                    System.out.print("Enter Event ID to check your registration/waitlist status (or 0 to go back): ");
                    int studentViewId;
                    try {
                        studentViewId = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                        scanner.nextLine();
                        continue;
                    }

                    if (studentViewId != 0) {
                        Event selectedEvent = manager.findEventById(studentViewId);
                        if (selectedEvent != null) {
                            selectedEvent.displayFullEventDetails(); // Triggers the student tables view
                        } else {
                            System.out.println("Event not found.");
                        }
                    }
                    break;

                case 3:
                    manager.displayEvents();
                    System.out.print("Enter Event ID: ");
                    int cancelEventId = scanner.nextInt();
                    scanner.nextLine();

                    Event cancelEvent = manager.findEventById(cancelEventId);
                    if (cancelEvent == null) {
                        System.out.println("Event not found.");
                        continue;
                    }

                    System.out.print("Enter Student Number: ");
                    String cancelStudentNumber = scanner.nextLine();

                    cancelEvent.cancelRegistration(cancelStudentNumber);
                    manager.saveData(); 
                    break;

                case 4:
                    handleSearchMenu(manager);
                    break;

                case 5:
                    return; 

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void updateEventMenu(EventManager manager) {
        System.out.print("Enter Event ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Event event = manager.findEventById(id);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }

        System.out.println("\nUpdating Event: " + event.getEventName());
        System.out.println("1. Update Name");
        System.out.println("2. Update Time");
        System.out.println("3. Update Location");
        System.out.print("Choose field to update: ");
        
        int fieldChoice = scanner.nextInt();
        scanner.nextLine();

        switch (fieldChoice) {
            case 1:
                System.out.print("Enter new Name: ");
                String newName = scanner.nextLine();
                if (ValidationService.isValidString(newName)) {
                    event.setEventName(newName);
                    System.out.println("Event name updated successfully.");
                } else {
                    System.out.println("Invalid value. Update aborted.");
                }
                break;
            case 2:
                System.out.print("Enter new Time (HH:mm): ");
                String newTime = scanner.nextLine();
                if (ValidationService.isValidTime(newTime)) {
                    event.setEventTime(newTime);
                    System.out.println("Event time updated successfully.");
                } else {
                    System.out.println("Invalid format. Update aborted.");
                }
                break;
            case 3:
                System.out.print("Enter new Location: ");
                String newLocation = scanner.nextLine();
                if (ValidationService.isValidString(newLocation)) {
                    event.setLocation(newLocation);
                    System.out.println("Event location updated successfully.");
                } else {
                    System.out.println("Invalid value. Update aborted.");
                }
                break;
            default:
                System.out.println("Invalid choice. Modification aborted.");
        }
    }

    public static void sortEventsMenu(EventManager manager) {
        System.out.println("\n--- Sorting Options ---");
        System.out.println("1. Sort by Event Name");
        System.out.println("2. Sort by Event Date");
        System.out.print("Choose sorting type: ");
        
        int sortChoice = scanner.nextInt();
        scanner.nextLine();

        if (sortChoice == 1) {
            manager.sortByName();
        } else if (sortChoice == 2) {
            manager.sortByDate();
        } else {
            System.out.println("Invalid option.");
        }
    }

    public static void handleSearchMenu(EventManager manager) {
        System.out.println("\n--- SEARCH EVENTS ---");
        System.out.println("1. Search by Name Match (Partial or Full)");
        System.out.println("2. Search by Date String (dd/mm/yyyy)");
        System.out.print("Choose search option: ");
        
        int mode;
        try {
            mode = scanner.nextInt();
            scanner.nextLine(); 
        } catch (Exception e) {
            System.out.println("Invalid numeric option. Returning to menu.");
            scanner.nextLine(); 
            return;
        }

        if (mode == 1) {
            System.out.print("Enter search keyword: ");
            String keyword = scanner.nextLine();
            manager.searchByEventName(keyword);
        } else if (mode == 2) {
            System.out.print("Enter exact date (dd/mm/yyyy): ");
            String dateString = scanner.nextLine();
            manager.searchByEventDate(dateString);
        } else {
            System.out.println("Invalid search option chosen.");
        }
    }
}