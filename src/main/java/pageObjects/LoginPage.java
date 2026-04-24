package pageObjects;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class LoginPage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    private By headerLogin = By.className("_gnb__login--button_5pcry_214");
    private By emailField = By.xpath("//*[@type = 'text']");
    private By passwordField = By.xpath("//*[@type = 'password']");
    private By loginButton = By.xpath("//*[@data-button-id = 'login_login']");
    private By successMessage = By.className("_gnb__login_5pcry_207");
    private By errorMessage = By.className("error-msg");
    private By wrongPasswordAlertText = By.className("login-v2-input__validation");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void clickHeaderLoginMenu() {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(headerLogin));
        field.click();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(headerLogin)).click();
    }

    public void enterEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public String getLoginResultText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getAttribute("aria-label");
        } catch (Exception e) {
            return driver.findElement(errorMessage).getAttribute("aria-label");
        }
    }

    public String getAlertMessage() {
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();
        String message = alert.getText();

        // Alert 의 OK button 클릭
        alert.accept();

        return message;
    }

    public String getAlertForWrongPassword() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(wrongPasswordAlertText));

        int alertCount = driver.findElements(wrongPasswordAlertText).size();
        logger.info("Alart Message Count : {}", alertCount);

        if (alertCount == 1) {
            return driver.findElement(wrongPasswordAlertText).getText();
        } else if (alertCount > 1) {
            String baseXpath = "//*[contains(@class,'login-v2-input__validation')]";
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= alertCount; i++) {
                String text = driver.findElement(By.xpath("(" + baseXpath + ")[" + i + "]")).getText();
                result.append(text);
                if (i < alertCount) result.append("\n");
            }
            return result.toString();
        }
        return "";
    }
}
