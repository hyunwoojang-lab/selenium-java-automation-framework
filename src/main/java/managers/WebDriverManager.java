package managers;

import enums.DriverType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverManager {
    private static final Logger logger = LogManager.getLogger(WebDriverManager.class);
    private WebDriver driver;

    public WebDriverManager() {}

    public WebDriver getDriver() {
        return (driver == null) ? driver = createDriver() : driver;
    }

    public WebDriver createDriver() {
        DriverType driverType = FileReaderManager.getInstance().getConfigReader().getBrowser();
        logger.info("브라우저 생성: {}", driverType);
        switch (driverType) {
            case FIREFOX: return new FirefoxDriver();
            case EDGE:    return new EdgeDriver();
            default:      return new ChromeDriver();
        }
    }

    public void closeDriver() {
        if (driver != null) {
            try {
                driver.close();
                driver.quit();
            } catch (Exception e) {
//                logger.warn("Error closing driver: " + e.getMessage());
                System.out.println("Error closing driver: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
}
