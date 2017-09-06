package nz.co.afor.framework;

import java.util.List;

/**
 * Created by Matt on 01/08/2016.
 */
public class Lists {
    public static <T> T getFirst(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public static <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }
}
