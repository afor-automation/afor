package nz.co.afor.framework;

import java.util.List;
import java.util.Random;

/**
 * Created by Matt on 01/08/2016.
 */
public class Collections {

    @Deprecated
    public static <T> T getFirst(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    @Deprecated
    public static <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }

    public static <T> T anyOf(List<T> ts) {
        if (null == ts || ts.size() == 0)
            return null;
        return ts.get(new Random().nextInt(ts.size()));
    }

    @Deprecated
    public static <T> T getFirst(T[] ts) {
        return ts != null && !(ts.length == 0) ? ts[0] : null;
    }

    @Deprecated
    public static <T> T getLast(T[] ts) {
        return ts != null && !(ts.length == 0) ? ts[ts.length - 1] : null;
    }

    public static <T> T anyOf(T[] ts) {
        if (null == ts || ts.length == 0)
            return null;
        return ts[new Random().nextInt(ts.length)];
    }
}
