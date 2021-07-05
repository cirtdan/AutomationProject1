package SeleniumPractice;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.testng.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CreateAnOrder {

    public static void main(String[] args) throws IOException, ParseException {

        // 1. Launch Chrome browser.
        System.setProperty("webdriver.chrome.driver", "/Users/rafaelaziz/Documents/drivers/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // 2. Navigate to http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx
        driver.get("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx");

        // 3. Login using username Tester and password test
        driver.findElement(By.id("ctl00_MainContent_username")).sendKeys("Tester", Keys.TAB, "test", Keys.ENTER);

        // 4. Click on Order link
        driver.findElement(By.xpath("//a[.='Order']")).click();

        // 5. Enter a random product quantity between 1 and 100
        String randomProductQuantity = "" + (int) (Math.random() * 100);
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtQuantity")).sendKeys(Keys.BACK_SPACE, randomProductQuantity, Keys.ENTER);

        // 6. Click on Calculate and verify that the Total value is correct.
        //Price per unit is 100.  The discount of 8 % is applied to quantities of 10+.
        // So for example, if the quantity is 8, the Total should be 800.
        // If the quantity is 20, the Total should be 1840.
        // If the quantity is 77, the Total should be 7084. And so on.
        String actualValue = driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtTotal")).getAttribute("value");
        String expectedValue = Integer.parseInt(randomProductQuantity) > 10 ?
                "" + (int) (Integer.parseInt(randomProductQuantity) * 0.92 * 100) : "" + (Integer.parseInt(randomProductQuantity) * 100);

        assertEquals(actualValue, expectedValue);

        List<String[]> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/SeleniumPractice/mydata.csv"));

        String line;
        while ((line = bufferedReader.readLine()) != null) {

            list.add(line.split(","));
        }
        int random = (int)(Math.random() * 1000);

        // 6. Generate and enter random first name and last name.
        String name = list.get(random)[1];
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtName")).sendKeys(name);
        // 7. Generate and Enter random street address
        String address = list.get(random)[2];
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox2")).sendKeys(address);
        // 8. Generate and Enter random city
        String city = list.get(random)[3];
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox3")).sendKeys(city);
        // 9. Generate and Enter random state
        String state = list.get(random)[4];
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox4")).sendKeys(state);
        // 10. Generate and Enter a random 5 digit zip code
        String zipCode = list.get(random)[5];
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox5")).sendKeys(zipCode);

        // 11. Select the card type randomly. On each run your script should select a random type.
        List<String> cardTypeList = Arrays.asList("ctl00_MainContent_fmwOrder_cardList_0",
                    "ctl00_MainContent_fmwOrder_cardList_1", "ctl00_MainContent_fmwOrder_cardList_2");
        String randomCardType = cardTypeList.get(new Random().nextInt(cardTypeList.size()));
        driver.findElement(By.id(randomCardType)).click();

        // 12. Generate and enter the random card number:
            //      If Visa is selected, the card number should start with 4.
            //      If MasterCard is selected, card number should start with 5.
            //      If American Express is selected, card number should start with 3.
            //      Card numbers should be 16 digits for Visa and MasterCard, 15 for American Express.

        String visaCard = String.valueOf(4000000000000000L + (long) (Math.random() * 100000000000000L)); // Visa 4
        String mastercard = String.valueOf(5000000000000000L + (long) (Math.random() * 100000000000000L)); // Mastercard 5
        String americanExpress = String.valueOf(300000000000000L + (long) (Math.random() * 10000000000000L)); // American Express 3
        String cardType = "";
        String randomCardNum = "";

        switch (randomCardType) {
            case "ctl00_MainContent_fmwOrder_cardList_0":
                cardType = "Visa";
                randomCardNum = visaCard;
                driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox6")).sendKeys(randomCardNum);
                break;
            case "ctl00_MainContent_fmwOrder_cardList_1":
                cardType = "MasterCard";
                randomCardNum = mastercard;
                driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox6")).sendKeys(randomCardNum);
                break;
            case "ctl00_MainContent_fmwOrder_cardList_2":
                cardType = "American Express";
                randomCardNum = americanExpress;
                driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox6")).sendKeys(randomCardNum);
                break;
        }

        // 13. Enter a valid expiration date (newer than the current date)
        String expDate = "09/22";
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox1")).sendKeys(expDate);

        // 14. Click on Process
        driver.findElement(By.xpath("//a[.='Process']")).click();

        // 15. Verify that “New order has been successfully added” message appeared on the page.
        String HTMLCode = driver.getPageSource();
        assertTrue(HTMLCode.contains("New order has been successfully added."));

        // 16. Click on View All Orders link.
        driver.findElement(By.xpath("//a[.='View all orders']")).click();

        // 17. The placed order details appears on the first row of the orders table.
        // Verify that the entire information contained on the row (Name, Product, Quantity, etc)
        // matches the previously entered information in previous steps.
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String inputDate= formatter.format(date);

        List<String> actualList = new ArrayList<>();
        List<WebElement> trs = driver.findElements(By.tagName("tr"));

        for (WebElement tr : trs) {
            actualList.add(tr.getText());
        }
        List<String> inputList = Arrays.asList(name, "MyMoney", randomProductQuantity, inputDate, address, city,
                state, zipCode, cardType, randomCardNum, expDate);
        String expected = "";
        for (String input : inputList) {
            expected = expected + input + " ";
        }
        assertEquals(actualList.get(2) + " ", expected);

        // 18. Log out of the application.
        driver.findElement(By.id("ctl00_logout")).click();

    }
    }