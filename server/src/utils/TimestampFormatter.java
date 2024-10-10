package utils;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneId;

public final class TimestampFormatter {
    
    private TimestampFormatter() {}

    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneId.of("UTC"));

    public static String convertTimestampToISOFormatUTC(Timestamp timestamp) {
        return formatter.format(timestamp.toInstant());
    }

    public static Timestamp parseFromClientRequest(String stringEncodedTimestamp) {
        Instant instant = Instant.parse(stringEncodedTimestamp);
        return Timestamp.from(instant);
    }
}
