package nz.co.afor.reports;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public interface ReportContext {

    ZonedDateTime START_TIME = ZonedDateTime.now();

    default String getReportHeading() {
        return ReportContextProvider.getReportHeading();
    }

    default String getReportTitle() {
        return ReportContextProvider.getReportTitle();
    }

    default DateTimeFormatter getDateFormat() {
        return ReportContextProvider.getDateFormat();
    }

    default ZoneId getTimezone() {
        return ReportContextProvider.getTimezone();
    }

    default String getFormattedTitle() {
        return getReportTitle() + " " + getDateFormat().format(START_TIME.withZoneSameInstant(getTimezone()));
    }

    default long getTotalRunTime() {
        return (ZonedDateTime.now().toEpochSecond() * 1000) - (START_TIME.toEpochSecond() * 1000);
    }
}
