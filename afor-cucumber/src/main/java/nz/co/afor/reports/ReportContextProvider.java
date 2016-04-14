package nz.co.afor.reports;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Matt on 7/04/2016.
 */
@Component
public class ReportContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private static String reportTitle;
    private static String dateFormat;

    public static String getReportTitle() {
        return reportTitle;
    }

    public static String getDateFormat() {
        return dateFormat;
    }

    @Value("${nz.co.afor.report.title:Automation Results}")
    public void setReportTitle(String reportTitle) {
        ReportContextProvider.reportTitle = reportTitle;
    }

    @Value("${nz.co.afor.report.date.format:yyyy-MMM-dd HH:mm:ss}")
    public void setDateFormat(String dateFormat) {
        ReportContextProvider.dateFormat = dateFormat;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ReportContextProvider.applicationContext = applicationContext;
    }
}
