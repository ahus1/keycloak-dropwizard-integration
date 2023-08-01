package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WhoamiPage extends Page {

    @Drone
    private WebDriver browser;

    @Override
    public void verify() {
        Graphene.waitAjax().withTimeout(10, TimeUnit.SECONDS)
                .until(webDriver -> webDriver.getTitle().equals("Who Am I"));
    }

    public static LoginPage<WhoamiPage> openWithoutLogin(WebDriver webClient, URL url) {
        webClient.manage().deleteAllCookies();
        webClient.get(url.toString());
        LoginPage<WhoamiPage> loginPage = Page.createPage(LoginPage.class, webClient);
        loginPage.setReturnPage(WhoamiPage.class);
        return loginPage;
    }

}
