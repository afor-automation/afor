package nz.co.afor.framework.api.soap.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Matt on 16/06/2017.
 */
public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime unmarshal(String v) {
        return ZonedDateTime.parse(v);
    }

    @Override
    public String marshal(ZonedDateTime v) {
        if (v != null) {
            return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(v);
        } else {
            return null;
        }
    }
}