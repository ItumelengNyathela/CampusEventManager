package services;

import java.util.regex.Pattern;

public class ValidationService {

    // Regex patterns for matching strict standard structures
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^\\d{2}:\\d{2}$");

    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isValidDate(String date) {
        if (!DATE_PATTERN.matcher(date).matches()) return false;
        // Basic range breakdown checking
        String[] parts = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        return (day >= 1 && day <= 31) && (month >= 1 && month <= 12);
    }

    public static boolean isValidTime(String time) {
        if (!TIME_PATTERN.matcher(time).matches()) return false;
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return (hour >= 0 && hour <= 23) && (minute >= 0 && minute <= 59);
    }
}