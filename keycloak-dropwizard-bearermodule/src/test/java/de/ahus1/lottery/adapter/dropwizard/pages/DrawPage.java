package de.ahus1.lottery.adapter.dropwizard.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class DrawPage extends Page {

    @FindBy(id = "result")
    private WebElement areaResult;

    @FindBy(name = "logout")
    private WebElement linkLogout;

    @Override
    public void verify() {
        assertThat(areaResult.isDisplayed());
    }

    public LoginPage logout() throws IOException {
        linkLogout.click();
        LoginPage page = createPage(LoginPage.class);
        page.setReturnPage(StartPage.class);
        return page;
    }

}
