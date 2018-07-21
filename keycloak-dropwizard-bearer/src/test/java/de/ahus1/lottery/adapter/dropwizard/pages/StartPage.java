package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class StartPage extends Page {

    @Drone
    private WebDriver browser;

    @FindBy(name = "date")
    private WebElement fieldDate;

    @FindBy(name = "draw")
    private WebElement buttonDraw;

    public void verify() {
        assertThat(browser.getTitle()).isEqualTo("Lottery Calculator");
    }

    public static LoginPage<StartPage> openWithoutLogin(WebDriver webClient, URL url) throws IOException {
        webClient.get(url.toString());
        LoginPage loginPage = Page.createPage(LoginPage.class, webClient);
        loginPage.setReturnPage(StartPage.class);
        return loginPage;
    }

    public DrawPage draw(LocalDate date) throws IOException {
        fieldDate.sendKeys(date.toString());
        buttonDraw.click();
        DrawPage page = createPage(DrawPage.class);
        return page;
    }
}
