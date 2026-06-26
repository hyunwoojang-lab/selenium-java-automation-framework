package pageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SideMenuPage {
    private static final Logger logger = LogManager.getLogger(SideMenuPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    private By searchBox = By.cssSelector(".oxd-input.oxd-input--active");
    private static final String SIDE_MENU_XPATH = "//span[contains(@class, 'oxd-main-menu-item--name') and text() = '%s']";

    public SideMenuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickSideMenu(String menuName) {
        if (menuName.equals("Search")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).click();
        } else {
            By menuLocator = By.xpath(String.format(SIDE_MENU_XPATH, menuName));
            wait.until(ExpectedConditions.visibilityOfElementLocated(menuLocator)).click();
        }

    }
}
