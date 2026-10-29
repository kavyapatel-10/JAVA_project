package util;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
public final class ReportGenerator {
    private ReportGenerator() {}
    public static String generate(Path logsFolder, Path reportFile) throws IOException {
        long deposits = 0L, withdrawals = 0L;
        List<String> details = new ArrayList<>();
        if (Files.exists(logsFolder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsFolder)) {
                for (Path file : stream) {
                    if (!Files.isRegularFile(file)) continue;
                    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 3) {
                                try { long amount = Long.parseLong(parts[2].trim()); if (parts[1].equalsIgnoreCase("DEPOSIT")) deposits += amount; else if (parts[1].equalsIgnoreCase("WITHDRAW")) withdrawals += amount; }
                                catch (NumberFormatException ignored) { /* Ignore non-standard log rows. */ }
                            }
                        }
                    }
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    details.add(file.getFileName() + " | size=" + attrs.size() + " bytes | modified=" + attrs.lastModifiedTime());
                }
            }
        }
        StringBuilder report = new StringBuilder("=== MiniBank End-of-Day Report ===\n");
        report.append("Total deposits: Rs. ").append(deposits).append('\n');
        report.append("Total withdrawals: Rs. ").append(withdrawals).append('\n');
        report.append("Net movement: Rs. ").append(deposits - withdrawals).append('\n');
        report.append("Log files:\n"); for (String detail : details) report.append(" - ").append(detail).append('\n');
        Path parent = reportFile.toAbsolutePath().getParent(); if (parent != null) Files.createDirectories(parent);
        Files.writeString(reportFile, report.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return report.toString();
    }
}
