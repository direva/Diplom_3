import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PageRedirectTest {
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
    @DisplayName("Переход в Личный кабинет")
    public void successfulRedirectToProfilePage() {
        HomePage home = new HomePage(driver);
        String startUrl = driver.getCurrentUrl();

        home.clickHeaderButton("Личный Кабинет");
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl, not(equalTo(startUrl)));
        assertThat(currentUrl, containsString("/account"));
    }

    @Test
    @DisplayName("Переход из личного кабинета в Конструктор по клику по кнопке")
    public void successfulRedirectFromProfilePageToConstructorByHeaderButtonClick() {
        HomePage home = new HomePage(driver);

        home.clickHeaderButton("Личный Кабинет");
        String startUrl = driver.getCurrentUrl();
        home.clickHeaderButton("Конструктор");
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl, not(equalTo(startUrl)));
    }

    @Test
    @DisplayName("Переход из личного кабинета в Конструктор по клику по логотипу")
    public void successfulRedirectFromProfilePageToConstructorByLogoClick() {
        HomePage home = new HomePage(driver);

        home.clickHeaderButton("Личный Кабинет");
        String startUrl = driver.getCurrentUrl();
        home.clickLogo();
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl, not(equalTo(startUrl)));
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
