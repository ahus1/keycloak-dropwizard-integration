package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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

    @Override
    public void verify() {
        assertThat(browser.getTitle()).isEqualTo("Lottery Calculator");
    }

    public static LoginPage<StartPage> openWithoutLogin(WebDriver webClient, URL url) {
        webClient.get(url.toString());
        if (webClient.getCurrentUrl().equals(url.toString())) {
            // if we end up at the target page, the browser was still logged in
            webClient.navigate()
                    .to("http://localhost:8080/realms/test/protocol/openid-connect/logout?redirect_uri=" + url);
        }
        LoginPage<StartPage> loginPage = Page.createPage(LoginPage.class, webClient);
        loginPage.setReturnPage(StartPage.class);
        return loginPage;
    }

    public DrawPage draw(LocalDate date) {
        fieldDate.sendKeys(date.toString());
        buttonDraw.click();
        return createPage(DrawPage.class);
    }
}
