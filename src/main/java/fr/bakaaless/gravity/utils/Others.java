package fr.bakaaless.gravity.utils;

import java.util.ArrayList;
import java.util.List;

public class Others {

    public static String getIntCircle(final int index) {
        final String[] dataSet = new String[]{"⓪","①","②","③","④","⑤","⑥","⑦","⑧","⑨","⑩"};
        return dataSet[index % dataSet.length];
    }

    public static <T> List<T> copy(final List<T> source) {
        final List<T> result = new ArrayList<>();
        for (final T value : source) {
            result.add(value);
        }
        return result;
    }
}
