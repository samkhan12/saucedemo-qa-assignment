package com.saucedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public final class Evidence {

    private Evidence() {
    }

    public static Path screenshot(WebDriver driver, String name, String dir) {
        try {
            Path folder = Paths.get(dir);
            Files.createDirectories(folder);
            Path src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Path target = folder.resolve(name + ".png");
            Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
            return target;
        } catch (IOException e) {
            System.err.println("Could not save screenshot " + name + ": " + e.getMessage());
            return null;
        }
    }

    public static void note(String file, String line) {
        try {
            Path path = Paths.get(file);
            Files.createDirectories(path.getParent());
            String text = "[" + LocalDateTime.now() + "] " + line + System.lineSeparator();
            Files.writeString(path, text, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("OBSERVATION: " + line);
        } catch (IOException e) {
            System.err.println("Could not write note: " + e.getMessage());
        }
    }
}
