import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;

public class AllureTests {

    @BeforeAll
    static void setup() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        Configuration.browserCapabilities = options;
    }

    @Test
    @DisplayName("Чистый Selenide (с Listener)")
    public void testIssueSearchWithListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open("https://github.com");

        $("[data-target='qbsearch-input.inputButton']").click();
        $("#query-builder-test").sendKeys("Margolog/qa_guru_9");
        $("#query-builder-test").pressEnter();

        $(linkText("Margolog/qa_guru_9")).click();
        $("#issues-tab").click();
        $(withText("Issues")).should(Condition.exist);
    }

    @Test
    @DisplayName("Лямбда шаги через step (name, () -> {})")
    public void testIssueSearchWithSteps() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        step("Открываем главную страницу", () -> {
            open("https://github.com");
        });

        step("Ищем репозиторий", () -> {
            $("[data-target='qbsearch-input.inputButton']").click();
            $("#query-builder-test").sendKeys("Margolog/qa_guru_9");
            $("#query-builder-test").pressEnter();
        });

        step("Кликаем на нужный репозиторий", () -> {
            $(linkText("Margolog/qa_guru_9")).click();
            $("#issues-tab").click();
        });

        step("Проверяем наличие Issues", () -> {
            $(withText("Issues")).should(Condition.exist);
        });
    }

    @Test
    @DisplayName("Шаги с аннотацией @Step")
    public void testIssueSearchWithWebSteps() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        WebSteps steps = new WebSteps();

        steps.openMainPage();
        steps.searchForRepository("Margolog/qa_guru_9");
        steps.clickOnRepositoryLink("Margolog/qa_guru_9");
        steps.shouldSeeIssue();
        steps.takeScreenshot();
    }
}
