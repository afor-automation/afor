package nz.co.afor.framework.api.soap.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalTime;

/**
 * Created by Matt on 16/06/2017.
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    @Override
    public LocalTime unmarshal(String v) {
        return LocalTime.parse(v);
    }

    @Override
    public String marshal(LocalTime v) {
        if (v != null) {
            return v.toString();
        } else {
            return null;
        }
    }
}