package stepDefinitions;

import cucumber.TestContext;
import io.cucumber.java.en.*;
import managers.CdpNetworkInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class NetworkInterceptSteps {
    private static final Logger logger = LogManager.getLogger(NetworkInterceptSteps.class);

    TestContext testContext;
    WebDriver driver;
    CdpNetworkInterceptor interceptor;

    private long startTime;
    private long elapsedTime;

    public NetworkInterceptSteps(TestContext context) {
        testContext = context;
        driver = testContext.getWebDriverManager().getDriver();
        interceptor = testContext.getCdpNetworkInterceptor();
    }

    @Given("CDP interceptor is set up for {string} with status {int} and body {string}")
    public void cdp_interceptor_is_set_up(String urlPattern, int statusCode, String body) {
        interceptor.addMockResponse(urlPattern, statusCode, "application/json", body);
        interceptor.enable();
        logger.info("인터셉터 설정 완료 - 패턴: {}", urlPattern);
    }

    @Given("Browser navigates to {string}")
    public void browser_navigates_to(String url) {
        logger.info("페이지 이동 시작: {}", url);
        startTime = System.currentTimeMillis();
        driver.get(url);
        elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("페이지 이동 완료 - 소요시간: {}ms", elapsedTime);
    }

//    @When("Browser navigates to {string}")
//    public void browser_navigates_to_when(String url) {
//        browser_navigates_to(url);
//    }

    @Then("Response time is more than {int} milliseconds")
    public void response_time_is_more_than(int ms) {
        logger.info("응답 시간 검증 ({}ms 초과 예상): 실제 {}ms", ms, elapsedTime);
        assertTrue("응답 시간이 " + ms + "ms 보다 커야 합니다. 실제: " + elapsedTime + "ms", elapsedTime > ms);
    }

    @Then("Response time is less than {int} milliseconds")
    public void response_time_is_less_than(int ms) {
        logger.info("응답 시간 검증 ({}ms 미만 예상): 실제 {}ms", ms, elapsedTime);
        assertTrue("인터셉트 후 응답 시간이 " + ms + "ms 미만이어야 합니다. 실제: " + elapsedTime + "ms", elapsedTime < ms);
    }

    @Then("Page source contains {string}")
    public void page_source_contains(String expected) {
        String pageSource = driver.getPageSource();
        logger.info("페이지 소스 검증 - 기대값: {}", expected);
        assertTrue("페이지 소스에 '" + expected + "' 가 포함되어야 합니다.", pageSource.contains(expected));
    }

    @Then("CDP interceptor is disabled")
    public void cdp_interceptor_is_disabled() {
        interceptor.disable();
        logger.info("인터셉터 비활성화 완료");
    }
}