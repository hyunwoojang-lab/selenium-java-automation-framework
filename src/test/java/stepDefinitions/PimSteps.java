package stepDefinitions;

import cucumber.TestContext;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pageObjects.LoginPage;
import pageObjects.PimPage;
import pageObjects.SideMenuPage;

import static org.junit.Assert.assertEquals;

public class PimSteps {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);
    private LoginPage loginPage;
    private PimPage pimPage;
    private SideMenuPage sideMenuPage;

    TestContext testContext;

    public PimSteps(TestContext context) {
        testContext = context;
        loginPage = testContext.getPageObjectManager().getLoginPage();
        pimPage = testContext.getPageObjectManager().getPimPage();
        sideMenuPage = testContext.getPageObjectManager().getSideMenuPage();
        //Hooks 생성으로 불필요
//        driver = testContext.getWebDriverManager().getDriver();
    }

    @When("User click the {string} side menu")
    public void user_click_the_side_menu(String menuName) throws InterruptedException {
        Thread.sleep(3000);
        sideMenuPage.clickSideMenu(menuName);
        Thread.sleep(100);
    }

    @When("User click the Add button")
    public void user_click_the_add_button() throws InterruptedException {
        pimPage.click_add_button();
        Thread.sleep(100);
    }

    @When("User select the {string} input field and enter {string}")
    public void user_select_the_input_field_and_enter(String inputField, String inputName) throws InterruptedException {
        pimPage.click_input_nameField(inputField, inputName);

    }

    @When("User click create user toggle")
    public void user_click_create_user_toggle() {
        pimPage.clickCreateUserToggle();
    }

    @When("User select the {string} field and enter {string}")
    public void user_select_the_field_and_enter(String fieldName, String value) throws InterruptedException {
        pimPage.clickAndEnterUserInfo(fieldName, value);
    }

    @When("User click the {string} button in the pim page")
    public void user_click_the_button_in_the_pim_page(String buttonName) {
        pimPage.clickTheSaveAndCancelButton(buttonName);
    }

    @Then("User can see {string} message")
    public void user_can_see_message(String expectedMsg) throws InterruptedException {
        String getMsg = pimPage.checkTheToastMessage();
        assertEquals(expectedMsg, getMsg);
        Thread.sleep(100);
    }

    @When("User click the trash button of the {string} user")
    public void user_click_the_trash_button_of_the_user(String userName) throws InterruptedException {
        pimPage.clickUserTrash(userName);
    }
}
