package managers;

import org.openqa.selenium.WebDriver;
import pageObjects.LoginPage;

public class PageObjectManager {
    private WebDriver driver;

    private LoginPage loginPage;
//    private HomePage homePage;
//    private SearchPage searchPage;
//    private ProductDetailPage productDetailPage;
//    private CartPage cartPage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        return (loginPage == null) ? loginPage = new LoginPage(driver) : loginPage;
    }

//    public HomePage getHomePage() {
//        return (homePage == null) ? homePage = new HomePage(driver) : homePage;
//    }
//
//    public SearchPage getSearchPage() {
//        return (searchPage == null) ? searchPage = new SearchPage(driver) : searchPage;
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
