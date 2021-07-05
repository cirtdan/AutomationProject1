package SeleniumPractice;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.testng.Assert.*;

public class SeleniumPractice1 {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "/Users/rafaelaziz/Documents/drivers/chromedriver");
        WebDriver driver = new ChromeDriver();

        // 1. Navigate to http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php
        driver.get("http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php");

        // 2. Verify the the title is "Welcome to Duotify!"
        String actualTitle = driver.getTitle();
        assertTrue(actualTitle.contains("Welcome to Duotify!"));

        // 3. Click on Signup here
        driver.findElement(By.id("hideLogin")).click();

        // 4. Fill out the form with the required info
        String s = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPGTSTIVWXYZ";
        String username = "";
        for (int i = 0; i < 8; i++) {
            username += s.charAt((int) (Math.random() * s.length()));
        }

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("firstName")).sendKeys("Mickey");
        driver.findElement(By.id("lastName")).sendKeys("Mouse");

        String email = username + "@gmail.com";

        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("email2")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("Mick3y55");
        driver.findElement(By.id("password2")).sendKeys("Mick3y55" + Keys.ENTER); // 5. Click on Sign up

        // 6. Once logged in to the application, verify that the URL is:
        //http://duotifyapp.us-east-2.elasticbeanstalk.com/browse.php?
        Thread.sleep(2000);
        String actualUrl = driver.getCurrentUrl();
        assertTrue(actualUrl.contains("http://duotifyapp.us-east-2.elasticbeanstalk.com/browse.php?"));

        // 7. In the left navigation bar, verify that your username (first+lastname)
        // is the combination of the same first and last name that you used when signing up.
        assertTrue(driver.getPageSource().contains("Mickey Mouse"));

        // 8. Click on the username on the left navigation bar and verify the username on the main
        // window is correct and then click logout.
        //assertTrue(driver.findElement(By.tagName("h1")).getText().contains("Mickey Mouse"));

        driver.findElement(By.id("nameFirstAndLast")).click();

        Thread.sleep(2000);

        driver.findElement(By.id("rafael")).click(); // logout

        // 9. Verify that you are logged out by verifying the URL is:
        // http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php
        Thread.sleep(2000);
        String actualUrl2 = driver.getCurrentUrl();
        assertTrue(actualUrl2.contains("http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php"));
        Thread.sleep(2000);

        // 10. Login using the same username and password when you signed up.
        driver.findElement(By.id("loginUsername")).sendKeys(username);
        driver.findElement(By.id("loginPassword")).sendKeys("Mick3y55" + Keys.ENTER);

        // 11. Verify successful login by verifying that the home page contains the text
        // "You Might Also Like".
        Thread.sleep(2000);
        System.out.println(driver.getPageSource().contains("You Might Also Like"));

        // 12. Log out once again and verify that you are logged out.

        driver.findElement(By.id("nameFirstAndLast")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("rafael")).click(); //a[.='Contacts'] -> Xpath //p[@class='paragraph-big']
        //p[contains(text(), 'Business Systems Analyst and')]

        driver.quit();




    }
}
