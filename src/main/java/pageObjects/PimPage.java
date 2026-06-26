package pageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PimPage {
    private static final Logger logger = LogManager.getLogger(PimPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    private By addButtonXpath = By.xpath("//div[@class='orangehrm-paper-container']//button[contains(@class, 'oxd-button--secondary')]");
    private static final String inputNameXpath = "//input[@placeholder = '%s']";
    private static final String createToggle = "//span[@class = 'oxd-switch-input oxd-switch-input--active --label-right']";
    private static final String createUserinfoXpath = "//div[normalize-space() = '%s']//input";
    private static final String buttonXpath = "//button[normalize-space() = '%s']";
    private static final String toastMessageXpath = "//*[contains(@class, 'oxd-text--toast-message')]";
    private static final String userTrashBtnXpath = "//div[normalize-space() = '%s']/parent::div//*[@class = 'oxd-icon bi-trash']";

    public PimPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void click_add_button() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addButtonXpath)).click();
    }

    public void click_input_nameField(String nameField, String inputValue) throws InterruptedException {
        Thread.sleep(1000);
        By inputLocator = By.xpath(String.format(inputNameXpath, nameField));
        driver.findElement(inputLocator).click();
        Thread.sleep(500);
        driver.findElement(inputLocator).sendKeys(inputValue);
        Thread.sleep(500);

    }

    public void clickCreateUserToggle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(createToggle))).click();
    }

    public void clickAndEnterUserInfo(String inputField, String inputValue) throws InterruptedException {
        By inputBox = By.xpath(String.format(createUserinfoXpath, inputField));
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputBox)).click();
//        driver.findElement(inputBox).click();
        Thread.sleep(500);
        driver.findElement(inputBox).sendKeys(inputValue);
        Thread.sleep(500);
    }

    public void changeEmployeeId(String inputFiled, String inputValue) throws InterruptedException {
        By employeeIdLocator = By.xpath(String.format(createUserinfoXpath, inputFiled));
        driver.findElement(employeeIdLocator).click();
        Thread.sleep(500);

        Thread.sleep(500);

    }

    public void clickTheSaveAndCancelButton(String buttonName) {
        By buttonLocator = By.xpath(String.format(buttonXpath, buttonName));
        wait.until(ExpectedConditions.visibilityOfElementLocated(buttonLocator)).click();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String checkTheToastMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(toastMessageXpath))).getText();

    }

    public void clickUserTrash(String userName) throws InterruptedException {
        Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(userTrashBtnXpath, userName)))).click();
    }


}
