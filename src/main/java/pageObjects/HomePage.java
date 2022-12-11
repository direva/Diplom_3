package pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
    }

    //Elements
    private By headerButton(String buttonLabel) {
        return By.xpath(".//p[text() = '" + buttonLabel + "']");
    }
    private By loginButton() {
        return By.xpath(".//button[text() = 'Войти в аккаунт']");
    }
    private By checkoutButton() {
        return By.xpath(".//button[text() = 'Оформить заказ']");
    }
    private By logo() {
        return By.cssSelector(".AppHeader_header__logo__2D0X2");
    }
    private By ingredientHeader(String ingredientLabel) {
        return By.xpath(".//span[text() = '" + ingredientLabel + "']/..");
    }

    //Functions
    @Step("Клик по кнопке в навигации")
    public void clickHeaderButton(String buttonLabel) {
        driver.findElement(headerButton(buttonLabel)).click();
    }

    @Step("Клик по кнопке логина")
    public void clickLoginButton() {
        driver.findElement(loginButton()).click();
    }

    @Step("Ожидание появления кнопки оформления заказа")
    public void waitCheckoutButtonVisibility() {
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton()));
    }

    @Step("Клик по логотипу")
    public void clickLogo() {
        driver.findElement(logo()).click();
    }

    @Step("Клик по разделу ингредиентов")
    public void clickIngredientHeader(String ingredientLabel) {
        driver.findElement(ingredientHeader(ingredientLabel)).click();
    }

    @Step("Получение класса заголовка раздела ингредиентов")
    public String getIngredientHeaderClass(String ingredientLabel) {
        return driver.findElement(ingredientHeader(ingredientLabel)).getAttribute("class");
    }
}