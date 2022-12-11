package pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilePage {
    private WebDriver driver;

    public ProfilePage(WebDriver driver){
        this.driver = driver;
    }

    //Elements
    private By logoutButton() {
        return By.xpath(".//button[contains(@class, 'Account_button')]");
    }

    //Functions
    @Step("Клик по кнопке 'Выход'")
    public void clickLogout() {
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton()));
        driver.findElement(logoutButton()).click();
    }
}
