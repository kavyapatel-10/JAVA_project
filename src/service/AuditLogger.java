package service;
import java.lang.reflect.Method;
import model.annotation.Audit;
public final class AuditLogger {
    private AuditLogger() {}
    public static void printAuditedMethods(Class<?> type) {
        for (Method method : type.getDeclaredMethods()) if (method.isAnnotationPresent(Audit.class)) System.out.println("AUDIT: " + type.getSimpleName() + "." + method.getName());
    }
}
