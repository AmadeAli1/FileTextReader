package configurations;

import annotations.FileConfiguration;
import annotations.FileReaderListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <T>
 * @Author <h2>Amade Ali</h2>
 *
 * <h4>Provides the implementation of FileTextReader that provides the reading and writing of data, the collected data is transmitted from the data structure <Set> that will help to avoid duplicate data (requires the custom implementation of the equals method).</h4>
 * @see Set,HashSet
 */
public class FileTextReaderImpl<T> implements FileTextReader<T> {
    private final Class<T> clazz;
    private final FileReaderListener<T> listener;

    public FileTextReaderImpl(Class<T> clazz, FileReaderListener<T> listener) {
        this.listener = listener;
        this.clazz = clazz;
        read();
    }


    @Override
    public void read() {
        String filename = clazz.getAnnotation(FileConfiguration.class).filename();
        String separator = clazz.getAnnotation(FileConfiguration.class).separator();
        java.io.FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Set<T> sets = new HashSet<>();
        try {
            fileReader = new java.io.FileReader(filename);
            bufferedReader = new BufferedReader(fileReader);
            var next = bufferedReader.readLine();
            StringTokenizer tok;

            while (next != null) {
                tok = new StringTokenizer(next, separator);
                T data = clazz.getConstructor().newInstance();
                for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
                    if (tok.hasMoreElements()) {
                        String[] value = tok.nextToken().split("=");
                        var field = clazz.getDeclaredField(value[0].trim());
                        field.setAccessible(true);
                        if (field.isAnnotationPresent(annotations.Field.class)) {
                            var type = field.getAnnotation(annotations.Field.class).type();
                            var valid = field.getAnnotation(annotations.Field.class).valid();

                            if (valid.length != 0) {
                                var ks = Arrays.stream(valid).filter(s -> s.equalsIgnoreCase(value[1])).count();
                                if (ks == 0) {
                                    throw new IllegalArgumentException("This Field " + value[0] + " required valid data ::> " + Arrays.stream(valid).toList() + " <::");
                                }
                            }

                            var result = type.convert(value[1]);
                            field.set(data, result);
                        }
                    } else {
                        break;
                    }
                }
                sets.add(data);
                next = bufferedReader.readLine();
            }
            listener.onResult(sets);
        } catch (IllegalAccessException | IOException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            if (e instanceof NoSuchMethodException) {
                System.err.println("Required empty constructor in class" + clazz.getSimpleName());
                System.err.printf("Add this public class %s(){}%n", clazz.getSimpleName());
            }else{
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Objects.requireNonNull(fileReader).close();
                Objects.requireNonNull(bufferedReader).close();
            } catch (Exception e) {
                System.out.println("Close streams error :" + e.getMessage());
            }
        }
    }

    private void save(T object, BufferedWriter bufferedWriter, String separator) {
        if (isValid(object)) {
            try {
                StringBuilder line = new StringBuilder();
                saveData(separator, line, bufferedWriter, object, clazz);
            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            System.err.println("Unable to write to file with invalid data");
        }

    }

    private boolean isValid(T object) {
        Class<?> clazz = object.getClass();
        var state = true;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(annotations.Field.class)) {
                try {
                    var type = field.getAnnotation(annotations.Field.class).type();
                    var min = field.getAnnotation(annotations.Field.class).min();
                    var max = field.getAnnotation(annotations.Field.class).max();

                    var x = switch (type) {
                        case INTEGER -> (Integer) field.get(object);
                        case BYTE -> (Byte) field.get(object);
                        case DOUBLE -> (Double) field.get(object);
                        case SHORT -> (Short) field.get(object);
                        case FLOAT -> (Float) field.get(object);
                        case LONG -> (Long) field.get(object);
                        case BOOLEAN -> String.valueOf((boolean) field.get(object)).length();
                        case STRING -> ((String) field.get(object)).length();
                    };

                    if (valid(min, max, (long) x)) {
                        System.out.println("Invalid data in the field " + field.getName() + " Min:" + min + " Max:" + max + " Input:" + ((long) x));
                        state = false;
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

        return state;
    }

    @Override
    @SafeVarargs
    public final void saveAll(T... objects) {
        var filename = clazz.getAnnotation(FileConfiguration.class).filename();
        var separator = clazz.getAnnotation(FileConfiguration.class).separator();
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        var state = true;
        try {
            fileWriter = new FileWriter(filename, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (T data : objects) {
                if (isValid(data)) {
                    save(data, bufferedWriter, separator);
                } else {
                    System.err.println("Unrecorded information {" + data + "}");
                    state = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    // fileWriter.close();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (state) {
            System.out.println("Successfully Written Data");
            listener.onResult(Arrays.stream(objects).collect(Collectors.toSet()));
        }
    }

    private void saveData(String separator, StringBuilder line, BufferedWriter buff, T data, Class<?> clazzy)
            throws IllegalAccessException, IOException {
        int count = 0;
        for (Field field : clazzy.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(annotations.Field.class)) {
                var sv = field.getName() + "=" + field.get(data);
                if (count == clazzy.getDeclaredFields().length - 1) {
                    line.append(sv);
                    break;
                } else {
                    line.append(sv).append(separator);
                }
            }
            count++;
        }
        buff.write(line.toString());
        buff.newLine();
    }

    private boolean valid(long min, long max, long current) {
        return current < min || current > max;
    }

}
