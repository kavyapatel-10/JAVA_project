package util;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import model.Account;
public final class StatePersister {
    private StatePersister() {}
    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent(); if (parent != null) Files.createDirectories(parent);
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) { out.writeObject(accounts); }
    }
    public static Account[] load(Path file) throws IOException {
        if (!Files.exists(file)) return new Account[0];
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Object value = in.readObject();
            if (!(value instanceof Account[] accounts)) throw new IOException("Saved state does not contain Account[]");
            return accounts;
        } catch (ClassNotFoundException e) { throw new IOException("Unable to load saved account classes", e); }
    }
}
