package com.github.zocker160;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class TimeHelper {
    public static String getTimeString(Duration duration) {
        if (duration == null) return "";

        if (duration.getStandardDays() > 30)
            return duration.getStandardDays()+" days ("+(duration.getStandardDays()/365f)+" years)";

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix("d ")
                .appendHours()
                .appendSuffix("h ")
                .appendMinutes()
                .appendSuffix("m ")
                .appendSeconds()
                .appendSuffix("s ")
                .toFormatter();

        return formatter.print(duration.toPeriod().normalizedStandard());
    }
}
