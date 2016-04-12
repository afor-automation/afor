package nz.co.afor.framework.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt Belcher on 12/04/2016.
 */
public class ApiModel {
    List<String> listOfString = new ArrayList<String>() {
        {
            add("String A");
            add("String B");
            add("String C");
        }
    };
    Integer integer = 5;
    String string = "testing";

    public List<String> getListOfString() {
        return listOfString;
    }

    public void setListOfString(List<String> listOfString) {
        this.listOfString = listOfString;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
