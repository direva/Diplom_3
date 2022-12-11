package pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    //Elements
    private By functionLink(String linkLabel) {
        return By.xpath(".//a[text() = '" + linkLabel + "']");
    }
    private By inputField(String fieldLabel) {
        return By.xpath(".//label[text() = '" + fieldLabel + "']/..//input");
    }
    private By confirmButton(String buttonLabel) {
        return By.xpath(".//button[text() = '" + buttonLabel + "']");
    }
    private By fieldError() {
        return By.cssSelector(".input__error");
    }

    //Functions
    @Step("Клик по ссылке под формой с данными пользователя")
    public void clickFunctionLink(String linkLabel) {
        driver.findElement(functionLink(linkLabel)).click();
    }

    @Step("Заполнение поля в форме с данными пользователя")
    public void setInputValue(String fieldLabel, String fieldValue) {
        driver.findElement(inputField(fieldLabel)).sendKeys(fieldValue);
    }

    @Step("Клик по кнопке 'Войти' или 'Зарегистрироваться'")
    public void clickConfirmButton(String buttonLabel) {
        driver.findElement(confirmButton(buttonLabel)).click();
    }

    @Step("Получение текста сообщения об ошибке")
    public String getErrorText() {
        return driver.findElement(fieldError()).getText();
    }

    @Step("Ожидание появления кнопки 'Войти' или 'Зарегистрироваться'")
    public void waitConfirmButtonVisibility(String buttonLabel) {
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmButton(buttonLabel)));
    }
}
