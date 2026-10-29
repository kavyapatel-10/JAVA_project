package util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.annotation.Id;
public class Repository<T> {
    private final Map<String, T> items = new HashMap<>();
    public synchronized T save(T entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null");
        String id = extractId(entity);
        items.put(id, entity);
        return entity;
    }
    @SafeVarargs public final synchronized void saveAll(T... entities) { for (T entity : entities) save(entity); }
    public synchronized T findById(String id) { return items.get(id); }
    public synchronized ArrayList<T> findAll() { return new ArrayList<>(items.values()); }
    public synchronized boolean delete(String id) { return items.remove(id) != null; }
    private String extractId(T entity) {
        Class<?> type = entity.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null) throw new IllegalArgumentException("@Id field cannot be null");
                        return value.toString();
                    } catch (ReflectiveOperationException e) { throw new IllegalStateException("Cannot read @Id field", e); }
                }
            }
            type = type.getSuperclass();
        }
        throw new IllegalArgumentException("No field annotated with @Id found on " + entity.getClass().getName());
    }
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) return 0.0;
        double total = 0; for (N value : values) { if (value == null) throw new IllegalArgumentException("Values cannot contain null"); total += value.doubleValue(); }
        return total / values.length;
    }
}
