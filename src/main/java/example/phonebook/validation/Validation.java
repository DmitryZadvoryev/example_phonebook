package example.phonebook.validation;

import java.util.List;
import java.util.Map;

public final class Validation {


    public static <T> boolean requireNull(T value) {
        return value == null;
    }

    public static <T> boolean requireNonNull(T value) {
        return value != null;
    }

    public static <K, V> boolean requireIsEmpty(Map<K, V> item) {
        return item.isEmpty();
    }

    public static <K, E> boolean requireIsEmpty(List<E> list) {
        return list.isEmpty();
    }

    public static <T, K, V> boolean require(T value, Map<K, V> list) {
        return requireNull(value) || requireIsEmpty(list);
    }

    public static <T, E> boolean require(T value, List<E> list) {
        return requireNull(value) || requireIsEmpty(list);
    }
}

