import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.HomePage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class ConstructorTest {
    private WebDriver driver;
    private String startIngredient;
    private String validateIngredient;

    public ConstructorTest(String startIngredient, String validateIngredient) {
        this.startIngredient = startIngredient;
        this.validateIngredient = validateIngredient;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                { "Соусы", "Булки" },
                { "Начинки", "Соусы" },
                { "Соусы", "Начинки" },
        };
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
    @DisplayName("Проверка открытия раздела с ингредиентами")
    public void selectIngredient() {
        HomePage home = new HomePage(driver);

        home.clickIngredientHeader(startIngredient);
        home.clickIngredientHeader(validateIngredient);
        assertThat(home.getIngredientHeaderClass(validateIngredient), containsString("tab_tab_type_current"));
    }

    @After
    public void teardown() {
        driver.quit();
    }
}
