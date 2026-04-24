package stepDefinitions;

import cucumber.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import managers.FileReaderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {
    TestContext testContext;
    private static final Logger logger = LogManager.getLogger(Hooks.class);

    public Hooks(TestContext context) {
        testContext = context;
    }

    @Before
    public void beforeSteps() {
        logger.info("=== 테스트 시작 ===");
        if (FileReaderManager.getInstance().getConfigReader().isWindowMaximize()) {
            testContext.getWebDriverManager().getDriver().manage().window().maximize();
        }
    }

    @After
    public void afterSteps() {
        logger.info("=== 테스트 종료 ===");
        testContext.getWebDriverManager().closeDriver();
    }

    //TestContext 사용하지 않고 직접 webDriverManager 를 호출하여 사요
//    private WebDriverManager webDriverManager;
//    private WebDriver driver;
//
//
//    public Hooks() {
//        webDriverManager = new WebDriverManager();
//        driver = webDriverManager.getDriver();
//    }
//
//
//    @Before
//    public void before() {
//
////        driver.manage().window().maximize();
//    }
//
//
//    @After
//    public void after() {
//        webDriverManager.closeDriver();
//    }


}
