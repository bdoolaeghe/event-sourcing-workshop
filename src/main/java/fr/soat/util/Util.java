package fr.soat.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Util {

    public static <T> List<T> append(List<T> list, T object) {
        if (list == null) {
            list = new ArrayList<>();
        }
        return Stream.concat(list.stream(),
                Stream.of(object))
                .collect(toList());
    }

}
