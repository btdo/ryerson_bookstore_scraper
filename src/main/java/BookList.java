import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import models.Book;

public class BookList {
    FileWriter out;
    CSVPrinter printer;

    public BookList() throws IOException {
        File file = new File("book_new.csv");
        out = new FileWriter("book_new.csv");
        if (file.exists()) {
            printer = CSVFormat.DEFAULT.print(out);
        } else {
            printer = CSVFormat.DEFAULT.withHeader("section", "title", "author", "isbn", "price", "isRequired")
                    .print(out);
        }
    }

    public void parseBook(WebDriver driver) throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("course")));
        WebElement element = driver.findElement(By.className("course"));
        String section = getSection(element);
        getProducts(section, element);
    }

    public String getSection(WebElement element) {
        WebElement header = element.findElement(By.className("header-lead"));
        WebElement section = header.findElement(By.tagName("h3"));
        if (section != null) {
            return section.getText();
        }
        return null;
    }

    public Book getProducts(String section, WebElement element) throws IOException {
        List<WebElement> elementList = element.findElements(By.className("products"));
        for (WebElement bookElement : elementList) {
            getBookListProduct(section, bookElement);
        }
        return null;
    }

    public Book getBookListProduct(String section, WebElement bookElement) throws IOException {
        WebElement bookListProduct = bookElement.findElement(By.className("booklist-product"));
        String title = getBookTitle(bookListProduct);
        String author = getBookAuthor(bookListProduct);
        String isbn = getISBN(bookListProduct);
        String price = getPrice(bookListProduct).replaceAll("Select Option", "").replaceAll("Select Item", "")
                .replaceAll("[\\n\\r]", " ").replaceAll("\\s{2,}", " ").trim();
        boolean isRequired = isRequired(bookListProduct);
        Book book = new Book(section, title, author, isbn, price, isRequired);
        log(book.toString());
        printer.printRecord(section, title, author, isbn, price, isRequired);
        return book;
    }

    public boolean isRequired(WebElement element) {
        try {
            element.findElement(By.className("booklist-required"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getBookTitle(WebElement element) {
        try {
            return element.findElement(By.tagName("a")).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getBookAuthor(WebElement element) {
        try {
            return element.findElement(By.className("booklist-author")).getText();
        } catch (Exception e) {
            return "";
        }

    }

    public String getISBN(WebElement element) {
        try {
            return element.findElement(By.className("isbn-information")).getText();
        } catch (Exception e) {
            return "";
        }

    }

    public String getPrice(WebElement element) {
        try {
            return element.findElement(By.className("product-controls")).getText();
        } catch (Exception e) {
            return "";
        }
    }

    private void log(String txt) {
        System.out.println(txt);
    }

    public void done() {
        try {
            out.close();
            printer.close();
        } catch (Exception e) {
            log("Exception while closing file " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
