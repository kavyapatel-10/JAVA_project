package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.Positive;
import model.annotation.MaxLength;

public class AnnotationValidator {

    public static String[] validate(Object obj) {

        List<String> errors = new ArrayList<>();

        if (obj == null) {
            errors.add("Object cannot be null");
            return errors.toArray(new String[0]);
        }

        Class<?> clazz = obj.getClass();

        while (clazz != null) {

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {

                        Positive positive =
                                field.getAnnotation(Positive.class);

                        if (value instanceof Number number
                                && number.doubleValue() <= 0) {

                            errors.add(
                                    field.getName() + " "
                                            + positive.message()
                            );
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {

                        MaxLength maxLength =
                                field.getAnnotation(MaxLength.class);

                        if (value instanceof String str
                                && str.length() > maxLength.value()) {

                            errors.add(
                                    field.getName()
                                            + " must have at most "
                                            + maxLength.value()
                                            + " characters"
                            );
                        }
                    }

                } catch (IllegalAccessException e) {
                    errors.add(
                            "Unable to validate field: "
                                    + field.getName()
                    );
                }
            }

            clazz = clazz.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}