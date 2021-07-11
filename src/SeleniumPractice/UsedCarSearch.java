package SeleniumPractice;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.testng.Assert.assertTrue;

public class UsedCarSearch {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "/Users/rafaelaziz/Documents/drivers/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        // 1.	Navigate  to carfax.com
        driver.get("https://www.carfax.com/");

        // 2.	Click on Find a Used Car
        driver.findElement(By.xpath("//a[.='Find a Used Car']")).click();

        // 3.	Verify the page title contains the word “Used Cars”
        Assert.assertTrue(driver.findElement(By.xpath("//li[.='Used Cars']")).getText().contentEquals("Used Cars"));

        // 4.	Choose “Tesla” for  Make.
        Select dropdownBoxMake = new Select(driver.findElement(By.name("make")));
        dropdownBoxMake.selectByValue("Tesla");

        // 5.	Verify that the Select Model dropdown box contains 4 current Tesla models
        // - (Model 3, Model S, Model X, Model Y).
        driver.findElement(By.name("make")).click();
        driver.findElement(By.xpath("//select[@name='make']")).sendKeys("Tesla" + Keys.ENTER);
        List<String> expectedModels = Arrays.asList("Model 3", "Model S", "Model X", "Model Y", "Roadster");
        List<String> actualModels = new ArrayList<>();
        List<WebElement> elements = driver.findElements(By.className("model-option"));
        for (WebElement element : elements) {
            actualModels.add(element.getText().trim());
        }
        Assert.assertEquals(actualModels, expectedModels);

        // 6.	Choose “Model S” for Model.
        Thread.sleep(2000);
        WebElement model = driver.findElement(By.xpath("//select[@name='model']"));
        Select selectModel = new Select(model);
        selectModel.selectByValue("Model S");

        // 7.	Enter the zip code 22182 and click Next
        driver.findElement(By.name("zip")).sendKeys("22182" + Keys.ENTER);

        // 8.	Verify that the page contains the text “Step 2 - Show me cars with”
        assertTrue(driver.getPageSource().contains("Step 2 - Show me cars with"));

        // 9.	Check all 4 checkboxes.
        List<WebElement> checkboxes = driver.findElements(By.xpath("//span[@class='checkbox-list-item--fancyCbx']"));
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
        // 10.	Save the count of results from “Show me X Results” button. In this case it is 10.
        int countOfResults = Integer.parseInt(driver.findElement(By.xpath("//span[@class='totalRecordsText']")).getText());

        // 11.	Click on “Show me x Results” button.
        driver.findElement(By.xpath("//span[@class='totalRecordsText']")).click();

        // 12.	Verify the results count by getting the actual number of results displayed
        // in the page by getting the count of WebElements that represent each result
        int displayNum = Integer.parseInt(driver.findElement(By.xpath("//span[@id='totalResultCount']")).getText().substring(0,2));
        int webElementsNum = driver.findElements(By.xpath("//article[@class='srp-list-item']")).size();
        Assert.assertEquals(displayNum, webElementsNum);

        // 13.	Verify that each result header contains “Tesla Model S”.
        List<WebElement> results = driver.findElements(By.xpath("//article[@class='srp-list-item']"));
        for (WebElement result : results) {
            Assert.assertTrue(result.getText().contains("Tesla Model S"));
        }

        // 14.	Get the price of each result and save them into a List in the order of their appearance.
        // (You can exclude “Call for price” options)
        List<String> priceList = new ArrayList<>();
        List<WebElement> prices = driver.findElements(By.xpath("//span[@class='srp-list-item-price']"));
        for (WebElement price : prices) {
            if(price.getText().contains("$"))
            priceList.add(price.getText().substring(7));
        }
        System.out.println(priceList);

        // 15.	Choose “Price - High to Low” option from the Sort By menu
        WebElement sortClick = driver.findElement(By.xpath("//select[@class='srp-header-sort-select srp-header-sort-select-desktop--srp']"));
        Select selectOption = new Select(sortClick);
        selectOption.selectByIndex(1);

        // 16.	Verify that the results are displayed from high to low price.
        Thread.sleep(2000);
        List<WebElement> prices2 = driver.findElements(By.xpath("//span[@class='srp-list-item-price']"));
        int highestPrice = Integer.parseInt(prices2.get(0).getText().substring(8).replace(",",""));
        int secondHighestPrice = Integer.parseInt(prices2.get(1).getText().substring(8).replace(",",""));
        if (highestPrice > secondHighestPrice){
            System.out.println(true);
        }

        // 17.	Choose “Mileage - Low to High” option from Sort By menu
        WebElement sortClick2 = driver.findElement(By.xpath("//select[@class='srp-header-sort-select srp-header-sort-select-desktop--srp']"));
        Select selectOption2 = new Select(sortClick2);
        selectOption2.selectByIndex(4);

        // 18.	Verify that the results are displayed from low to high mileage.
        List<WebElement> mileage = driver.findElements(By.className("srp-list-item-basic-info-value"));
        String lowestMileage = mileage.get(0).getText().substring(9);
        Assert.assertEquals(lowestMileage, "8,191 miles");


        // 19.	Choose “Year - New to Old” option from Sort By menu
        WebElement sortClick3 = driver.findElement(By.xpath("//select[@class='srp-header-sort-select srp-header-sort-select-desktop--srp']"));
        Select selectOption3 = new Select(sortClick3);
        selectOption3.selectByIndex(5);

        // 20.	Verify that the results are displayed from new to old year.
        Thread.sleep(2000);
        List<WebElement> newToOld = driver.findElements(By.xpath("//h4[@class='srp-list-item-basic-info-model']"));
        int firstCarsYear = Integer.parseInt(newToOld.get(1).getText().substring(0,4));
        int secondCarsYear = Integer.parseInt(newToOld.get(2).getText().substring(0,4));
        if (firstCarsYear > secondCarsYear){
            System.out.println(true);
        }
    }
}

