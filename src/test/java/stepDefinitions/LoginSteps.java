package stepDefinitions;

import cucumber.TestContext;
import io.cucumber.java.en.*;
import managers.FileReaderManager;
import org.openqa.selenium.WebDriver;
import pageObjects.LoginPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertEquals;

public class LoginSteps {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);
    private LoginPage loginPage;

    TestContext testContext;

    //TestContext 활용을 위해 생성자에서 각각 주입
    public LoginSteps(TestContext context) {
        testContext = context;
        loginPage = testContext.getPageObjectManager().getLoginPage();
        //Hooks 생성으로 불필요
//        driver = testContext.getWebDriverManager().getDriver();
    }

    // PageObjectManager 설정을 위한 필드 선언
//    PageObjectManager pageObjectManager;

    // WebDriverManager 설정을 위한 필드 선언
//    WebDriverManager webDriverManager;

    // Hooks 으로 Before 이동
//    @Before
//    public void setup() {
//        // 1. WebDriverManager 생성 전
////        driver = new ChromeDriver();
//
//        // 2. PageObjectManager 생성전
////        loginPage = new LoginPage(driver);
//
//        // WebDriverManager 생성 시에만 TestContext 적용시 필요없음
////        webDriverManager = new WebDriverManager();
////        driver = webDriverManager.getDriver();
//
//        // PageObjectManager 설정 시에만 TestContext 적용 시 필요없음
////        pageObjectManager = new PageObjectManager(driver);
////        loginPage = pageObjectManager.getLoginPage();
//
//
//        // driver 최대화
////        driver.manage().window().maximize();
//
//    }

    @Given("User connect to {string}")
    public void user_connect_to_url(String Url) {
        loginPage.navigateTo(Url);
    }

    @Given("User connect to the base url")
    public void user_connect_to_base_url() {
        String url = FileReaderManager.getInstance().getConfigReader().getUrl();
        loginPage.navigateTo(url);
    }

    @When("User click the login header menu")
    public void user_click_the_login_header_menu() {
        loginPage.clickHeaderLoginMenu();
        logger.info("헤더 메뉴의 로그인 버튼 클릭");
    }

    @When("User enter {string} and {string}")
    public void user_enter_userid_and_pw(String id, String pw) {
        loginPage.enterEmail(id);
        loginPage.enterPassword(pw);
    }

    @When("User click the login button")
    public void user_click_the_login_button() {
        loginPage.clickLogin();
    }

    @Then("User can see that the login was successful")
    public void user_can_see_that_the_login_was_successful() {
        String loginSuccessText = loginPage.getLoginResultText();
        assertEquals("로그아웃", loginSuccessText);
    }

    @Then("User can see the {string} message displayed in the browser popup")
    public void user_can_see_the_message_displayed_in_the_browser_popup(String message) {
        String actualMessage = loginPage.getAlertMessage();
        assertEquals(message, actualMessage);
    }

    @Then("User can see the {string} message displayed in the bottom of password input field")
    public void user_can_see_the_message_displayed_in_the_bottom_of_password_input_field(String message) {
        String actualMessage = loginPage.getAlertForWrongPassword();
        assertEquals(message, actualMessage);
    }

    @Then("User can see the {string} message displayed in the bottom of {string} input field")
    public void user_can_see_the_message_displayed_in_the_bottom_of_input_field(String message, String fieldName) {
        String actualMessage = loginPage.getAlertForWrongPassword();
        if (actualMessage.contains("\n")) {
            String[] parts = actualMessage.split("\n");
            if (fieldName.equals("login")) {
                assertEquals(message, parts[0]);
            } else if (fieldName.equals("password")) {
                assertEquals(message, parts[1]);
            }
        } else {
            assertEquals(message, actualMessage);
        }
    }


    // Hooks 추가하여 After 제거
//    @After
//    public void tearDown() {
//        if (driver != null) driver.quit();
//    }



}
