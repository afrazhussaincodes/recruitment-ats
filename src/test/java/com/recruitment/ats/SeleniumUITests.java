
package com.recruitment.ats;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeleniumUITests {

    private WebDriver driver;
    private WebDriverWait wait;
    private boolean testPassed;

    private static final String BASE_URL =
            System.getProperty("app.url", "http://localhost:8080");

    @BeforeEach
    void setUp() {
        testPassed = false;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1440,1000");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        try {
            if (driver != null) {
                if (!testPassed) {
                    saveFailureScreenshot();
                }

                driver.quit();
            }
        } catch (Exception e) {
            System.err.println(
                    "Browser cleanup issue: " + e.getMessage()
            );
        }
    }

    private void saveFailureScreenshot() {
        try {
            Path directory =
                    Path.of("test-evidence", "screenshots");

            Files.createDirectories(directory);

            String fileName = "failure-" +
                    System.currentTimeMillis() + ".png";

            File screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Path destination = directory.resolve(fileName);

            Files.copy(
                    screenshot.toPath(),
                    destination
            );

            System.out.println(
                    "Failure screenshot saved: " +
                    destination.toAbsolutePath()
            );

        } catch (Exception e) {
            System.err.println(
                    "Screenshot capture failed: " +
                    e.getMessage()
            );
        }
    }

    @Test
    void homepageLoadsSuccessfully() {
        driver.get(BASE_URL);

        assertEquals(
                "Recruitment ATS",
                driver.getTitle()
        );

        assertTrue(
                driver.getPageSource().contains(
                        "Find your next opportunity."
                ),
                "Homepage hero heading should be present"
        );

        testPassed = true;
    }

    @Test
    void browseOpenJobsNavigatesToJobsSection() {
        driver.get(BASE_URL);

        WebElement browseButton = wait.until(
                d -> d.findElement(
                        By.xpath(
                                "//button[contains(.,'Browse open jobs')]"
                        )
                )
        );

        browseButton.click();

        WebElement jobsSection = wait.until(
                d -> d.findElement(By.id("jobsView"))
        );

        assertTrue(
                jobsSection.isDisplayed(),
                "Jobs section should be visible"
        );

        testPassed = true;
    }

    @Test
    void jobSearchFieldAcceptsSearchText() {
        driver.get(BASE_URL);

        WebElement browseButton = wait.until(
                d -> d.findElement(
                        By.xpath(
                                "//button[contains(.,'Browse open jobs')]"
                        )
                )
        );

        browseButton.click();

        WebElement searchField = wait.until(
                d -> d.findElement(By.id("jobSearch"))
        );

        assertTrue(
                searchField.isDisplayed(),
                "Job search field should be visible"
        );

        searchField.sendKeys("Software Engineer");

        assertEquals(
                "Software Engineer",
                searchField.getAttribute("value"),
                "Search field should accept user input"
        );

        testPassed = true;
    }
}
