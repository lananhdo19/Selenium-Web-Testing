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

public class SignOutTest {
	private WebDriver driver;
	private String url = "http://www.lwhawk.com/home.html";
	private WebElement username;
	private WebElement password;
	private WebElement submit;
	private WebDriverWait wait;

	@AfterEach
	public void teardown() {
		driver.quit(); // close the browser
	}
	
	/**Log in before every test
	 */
	@BeforeEach
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:/Software Testing/Driver/chromedriver.exe"); // specify path																							// driver
		driver = new ChromeDriver(); // create an instance of the web browser and open it
		driver.get(url); // open the given url
		username = driver.findElement(By.id("login-username"));
		password = driver.findElement(By.id("login-password"));
		submit = driver.findElement(By.className("btn-primary"));
		wait = new WebDriverWait(driver, 10);
		username.sendKeys("User_1");
		password.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/calendar.html"));
	}
	
	/**Sign out and assure redirects to login page
	 */
	@Test
	public void signOutTest() {
		assertEquals("http://www.lwhawk.com/calendar.html", driver.getCurrentUrl());
		driver.findElement(By.linkText("Sign Out")).click();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/home.html"));
		assertEquals(driver.getTitle(), "Schedule It!");
	}

}
