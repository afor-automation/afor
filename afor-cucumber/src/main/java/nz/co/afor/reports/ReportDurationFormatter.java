package nz.co.afor.reports;

import static java.lang.String.format;

public interface ReportDurationFormatter {

    int ONE_SECOND = 1000;
    int ONE_MINUTE = 60000;

    default String formatDuration(long milliseconds) {
        if (milliseconds < ONE_SECOND) {
            return format("%s milliseconds", milliseconds);
        } else if (milliseconds < ONE_MINUTE) {
            long seconds = milliseconds / ONE_SECOND;
            return format("%s second%s", seconds, seconds > 1 ? "s" : "");
        }
        long minutes = milliseconds / ONE_MINUTE;
        long seconds = (milliseconds - (minutes * ONE_MINUTE)) / ONE_SECOND;
        return format("%s minute%s, %s seconds", minutes, minutes > 1 ? "s" : "", seconds);
    }
}
