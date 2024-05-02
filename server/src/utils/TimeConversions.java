package utils;

import java.sql.Timestamp;
import java.time.Instant;

import org.jetbrains.annotations.NotNull;


public class TimeConversions {
    
    public static Timestamp TimestampfromDateString(@NotNull String dateString){
        return Timestamp.from(Instant.parse(dateString + "T00:00:00.00Z"));
    }
}
