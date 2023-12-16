package pl.qbawalat.file.relations.resolver.api.file.util;

import java.util.HashSet;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CollectionsToolbox {
    public static boolean hasUniqueValues(List<?> list) {
        return list.size() == new HashSet<>(list).size();
    }
}
