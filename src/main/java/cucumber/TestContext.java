package cucumber;

import managers.CdpNetworkInterceptor;
import managers.PageObjectManager;
import managers.WebDriverManager;

public class TestContext {
    private WebDriverManager webDriverManager;
    private PageObjectManager pageObjectManager;
    private CdpNetworkInterceptor cdpNetworkInterceptor;

    public TestContext() {
        webDriverManager = new WebDriverManager();
        pageObjectManager = new PageObjectManager(webDriverManager.getDriver());
        cdpNetworkInterceptor = new CdpNetworkInterceptor(webDriverManager.getDriver());
    }

    public WebDriverManager getWebDriverManager() {
        return webDriverManager;
    }

    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    public CdpNetworkInterceptor getCdpNetworkInterceptor() {
        return cdpNetworkInterceptor;
    }
}
