
import java.util.regex.Pattern;

public class ToSpokenEnglishConverter {

    private static final String[] numberNames = {
            "",
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
            "ten",
            "eleven",
            "twelve",
            "thirteen",
            "fourteen",
            "quarter",
            "sixteen",
            "seventeen",
            "eighteen",
            "nineteen",
    };

    private static final String[] tensDigitsNames = {
            "",
            "ten",
            "twenty",
            "half",
    };

    public static String convertToSpokenTime(String time) throws IllegalArgumentException {
        StringBuilder timeInEnglish = new StringBuilder();
        Integer hour, minute;
        validateInput(time);
        String[] hourAndMinutes = time.split(":");
        try {
            hour = Integer.parseInt(hourAndMinutes[0]);
            minute = Integer.parseInt(hourAndMinutes[1]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Incorrect input format: " + time + ", only accepted formats are HH:MM or H:MM");
        }
        validateRangeOfInput(hour, minute);

        if(minute.equals(0)){
            handleSpecialCases(timeInEnglish, hour);
        } else {
            appendWithSpokenTime(timeInEnglish, hour, minute);
        }

        return timeInEnglish.toString().replace("  ", " ");
    }

    private static void appendWithSpokenTime(StringBuilder timeInEnglish, Integer hour, Integer minute) {
        if(minute <= 30) {
            minutesToSpokenEnglish(timeInEnglish, minute);
            timeInEnglish.append(" ").append("past");
        } else {
            minute = 60 - minute;
            hour = hour + 1;
            minutesToSpokenEnglish(timeInEnglish, minute);
            timeInEnglish.append(" ").append("to");
        }
        if(hour == 0) {
            hour = 12;
        }
        timeInEnglish.append(" ").append(numberNames[hour]);
    }

    private static void validateRangeOfInput(Integer hour, Integer minute) {
        if (hour < 0 || hour > 12) {
            throw new IllegalArgumentException("Incorrect hour, it needs to be between 0-12");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Incorrect minutes, it needs to be between 0-59");
        }
    }

    private static void handleSpecialCases(StringBuilder timeInEnglish, Integer hour) {
        if (hour.equals(12)){
            timeInEnglish.append("noon");
        } else if (hour.equals(0)){
            timeInEnglish.append("midnight");
        }
        else {
            timeInEnglish.append(numberNames[hour]).append(" o'clock");
        }
    }

    private static void validateInput(String time) {
        Pattern pattern = Pattern.compile("[0,1]?[0-9]:[0-5][0-9]");
        if(time == null || !pattern.matcher(time).find()){
            throw new IllegalArgumentException("Only 12 hour clock time in format of HH:MM is accepted as input.");
        }
    }

    private static void minutesToSpokenEnglish(StringBuilder timeInEnglish, Integer minute) {
        int firstMinuteDigit = (int) Math.floor(minute / 10.);
        int secondMinuteDigit = minute % 10;
        if(minute >= 20){
            timeInEnglish.append(tensDigitsNames[firstMinuteDigit]).append(" ").append(numberNames[secondMinuteDigit]);
        } else {
            timeInEnglish.append(numberNames[minute]);
        }
    }
}
