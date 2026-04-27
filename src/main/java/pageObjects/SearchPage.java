package pageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SearchPage {
    private static final Logger logger = LogManager.getLogger(SearchPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchBox = By.xpath("//*[@data-button-id = 'search_window']");
    private final By searchInput = By.xpath("//input[@data-page-id = '/search']");
    private final By productCards = By.xpath("//div[contains(@class, 'UIStyleComponents')]//div[@class = 'sc-boKDdR cUiskL']//a[1]/span");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void clickTheSearchBox() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        driver.findElement(searchBox).click();
    }

    public void enterSearchKeyword(String keyword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        driver.findElement(searchInput).sendKeys(keyword);
    }

    public void pressEnter() {
        driver.findElement(searchInput).sendKeys(Keys.RETURN);
    }

    public int getProductCardCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.info("상품 카드 없음 (TimeoutException)");
            return 0;
        }
        List<WebElement> cards = driver.findElements(productCards);
        logger.info("검색 결과 상품 카드 수: {}", cards.size());
        return cards.size();
    }

    public List<String> getNoResultText() {
        String searchResultXpath = "//section/span";
        int getResultTextCnt = driver.findElements(By.xpath(searchResultXpath)).size();
        List<String> getResultText = new ArrayList<>();
        for (int i = 0; i < getResultTextCnt; i++) {
            getResultText.add(driver.findElement(By.xpath("(" + searchResultXpath + ")[" + (i + 1) + "]")).getText());
            logger.info(getResultText.get(i));
        }
        return getResultText;
    }

    public String getFirstProductCardText() {
        return driver.findElements(productCards).get(0).getText();
    }
}