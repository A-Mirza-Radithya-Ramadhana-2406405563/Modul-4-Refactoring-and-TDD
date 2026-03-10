package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrderPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/create");
        assertEquals("Create Order", driver.getTitle());
    }

    @Test
    void createOrder_submitsSuccessfully(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/create");

        driver.findElement(By.name("productName")).sendKeys("Test Product");
        driver.findElement(By.name("quantity")).sendKeys("5");
        driver.findElement(By.name("author")).sendKeys("John Doe");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(driver.getCurrentUrl().contains("/order/history"));
    }

    @Test
    void orderHistoryPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/history");
        assertEquals("Order History", driver.getTitle());
    }

    @Test
    void orderHistoryPost_displaysOrderList(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/history");

        driver.findElement(By.name("author")).sendKeys("John Doe");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertEquals("Order List", driver.getTitle());
    }

    @Test
    void paymentPage_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/pay/dummy-order-id");
        assertEquals("Payment", driver.getTitle());
    }
}