package de.ahus1.lottery.adapter.dropwizard.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginPage<T extends Page> extends Page {

    @FindBy(name = "username")
    private WebElement fieldUsername;

    @FindBy(name = "password")
    private WebElement fieldPassword;

    @FindBy(name = "login")
    private WebElement buttonLogin;

    private Class<T> clazz;

    public void verify() {
        assertThat(browser.getTitle()).isEqualTo("Log in to test");
    }

    public T login(String login, String password) throws IOException, ReflectiveOperationException {
        fieldUsername.sendKeys(login);
        fieldPassword.sendKeys(password);
        buttonLogin.click();
        T page = createPage(clazz);
        return page;
    }

    public void setReturnPage(Class<T> returnPage) {
        this.clazz = returnPage;
    }
}
