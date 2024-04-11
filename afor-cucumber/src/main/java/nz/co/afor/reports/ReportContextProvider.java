package nz.co.afor.reports;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Matt on 7/04/2016.
 */
@Component
public class ReportContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static String reportTitle;
    private static DateTimeFormatter dateFormat;
    private static ZoneId timezone;

    public static String getReportTitle() {
        if (null == reportTitle)
            return "Automation Results";
        return reportTitle;
    }

    public static DateTimeFormatter getDateFormat() {
        if (null == dateFormat)
            return DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss");
        return dateFormat;
    }

    public static ZoneId getTimezone() {
        if (null == timezone)
            return ZoneId.of("UTC");
        return timezone;
    }

    @Value("${nz.co.afor.report.title:Automation Results}")
    public void setReportTitle(String reportTitle) {
        ReportContextProvider.reportTitle = "Afor Automation - " + reportTitle;
    }

    @Value("${nz.co.afor.report.date.format:yyyy-MMM-dd HH:mm:ss}")
    public void setDateFormat(String dateFormat) {
        ReportContextProvider.dateFormat = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Value("${nz.co.afor.report.date.timezone:UTC}")
    public void setTimezone(String timezone) {
        ReportContextProvider.timezone = ZoneId.of(timezone);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ReportContextProvider.applicationContext = applicationContext;
    }
}
