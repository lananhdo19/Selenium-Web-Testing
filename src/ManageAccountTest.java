import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class ManageAccountTest {
	private WebDriver driver;
	   private String url = "http://www.lwhawk.com/home.html";
	   private WebElement username;
	   private WebElement password;
	   private WebElement submit;
	   private WebElement error;
	   private WebDriverWait wait;
	   private WebElement changePassswordButton;
	   private WebElement currentPass;
	   private WebElement newPass;
	   private WebElement confirmPass;
	   private WebElement saveChanges;
	   private WebElement delete;
	   
	/**Log in with the pre-made stub account
	 * go to manage account page
	 * click change password before every test
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
		username.sendKeys("d");
		password.sendKeys("12345678");
		submit.click();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/calendar.html"));
		driver.findElement(By.partialLinkText("Account")).click();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/account.html"));
		changePassswordButton = driver.findElement(By.cssSelector("button.btn.btn-primary"));
		changePassswordButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		driver.switchTo().activeElement();
		currentPass = driver.findElement(By.xpath("//*[@id=\"currentPassword\"]"));
		newPass = driver.findElement(By.xpath("//*[@id=\"newPassword\"]"));
		confirmPass = driver.findElement(By.xpath("//*[@id=\"newPasswordConfirm\"]"));
		saveChanges = driver.findElement(By.xpath("//*[@id=\"saveBtn\"]"));
		delete = driver.findElement(By.xpath("/html/body/div[2]/div[2]/ul/li[4]/div/div[2]/button"));
	}
	
	@AfterEach
	public void teardown() {
		driver.quit(); // close the browser
	}
	
	/**Change the password to qwertyui then assert it has been changed
	 * changes the password back to 12345678 
	 * reassert that password has been reset
	 */
	@Test
	public void baseTest() throws InterruptedException {
		currentPass.sendKeys("12345678");
		newPass.sendKeys("qwertyui");
		confirmPass.sendKeys("qwertyui");
		saveChanges.click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		assertTrue(driver.getPageSource().contains("Your password has successfully been changed."));
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		driver.switchTo().activeElement();
		changePassswordButton.click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		driver.switchTo().activeElement();
		
		currentPass.sendKeys("qwertyui");
		newPass.sendKeys("12345678");
		confirmPass.clear();
		confirmPass.sendKeys("12345678");
		saveChanges.click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		
		assertTrue(driver.getPageSource().contains("Your password has successfully been changed."));
		
	}
	
	/**Incorrect current password results in not changing the password
	 */
	@Test
	public void invalidCurrentPass() {
		currentPass.sendKeys("Not Pass");
		newPass.sendKeys("qwertyui");
		confirmPass.sendKeys("qwertyui");
		saveChanges.click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		assertTrue(driver.getPageSource().contains("The password you entered doesn't match our records."));
		
	}
	
	/**When new password and confirm password do not match
	 * user should not be able to click save changes button
	 */
	@Test
	public void nonmatchingNewPass() {
		currentPass.sendKeys("12345678");
		newPass.sendKeys("qwertyui");
		confirmPass.sendKeys("1");
		assertFalse(saveChanges.isEnabled());
	}
	
	
	/**Cancels the change password modal and deletes account
	 * asserts redirect to login page
	 * recreates the mock account again
	 */
	@Test
	public void deleteAccount() {
		driver.findElement(By.xpath("//*[@id=\"passwordForm\"]/div[4]/div/div[1]")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		driver.switchTo().activeElement();
		delete.click();
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/home.html"));
		assertEquals("http://www.lwhawk.com/home.html", driver.getCurrentUrl());
		
		driver.findElement(By.linkText("Sign up")).click();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/signup.html"));
		driver.findElement(By.id("fname")).sendKeys("b");
		driver.findElement(By.id("lname")).sendKeys("b");
		driver.findElement(By.id("username")).sendKeys("d");
		driver.findElement(By.id("password")).sendKeys("12345678");
		driver.findElement(By.id("confirmPassword")).sendKeys("12345678");
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		wait.until(ExpectedConditions.urlToBe("http://www.lwhawk.com/home.html"));
		assertEquals("http://www.lwhawk.com/home.html", driver.getCurrentUrl());

	}
	
	/**Change password to less than 8 characters should not be allowed as all users must have password >= 8
	 */
	@Test
	public void lessThan8CharNewPass() {
		currentPass.sendKeys("12345678");
		newPass.sendKeys("1");
		confirmPass.sendKeys("1");
		saveChanges.click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		boolean passChanged = driver.getPageSource().contains("Your password has successfully been changed.");
	
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		driver.switchTo().activeElement();
		changePassswordButton.click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		driver.switchTo().activeElement();
		
		currentPass.sendKeys("1");
		newPass.sendKeys("12345678");
		confirmPass.clear();
		confirmPass.sendKeys("12345678");
		saveChanges.click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"passwordForm\"]/div[1]/label")));
		
		assertTrue(driver.getPageSource().contains("Your password has successfully been changed."));
		assertFalse(passChanged, "Allowed password to be changed with a less than 8 character password.");
	}
	
	
	

}
