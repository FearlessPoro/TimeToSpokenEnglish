import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


public class TimeToSpokenEnglishTest {

    @ParameterizedTest
    @CsvSource({"1:00, one o'clock",
                "2:05, five past two",
                "3:11, eleven past three",
                "4:17, seventeen past four",
                "5:20, twenty past five",
                "06:25, twenty five past six",
                "6:32, twenty eight to seven",
                "7:30, half past seven",
                "7:35, twenty five to eight",
                "08:40, twenty to nine",
                "9:48, twelve to ten",
                "10:51, nine to eleven",
                "11:57, three to twelve",
                "00:00, midnight",
                "00:30, half past twelve",
                "00:46, fourteen to one",
                "12:15, quarter past twelve",
                "12:00, noon"
    })
    void testTimeToSpokenEnglishHappyPath(String time, String expectedResult){
            assertEquals(expectedResult, ToSpokenEnglishConverter.convertToSpokenTime(time));
    }

    @ParameterizedTest
    @CsvSource({"13:25", "21:13", "-3:03", "212:12"})
    void testTimeToSpokenEnglishHoursOutOfBounds(String time) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                        () -> ToSpokenEnglishConverter.convertToSpokenTime(time));
            assertTrue(e.getMessage().contains("Incorrect hour, it needs to be between 0-12"));
    }

    @ParameterizedTest
    @CsvSource({"12:75", "2:99", "05:-4", "Garbage", "03/14"})
    void testTimeToSpokenEnglishMinutesOutOfBounds(String time) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ToSpokenEnglishConverter.convertToSpokenTime(time));
        assertTrue(e.getMessage().contains("Only 12 hour clock time in format of HH:MM is accepted as input."));
    }

    @Test
    void testTimeToSpokenEnglishNullInputHandling() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ToSpokenEnglishConverter.convertToSpokenTime(null));
        assertTrue(e.getMessage().contains("Only 12 hour clock time in format of HH:MM is accepted as input."));
    }
}
