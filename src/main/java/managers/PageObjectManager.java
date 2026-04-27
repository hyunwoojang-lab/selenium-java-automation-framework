package managers;

import org.openqa.selenium.WebDriver;
import pageObjects.LoginPage;
import pageObjects.SearchPage;

public class PageObjectManager {
    private final WebDriver driver;

    private LoginPage loginPage;
    private SearchPage searchPage;
//    private HomePage homePage;
//    private ProductDetailPage productDetailPage;
//    private CartPage cartPage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        return (loginPage == null) ? loginPage = new LoginPage(driver) : loginPage;
    }

    public SearchPage getSearchPage() {
        return (searchPage == null) ? searchPage = new SearchPage(driver) : searchPage;
    }

//    public HomePage getHomePage() {
//        return (homePage == null) ? homePage = new HomePage(driver) : homePage;
//    }
//
//    public ProductDetailPage getProductDetailPage() {
//        return (productDetailPage == null) ? productDetailPage = new ProductDetailPage(driver) : productDetailPage;
//    }
//
//    public CartPage getCartPage() {
//        return (cartPage == null) ? cartPage = new CartPage(driver) : cartPage;
//    }
}
