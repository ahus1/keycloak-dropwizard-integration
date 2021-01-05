package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

public class DrawPage extends Page {

    @FindBy(id = "result")
    private WebElement areaResult;

    @FindBy(name = "logout")
    private WebElement linkLogout;

    @Override
    public void verify() {
        Graphene.waitAjax().withTimeout(2, TimeUnit.SECONDS).until().element(areaResult).is().visible();
    }

    public LoginPage logout() {
        linkLogout.click();
        LoginPage page = createPage(LoginPage.class);
        page.setReturnPage(StartPage.class);
        return page;
    }

}
