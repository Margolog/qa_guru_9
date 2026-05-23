package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.linkText;

public class WebSteps {

    @Step("Открываем главную страницу")
    public void openMainPage() {
        open("https://github.com");
    }

    @Step("Ищем репу")
    public void searchForRepository(String repo) {
        $("[data-target='qbsearch-input.inputButton']").click();
        $("#query-builder-test").sendKeys("Margolog/qa_guru_9");
        $("#query-builder-test").pressEnter();
    }

    @Step("Кликаем на нужный репозиторий")
    public void clickOnRepositoryLink(String repo) {
        $(linkText("Margolog/qa_guru_9")).click();
        $("#issues-tab").click();
    }

    @Step("Проверяем наличие Issues")
    public void shouldSeeIssue() {
        $(withText("Issues")).should(Condition.exist);
    }

    @Step("Проверяем наличие конкретной issue")
    public void shouldHaveIssue(String issue) {
        $$("a").findBy(text(issue)).shouldBe(visible);
    }

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = "png")
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
}