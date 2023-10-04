package katalon;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTests {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public  void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @BeforeMethod
    public void  navigateToMyAccountPage(){
        driver.manage().deleteAllCookies();
        driver.navigate().to("https://cms.demo.katalon.com");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("ul.nav-menu > li:nth-child(3) > a")).click();
    }

    @Test (priority = 1)
    public void checkIfLoginPageLoaded(){
        wait
                .withMessage("Title should contain: My account – Katalon Shop")
                .until(ExpectedConditions.titleContains("My account – Katalon Shop"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/my-account"), "Url should contain /my-account");
    }

    @Test (priority = 1)
    public void checkInputTypes(){
        WebElement email = driver.findElement(By.id("username"));
        Assert.assertEquals(email.getAttribute("type"), "text", "Username input should be text type.");

        WebElement password = driver.findElement(By.id("password"));
        Assert.assertEquals(password.getAttribute("type"), "password", "Password input should be password type.");

        WebElement checkbox = driver.findElement(By.id("rememberme"));
        Assert.assertEquals(checkbox.getAttribute("type"), "checkbox", "Remember me checkbox should be checkbox type.");
        Assert.assertFalse(checkbox.isSelected(), "Remember me should be unchecked by default.");
    }

    @Test (priority = 3)
    public void displayErrorWhenCredentialsAreWrong() {
        String email = "invalidemail@gmail.com";
        String password = "invalid123";

        driver.findElement(By.xpath("//input[@type='text']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        wait
                .withMessage("Alert is not present")
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@role='alert']")));

        By alert = (By.xpath("//ul[@role='alert']/li"));
        wait.until(ExpectedConditions.presenceOfElementLocated(alert));
        Assert.assertTrue(driver.findElement(alert).getText().contains("ERROR: Invalid email address."), "Text should be ERROR: Invalid email address");

    }
    @Test (priority = 4)
    public void  successfulLoginWithValidCredentials() {
        String email = "customer";
        String password = "crz7mrb.KNG3yxv1fbn";

        driver.findElement(By.xpath("//input[@type='text']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        wait
                .withMessage("Logout button is not present")
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("nav.woocommerce-MyAccount-navigation  li:last-child > a")));
    }


    @AfterClass
    public void closeDriver() {
        driver.quit();
    }
}
