package configurations;

/**
 * @param <T>
 * @Author Amade Ali
 * @see FileTextReaderImpl
 */
public interface FileTextReader<T> {

    /**
     * Reads the text file,
     */
    void read();


    /**
     * <h4>Writes one or more pieces of data to the text file</h4>
     * Example: <h2>saveAll(new Person("Antony")); saveAll(new Person("Antony"),new Person("Tonny")) ; saveAll(array);</h2>
     *
     * @param objects
     */
    void saveAll(T... objects);

}
