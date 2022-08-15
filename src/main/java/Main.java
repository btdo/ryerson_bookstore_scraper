
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
    private WebDriver driver;
    private BookList bookList;
    private int departmentIndex = 0;
    private int courseIndex = 0;
    private int sectionIndex = 0;

    public boolean start() {
        try {
            System.out.println("Starting at " + departmentIndex + ", " + courseIndex + ", " + sectionIndex);
            bookList = new BookList();
            driver = new ChromeDriver();
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
            driver.get("https://campusstore.torontomu.ca/courselistbuilder");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("clDeptSelectBox")));
            System.out.println("Website Title: " + driver.getTitle());
            scrape();
            return true;
        } catch (Throwable e) {
            System.out.println("Error at: " + departmentIndex + ", " + courseIndex + ", " + sectionIndex);
            return false;
        } finally {
            if (bookList != null) {
                bookList.done();
            }
            if (driver != null) {
                driver.close();
            }
        }
    }

    public void scrape() throws Throwable {
        Select dropdown = new Select(driver.findElement(By.id("clDeptSelectBox")));
        int i = 0;
        for (WebElement department : dropdown.getOptions()) {
            if (i < departmentIndex) {
                i++;
                continue;
            }
            department.click();
            Thread.sleep(500);
            selectCourse();
            departmentIndex++;
            i = departmentIndex;
            courseIndex = 0;
            sectionIndex = 0;
        }
    }

    public void selectCourse() throws Throwable {
        Select dropdown = new Select(driver.findElement(By.id("clCourseSelectBox")));
        int i = 0;
        for (WebElement course : dropdown.getOptions()) {
            if (i < courseIndex) {
                i++;
                continue;
            }
            course.click();
            Thread.sleep(500);
            chooseSection();
            courseIndex++;
            i = courseIndex;
            sectionIndex = 0;
        }
    }

    public void chooseSection() throws Throwable {
        Select dropdown = new Select(driver.findElement(By.id("clSectionSelectBox")));
        int i = 0;
        for (WebElement section : dropdown.getOptions()) {
            if (i < sectionIndex) {
                i++;
                continue;
            }

            section.click();
            Thread.sleep(500);
            chooseBooks();
            sectionIndex++;
            i = sectionIndex;
        }
    }

    public void chooseBooks() throws Throwable {
        System.out.println("Scanning " + departmentIndex + ", " + courseIndex + ", " + sectionIndex);
        driver.findElement(By.id("choose-books-btn")).click();
        Thread.sleep(500);
        bookList.parseBook(driver);
        driver.navigate().back();
        Thread.sleep(500);
        removeCourse();
    }

    public void removeCourse() throws Throwable {
        WebElement element = driver.findElement(By.className("clRemoveCourse")).findElement(By.tagName("a"));
        element.click();
        Thread.sleep(500);
    }

    public static void main(String args[]) throws Throwable {
        Main main = new Main();
        boolean isDone = false;
        while (!isDone) {
            isDone = main.start();
        }
    }
}
