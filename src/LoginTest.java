import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.firefox.FirefoxDriver; // for Firefox 
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver; // for chrome

/**
 * Note: Test environment: Chrome Version 76, selenium 3.14.0, Java 8,
 * ChromeDriver 2.42, JUnit 5
 */

public class LoginTest {
	private WebDriver driver;
	private String url = "http://www.lwhawk.com/home.html";
	private WebElement username;
	private WebElement password;
	private WebElement submit;
	private WebElement error;
	private WebDriverWait wait;

	@AfterEach
	public void teardown() {
		driver.quit(); // close the browser
	}

	/**Open up login page before every test
	 */
	@BeforeEach
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:/Software Testing/Driver/chromedriver.exe"); // specify path
																										// the the
																										// driver
		driver = new ChromeDriver(); // create an instance of the web browser and open it
		driver.get(url); // open the given url
		username = driver.findElement(By.id("login-username"));
		password = driver.findElement(By.id("login-password"));
		submit = driver.findElement(By.className("btn-primary"));
		error = driver.findElement(By.id("error"));
		wait = new WebDriverWait(driver, 10);
	}
	
	/**Log in with a premade account
	 */
	@Test
	public void baseTest() {
		username.sendKeys("User_1");
		password.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/calendar.html"));

		assertEquals("http://www.lwhawk.com/calendar.html", driver.getCurrentUrl());
	}

	/**Log in with invalid password not allowed
	 */
	@Test
	public void invalidPassword() throws IllegalMonitorStateException, InterruptedException {
		username.sendKeys("User_1");
		password.sendKeys("Pass1!");
		submit.click();
		wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(error, By.cssSelector("p.alert.alert-danger")));
		assertEquals(true, driver.getPageSource().contains("Username and password do not match our record."));
	}

	/**Log in with invalid username not allowed
	 */
	@Test
	public void invalidUser() throws IllegalMonitorStateException, InterruptedException {
		username.sendKeys("not_a_user");
		password.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(error, By.cssSelector("p.alert.alert-danger")));
		assertTrue(driver.getPageSource().contains("Username and password do not match our record."));
	}
	
	/**Log in with invalid password and username not allowed
	 */
	@Test
	public void bothInvalid() throws IllegalMonitorStateException, InterruptedException {
		username.sendKeys("not_a_user");
		password.sendKeys("Pass1!");
		submit.click();
		wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(error, By.cssSelector("p.alert.alert-danger")));
		assertTrue(driver.getPageSource().contains("Username and password do not match our record."));
	}

}