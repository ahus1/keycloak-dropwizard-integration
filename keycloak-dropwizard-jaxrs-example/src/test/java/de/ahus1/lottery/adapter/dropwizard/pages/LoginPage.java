package de.ahus1.lottery.adapter.dropwizard.pages;

import junit.framework.AssertionFailedError;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginPage<T> {
    private final HtmlPage page;
    private final Class<T> clazz;

    public LoginPage(HtmlPage page, Class<T> clazz) {
        this.page = page;
        this.clazz = clazz;
        verify();
    }

    private void verify() {
        assertThat(page.getTitleText()).isEqualTo("Sign in to test");
    }

    public T login(String login, String password) throws IOException, ReflectiveOperationException {
        HtmlForm form =
                page.getForms().stream()
                        .filter(f -> f.getId().equals("kc-form-login"))
                        .findFirst()
                        .orElseThrow(() -> new AssertionFailedError("unable to find keycloak form"));
        form.getInputByName("username").setValueAttribute(login);
        form.getInputByName("password").setValueAttribute(password);
        HtmlPage afterLogin = form.getInputByName("login").click();
        return clazz.getConstructor(HtmlPage.class).newInstance(afterLogin);
    }
}
