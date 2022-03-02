import java.util.regex.Pattern;

/**
 * This class is used as a wrapper for a static method of convertToSpokenTime used to convert time to english
 * spoken language
 * @author Wojciech Franczuk
 */
public class ToSpokenEnglishConverter {

    //This array is used to store names of consecutive numbers used in conversion of time
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

    //This array stores names of multiples of ten, used to avoid the need of storing every combination of digits as words.
    private static final String[] tensDigitsNames = {
            "",
            "ten",
            "twenty",
            "half",
    };

    /**
     * Returns english words representing the spoken time given as input.
     *
     * @param time 12 hour clock time in format of HH:MM that is to be converted to spoken english
     * @return the String representing the spoken form of specified input
     * @throws IllegalArgumentException when input is not in required format
     */
    public static String convertToSpokenTime(String time) throws IllegalArgumentException {
        int hour, minute;
        validateInput(time);
        String[] hourAndMinutes = time.split(":");
        try {
            hour = Integer.parseInt(hourAndMinutes[0]);
            minute = Integer.parseInt(hourAndMinutes[1]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Incorrect input format: " + time + ", only accepted formats are HH:MM or H:MM");
        }
        validateRangeOfInput(hour);

        return createSpokenString(hour, minute);
    }

    /**
     * Appends the timeInEnglish parameter with complete spoken time.
     * @param hour the hour to be converted to word form
     * @param minute the minutes to be converted to word form
     * @return the full english spoken time representing the input values
     */
    private static String createSpokenString(Integer hour, Integer minute) {
        StringBuilder timeInEnglish = new StringBuilder();
        if(minute.equals(0)) {
            return convertZeroMinutesTime(hour);
        }
        if(minute <= 30) {
            timeInEnglish.append(convertMinutesToSpokenEnglish(minute)).append(" ").append("past");
        } else {
            minute = 60 - minute;
            hour = hour + 1;
            timeInEnglish.append(convertMinutesToSpokenEnglish(minute)).append(" ").append("to");
        }

        //Ugly solution to handling the special case of naming the "00" hour
        //needed because it cannot be mapped in numberNames without breaking other uses of 0 as empty String.
        if(hour == 0) {
            hour = 12;
        }
        return timeInEnglish.append(" ").append(numberNames[hour]).toString().replace("  ", " ");
    }

    /**
     * Used to validate the input for incorrect time formats such as 24 hour clock (eg. 16:45)
     * @param hour the hour to be validated
     * @throws IllegalArgumentException if the input is not valid
     */
    private static void validateRangeOfInput(Integer hour) throws IllegalArgumentException {
        if (hour < 0 || hour > 12) {
            throw new IllegalArgumentException("Incorrect hour, it needs to be between 0-12");
        }
    }

    /**
     * Method needed to handle special cases of wording in english language when the minute count is equal to 0.
     * Examples: noon (12:00) and midnight (00:00), as well as "o'clock".
     * @param hour the hour to be converted to words
     * @return the final string of converted special cases
     */
    private static String convertZeroMinutesTime(Integer hour) {
        if (hour.equals(12)){
            return  "noon";
        } else if (hour.equals(0)){
            return  "midnight";
        } else {
            return numberNames[hour] + " o'clock";
        }
    }

    /**
     * Uses regex to validate input as 12 hour clock time in format of HH:MM
     * @param time to be validated
     * @throws IllegalArgumentException if the input is not valid (eg. 12:78)
     */
    private static void validateInput(String time) {
        Pattern pattern = Pattern.compile("[0,1]?[0-9]:[0-5][0-9]");
        if(time == null || !pattern.matcher(time).find()){
            throw new IllegalArgumentException("Only 12 hour clock time in format of HH:MM is accepted as input.");
        }
    }

    /**
     * appends the leading minute part of the spoken english time to the resulting
     * @param minute the minute valeu to be converted
     * @return the minute part of spoken english time
     */
    private static String convertMinutesToSpokenEnglish(Integer minute) {
        int firstMinuteDigit = (int) Math.floor(minute / 10.);
        int secondMinuteDigit = minute % 10;
        if(minute >= 20){
            return tensDigitsNames[firstMinuteDigit] + " " + numberNames[secondMinuteDigit];
        } else {
            return numberNames[minute];
        }
    }
}
