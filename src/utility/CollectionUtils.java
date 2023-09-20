package utility;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CollectionUtils {
    public static <E> boolean haveSameElements(Collection<E> c1, Collection<E> c2) {
        // If both collections are null or the same reference, they are equal
        if (c1 == c2) {
            return true;
        }

        // If one is null and the other is not, they are not equal
        if (c1 == null || c2 == null) {
            return false;
        }

        // If the sizes are different, they can't have the same elements
        if (c1.size() != c2.size()) {
            return false;
        }

        return c2.containsAll(c1);
    }
}
