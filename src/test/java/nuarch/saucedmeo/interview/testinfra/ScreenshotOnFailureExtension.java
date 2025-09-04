package nuarch.saucedmeo.interview.testinfra;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public record ScreenshotOnFailureExtension(Supplier<WebDriver> driverSupplier) implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext ctx, Throwable cause) {
        WebDriver driver = driverSupplier.get();
        String safeName = ctx.getDisplayName().replaceAll("[^a-zA-Z0-9._-]+", "_");
        String ts = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
        Path dir = Paths.get("target", "artifacts", safeName + "-" + ts);

        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {

        }

        // Screenshot
        if (driver instanceof TakesScreenshot tsDriver) {
            byte[] png = tsDriver.getScreenshotAs(OutputType.BYTES);

            try {
                Files.write(dir.resolve("screenshot.png"), png);
            } catch (IOException ignored) {

            }
        }

        // Browser console (Chrome/Edge)
        try {
            var logs = driver.manage().logs().get("browser");

            if (logs != null) {
                StringBuilder sb = new StringBuilder();
                logs.forEach(entry -> sb.append(entry.getLevel()).append(" ")
                        .append(entry.getTimestamp()).append(" ")
                        .append(entry.getMessage()).append("\n"));
                Files.writeString(dir.resolve("console.log"), sb.toString());
            }
        } catch (Exception ignored) {
        }

        System.out.println("[ARTIFACTS] Saved to: " + dir.toAbsolutePath());
    }

}
