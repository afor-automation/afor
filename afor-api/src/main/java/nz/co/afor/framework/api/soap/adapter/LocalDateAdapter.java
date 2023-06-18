package nz.co.afor.framework.api.soap.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;

/**
 * Created by belcherm on 16/06/2017.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String v) {
        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) {
        if (v != null) {
            return v.toString();
        } else {
            return null;
        }
    }
}