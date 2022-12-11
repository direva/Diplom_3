import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.ProfilePage;

public class LogoutTest {
    private WebDriver driver;
    private static User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
    private static String accessToken;

    @BeforeClass
    public static void createUser() throws InterruptedException {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Requests.sendRegisterRequest(user);
        Thread.sleep(1000);
        accessToken = Requests.sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get("https://stellarburgers.nomoreparties.site/");

        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.clickLoginButton();
        login.setInputValue("Email", user.getEmail());
        login.setInputValue("Пароль", user.getPassword());
        login.clickConfirmButton("Войти");
    }

    @Test
    @DisplayName("Успешный выход из аккаунта")
    public void successfulLogout() {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);
        ProfilePage profile = new ProfilePage(driver);

        home.clickHeaderButton("Личный Кабинет");
        profile.clickLogout();
        login.waitConfirmButtonVisibility("Войти");
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
