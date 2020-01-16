import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignUpTest {

	private WebDriver driver;
	private String url = "https://lwhawk.com/signup.html";
	private WebElement firstName;
	private WebElement lastName;
	private WebElement username;
	private WebElement password;
	private WebElement passwordConfirm;
	private WebElement showPassword;
	private WebElement submit;
	private WebDriverWait wait;

	private String charactersList = "asdfghjkl123456789";
	private String uniqueUsername = "";

	@AfterEach
	public void teardown() {
		driver.quit(); // close the browser
	}
	
	/**Directs to sign up page
	 */
	@BeforeEach
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:/Software Testing/Driver/chromedriver.exe"); // specify path
																										// the the																						// driver
		driver = new ChromeDriver(); // create an instance of the web browser and open it
		driver.get(url); // open the given url
		firstName = driver.findElement(By.id("fname"));
		lastName = driver.findElement(By.id("lname"));
		username = driver.findElement(By.id("username"));
		password = driver.findElement(By.id("password"));
		showPassword = driver.findElement(By.id("showPassword"));
		passwordConfirm = driver.findElement(By.id("confirmPassword"));
		submit = driver.findElement(By.cssSelector("button.btn.btn-primary"));
		wait = new WebDriverWait(driver, 10);

		uniqueUsername = "";
		for (int i = 0; i < 8; i++) {
			int j = (int) (Math.random() * charactersList.length());
			uniqueUsername += charactersList.charAt(j);
		}
	}
	
	/**Sign up to create account and redirects to login
	 */
	@Test
	public void baseTest() {
		firstName.sendKeys("Lan_Anh7!");
		lastName.sendKeys("Do7!");
		username.sendKeys(uniqueUsername);
		password.sendKeys("Pass123!");
		passwordConfirm.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.urlToBe("https://lwhawk.com/home.html"));
		assertEquals("https://lwhawk.com/home.html", driver.getCurrentUrl());
	}
	
	/**Sign up with no first name not allowed
	 */
	@Test
	public void noFirstName() {
		lastName.sendKeys("Do7!");
		username.sendKeys(uniqueUsername);
		password.sendKeys("Pass123!");
		passwordConfirm.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.urlToBe("https://lwhawk.com/home.html"));
		assertEquals("https://lwhawk.com/signup.html", driver.getCurrentUrl(), "Allowed sign up with no first name.");

	}

	/**Sign up with no last name not allowed
	 */
	@Test
	public void noLastName() {
		firstName.sendKeys("Lan_Anh7!");
		username.sendKeys(uniqueUsername);
		password.sendKeys("Pass123!");
		passwordConfirm.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.urlToBe("https://lwhawk.com/home.html"));
		assertEquals("https://lwhawk.com/signup.html", driver.getCurrentUrl(), "Allowed Sign Up with no last name.");
	}
	
	/**Sign up with preexisting mock username should not be allowed
	 */
	@Test
	public void preexistingUser() {
		firstName.sendKeys("Lan_Anh7!");
		lastName.sendKeys("Do7!");
		username.sendKeys("LanAnhDo7!9");
		password.sendKeys("Pass123!");
		passwordConfirm.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.alert.alert-danger")));
		assertTrue(driver.getPageSource().contains("Username already exists"));
	}

	/**Sign up with password less than 8 characters should not be allowed
	 */
	@Test
	public void less8CharPassword() {
		firstName.sendKeys("Lan_Anh7!");
		lastName.sendKeys("Do7!");
		username.sendKeys(uniqueUsername);
		password.sendKeys("Pass!");
		passwordConfirm.sendKeys("Pass123!");
		submit.click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.alert.alert-danger")));
		assertTrue(driver.getPageSource().contains("Password should be longer than 8 characters!"));
	}
	
	/**Sign up with password and confirm password not matching not be allowed
	 */
	@Test
	public void notMatchingConfirmPass() {
		firstName.sendKeys("Lan_Anh7!");
		lastName.sendKeys("Do7!");
		username.sendKeys(uniqueUsername);
		password.sendKeys("Pass123!");
		passwordConfirm.sendKeys("Pass!");
		showPassword.click();
		submit.click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.alert.alert-danger")));
		assertTrue(driver.getPageSource().contains("Passwords don't match!"));
	}
}
