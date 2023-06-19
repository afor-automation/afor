package nz.co.afor.framework.api.soap.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDateTime;

/**
 * Created by Matt on 16/06/2017.
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String v) {
        return LocalDateTime.parse(v);
    }

    @Override
    public String marshal(LocalDateTime v) {
        if (v != null) {
            return v.toString();
        } else {
            return null;
        }
    }
}