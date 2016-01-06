package nz.co.afor.framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Matt Belcher on 12/10/2015.
 */
@Component
public class GsonFactory {
    private com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    @Value(value = "${nz.co.afor.fixture.dateformat:null}")
    String dateFormat;

    public GsonFactory() {
        if (null != dateFormat)
            getGsonBuilder().setDateFormat(dateFormat);
    }

    public Gson getGson() {
        return getGsonBuilder().create();
    }
}
