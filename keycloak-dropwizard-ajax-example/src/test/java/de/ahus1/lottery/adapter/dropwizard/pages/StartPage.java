package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.net.URL;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class StartPage extends Page {

    @Drone
    private WebDriver browser;

    @FindBy(name = "date")
    private WebElement fieldDate;

    @FindBy(name = "draw")
    private WebElement buttonDraw;

    @Override
    public void verify() {
        Graphene.waitAjax().withTimeout(10, TimeUnit.SECONDS)
                    .until(webDriver -> fieldDate.isDisplayed());
    }

    public static LoginPage<StartPage> openWithoutLogin(WebDriver webClient, URL url) {
        webClient.manage().deleteAllCookies();
        webClient.get(url.toString());
        boolean[] loggedOut = new boolean[1];
        Graphene.waitAjax().withTimeout(10, TimeUnit.SECONDS)
                .until(webDriver -> {
                    if (webClient.getCurrentUrl().equals(url.toString())) {
                        if (!loggedOut[0]) {
                            try {
                                WebElement logout = webClient.findElement(By.name("logout"));
                                if (logout != null && logout.isDisplayed()) {
                                    logout.click();
                                    loggedOut[0] = true;
                                }
                            } catch (NotFoundException | ElementNotInteractableException ignored) {
                                //
                            }
                        }
                        return false;
                    } else {
                        return true;
                    }
                });
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
