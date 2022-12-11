import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.HomePage;
import pageObjects.LoginPage;

public class LoginTest {
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
    }

    @Test
    @DisplayName("Успешный логин с главной страницы")
    public void successfulLoginFromHomePage() {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.clickLoginButton();
        login.setInputValue("Email", user.getEmail());
        login.setInputValue("Пароль", user.getPassword());
        login.clickConfirmButton("Войти");

        home.waitCheckoutButtonVisibility();
    }

    @Test
    @DisplayName("Успешный логин из Личного кабинета")
    public void successfulLoginFromProfilePage() {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.clickHeaderButton("Личный Кабинет");
        login.setInputValue("Email", user.getEmail());
        login.setInputValue("Пароль", user.getPassword());
        login.clickConfirmButton("Войти");

        home.waitCheckoutButtonVisibility();
    }

    @Test
    @DisplayName("Успешный логин со страницы регистрации")
    public void successfulLoginFromRegisterPage() {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.clickHeaderButton("Личный Кабинет");
        login.clickFunctionLink("Зарегистрироваться");
        login.clickFunctionLink("Войти");
        login.setInputValue("Email", user.getEmail());
        login.setInputValue("Пароль", user.getPassword());
        login.clickConfirmButton("Войти");

        home.waitCheckoutButtonVisibility();
    }

    @Test
    @DisplayName("Успешный логин со страницы сброса пароля")
    public void successfulLoginFromPasswordResetPage() {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.clickHeaderButton("Личный Кабинет");
        login.clickFunctionLink("Восстановить пароль");
        login.clickFunctionLink("Войти");
        login.setInputValue("Email", user.getEmail());
        login.setInputValue("Пароль", user.getPassword());
        login.clickConfirmButton("Войти");

        home.waitCheckoutButtonVisibility();
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
