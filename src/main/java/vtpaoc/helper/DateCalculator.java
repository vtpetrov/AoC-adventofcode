package vtpaoc.helper;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateCalculator {

    static LocalDateTime timestampInMillisToDate(final Long timestampInMillis) {
        return LocalDateTime.ofEpochSecond(timestampInMillis / 1000, 0, getDefaultZoneOffset());
    }

    static LocalDateTime timestampInSecondsToDate(final Long timestampInSeconds) {
        return LocalDateTime.ofEpochSecond(timestampInSeconds, 0, getDefaultZoneOffset());
    }

    public Instant getCurrentDateTimeUTC() {
        Instant currentDateTime = java.time.Clock.systemUTC().instant();
        return currentDateTime;
    }

    public String getCurrentDateAsString() {
        String currentDate;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        currentDate = dtf.format(localDate);
        return currentDate;
    }

    public LocalDate getDateFromString(final String date, final String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date, dtf);
    }

    public String calculateDateTimeInDaysBeforeTodaysDate(final Integer daysToGoBack, final String pattern) {
        long milliseconds = daysToGoBack * 1000 * 60 * 60 * 24;
        Instant currentInstantDateTime = getCurrentDateTimeUTC();

        Instant calculatedInstantDateTime = currentInstantDateTime.minusMillis(milliseconds);
        ZonedDateTime convertedDateTime = ZonedDateTime.ofInstant(calculatedInstantDateTime, ZoneOffset.UTC);
        String finalDateTime = DateTimeFormatter.ofPattern(pattern).format(convertedDateTime);

        return finalDateTime;
    }

    /**
     * Get the seconds since the epoch start 1970-01-01T00:00:00Z in UTC time zone until NOW.
     *
     * @return the calculated seconds
     */
    public static long getSecondsSinceEpochUTC() {
        return getSecondsSinceEpochUTC(Instant.now());
    }

    /**
     * Get the seconds since the epoch start 1970-01-01T00:00:00Z in UTC time zone until given point in time.
     *
     * @param to Until when (particular point in time)
     * @return the calculated seconds
     */
    public static long getSecondsSinceEpochUTC(final Instant to) {
        return to.getEpochSecond();
    }

    public static ZoneOffset getDefaultZoneOffset() {
        return ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
    }

}
