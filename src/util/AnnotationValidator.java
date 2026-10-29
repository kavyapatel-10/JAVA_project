package util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import model.annotation.Positive;
import model.annotation.MaxLength;
public final class AnnotationValidator {
    private AnnotationValidator() {}
    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();
        if (obj == null) return new String[] { "Object cannot be null" };
        Class<?> type = obj.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (field.isAnnotationPresent(Positive.class) && value instanceof Number n && n.doubleValue() <= 0) {
                        errors.add(field.getName() + " " + field.getAnnotation(Positive.class).message());
                    }
                    if (field.isAnnotationPresent(MaxLength.class) && value instanceof String s && s.length() > field.getAnnotation(MaxLength.class).value()) {
                        errors.add(field.getName() + " must have at most " + field.getAnnotation(MaxLength.class).value() + " characters");
                    }
                } catch (ReflectiveOperationException | RuntimeException ex) {
                    errors.add("Unable to validate field " + field.getName() + ": " + ex.getMessage());
                }
            }
            type = type.getSuperclass();
        }
        return errors.toArray(new String[0]);
    }
}
