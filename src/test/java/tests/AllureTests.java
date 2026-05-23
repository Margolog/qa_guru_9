package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;

public class AllureTests {

    @BeforeAll
    static void setup() {
        Configuration.browserSize = "1920x1080";

        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @Test
    @DisplayName("Чистый Selenide (с Listener)")
    public void testIssueSearchWithListener() {
        open("https://github.com");

        $("[data-target='qbsearch-input.inputButton']").click();
        $("#query-builder-test").sendKeys("Margolog/qa_guru_9");
        $("#query-builder-test").pressEnter();

        $(linkText("Margolog/qa_guru_9")).click();
        $("#issues-tab").click();
        $(withText("Issues")).should(Condition.exist);
        $$("a").findBy(text("new issue")).shouldBe(visible);
    }

    @Test
    @DisplayName("Лямбда шаги через step (name, () -> {})")
    public void testIssueSearchWithSteps() {
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

        step("Проверяем наличие конкретной issue", () -> {
            $$("a").findBy(text("new issue")).shouldBe(visible);
        });
    }

    @Test
    @DisplayName("Шаги с аннотацией @Step")
    public void testIssueSearchWithWebSteps() {
        WebSteps steps = new WebSteps();

        steps.openMainPage();
        steps.searchForRepository("Margolog/qa_guru_9");
        steps.clickOnRepositoryLink("Margolog/qa_guru_9");
        steps.shouldSeeIssue();
        steps.takeScreenshot();
        steps.shouldHaveIssue("new issue");
    }
}
