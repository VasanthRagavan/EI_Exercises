import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeParserAdapter {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    public static int toMinutes(String hhmm) throws InvalidTimeException {
        if (hhmm == null) throw new InvalidTimeException("Time string null");
        try {
            LocalTime t = LocalTime.parse(hhmm.trim(), FMT);
            return t.getHour() * 60 + t.getMinute();
        } catch (DateTimeParseException e) {
            throw new InvalidTimeException("Invalid time format (expected HH:mm): " + hhmm);
        }
    }

    public static String toHHmm(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;
        return String.format("%02d:%02d", h, m);
    }
}
