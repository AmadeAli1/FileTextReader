package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author <h2>Amade Ali</h2>
 * <h4>This annotation enables you to choose the location of the text file and how it will be read.</h4>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FileConfiguration {

    /**
     * Directory of the file to be handled
     *
     * @return File path
     */
    String filename();

    /**
     * Separator that will be used
     *
     * @return separator
     */
    String separator();
}
