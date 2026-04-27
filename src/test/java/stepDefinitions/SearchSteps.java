package stepDefinitions;

import cucumber.TestContext;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pageObjects.SearchPage;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchSteps {
    private static final Logger logger = LogManager.getLogger(SearchSteps.class);
    private final SearchPage searchPage;

    public SearchSteps(TestContext context) {
        searchPage = context.getPageObjectManager().getSearchPage();
    }

    @When("User click the search box")
    public void user_click_the_search_box() {
        searchPage.clickTheSearchBox();
        logger.info("검색창 클릭");
    }

    @When("User enter search keyword {string} and press enter")
    public void user_enter_search_keyword_and_press_enter(String keyword) {
        searchPage.enterSearchKeyword(keyword);
        searchPage.pressEnter();
        logger.info("검색 키워드 입력 및 엔터: {}", keyword);
    }

    @Then("User can see product cards displayed in search results")
    public void user_can_see_product_cards_displayed_in_search_results() {
        int count = searchPage.getProductCardCount();
        logger.info("상품 카드 수: {}", count);
        assertTrue("검색 결과에 상품 카드가 1개 이상 표시되어야 합니다.", count > 0);
    }

    @Then("User can see {string} in the first product card title")
    public void user_can_see_keyword_in_first_product_card_title(String keyword) {
        String title = searchPage.getFirstProductCardText();
        assertTrue(title.contains(keyword));
    }

    @Then("User can see no product cards displayed in search results")
    public void user_can_see_no_product_cards_displayed_in_search_results() {
        int count = searchPage.getProductCardCount();
        logger.info("검색 결과 상품 카드 수: {}", count);
        assertTrue("검색 결과에 상품 카드가 표시되지 않아야 합니다.", count == 0);
    }

    @Then("User can see {string} and {string} messages in search results")
    public void user_can_see_no_result_messages_in_search_results(String firstMessage, String secondMessage) {
        List<String> messages = searchPage.getNoResultText();
        logger.info("검색 결과 없음 메시지: {}", messages);
        assertEquals(firstMessage, messages.get(0));
        assertEquals(secondMessage, messages.get(1));
    }

}