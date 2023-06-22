package annotations;

import java.util.List;
import java.util.Set;

/**
 * This listener ensures that you collect the data that has been read.
 *
 * @param <T>
 */
public interface FileReaderListener<T> {

    /**
     * Collects all read data and adds it to a list
     */
    void onResult(Set<T> data);

}
