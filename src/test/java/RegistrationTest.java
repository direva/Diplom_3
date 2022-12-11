import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class RegistrationTest {
    private WebDriver driver;
    User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
    private static String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get("https://stellarburgers.nomoreparties.site/");
    }

    @Test
    @DisplayName("Успешная регистрация")
    public void validateSuccessfulRegistration() throws InterruptedException {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.clickHeaderButton("Личный Кабинет");
        login.clickFunctionLink("Зарегистрироваться");
        login.setInputValue("Email", user.getEmail());
        login.setInputValue("Имя", user.getName());
        login.setInputValue("Пароль", user.getPassword());
        login.clickConfirmButton("Зарегистрироваться");
        Thread.sleep(1000);

        Response response = Requests.sendLoginRequest(user);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        accessToken = response.body().as(LoggedInUser.class).getAccessToken();
    }

    @Test
    @DisplayName("Проверка ошибки при вводе короткого пароля")
    public void validateRegistrationWithPasswordError() {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.clickHeaderButton("Личный Кабинет");
        login.clickFunctionLink("Зарегистрироваться");
        login.setInputValue("Email", user.getEmail());
        login.setInputValue("Имя", user.getName());
        login.setInputValue("Пароль", "test");
        login.clickConfirmButton("Зарегистрироваться");

        assertThat(login.getErrorText(), equalTo("Некорректный пароль"));
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @AfterClass
    public static void removeUser() {
        Requests.sendDeleteRequest(accessToken);
    }
}
