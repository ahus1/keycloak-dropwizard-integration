package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class WhoamiPage extends Page {

    @Drone
    private WebDriver browser;

    @Override
    public void verify() {
        assertThat(browser.getTitle()).isEqualTo("Who Am I");
    }

    public static LoginPage<WhoamiPage> openWithoutLogin(WebDriver webClient, URL url) {
        webClient.get(url.toString());
        LoginPage<WhoamiPage> loginPage = Page.createPage(LoginPage.class, webClient);
        loginPage.setReturnPage(WhoamiPage.class);
        return loginPage;
    }

}
