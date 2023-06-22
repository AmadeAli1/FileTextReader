package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author <h2>Amade Ali</h2>
 * <h4>This annotation ensures that you specify some metadata for each attribute that will be read or written to the text file</h4>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

    /**
     * Type of data that the field will receive, by default it is String
     * @return Datatype
     */
    DataType type() default DataType.STRING;

    /**
     * Minimum value, for example if it is String it will validate according to the length of the sequence if it is number it will validate normally
     * @return Number
     */
    long min() default 1;


    /**
     * Maximum value, for example if it is String it will validate according to the length of the sequence if it is number it will validate normally
     * @return Number
     */
    long max() default Long.MAX_VALUE;

    /**
     * Array of options that will be valid for this specific field
     * @return String array
     */
    String[] valid() default {};

}
