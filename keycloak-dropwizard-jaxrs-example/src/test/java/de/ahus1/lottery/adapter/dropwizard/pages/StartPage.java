package de.ahus1.lottery.adapter.dropwizard.pages;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class StartPage {

    private HtmlPage page;

    public StartPage(HtmlPage page) {
        this.page = page;
        verify();
    }

    private void verify() {
        assertThat(page.getTitleText()).isEqualTo("Lottery Calculator");
    }

    public static LoginPage<StartPage> openWithoutLogin(WebClient webClient, URL url) throws IOException {
        return new LoginPage<>(webClient.getPage(url), StartPage.class);
    }

    public DrawPage draw(LocalDate date) throws IOException {
        HtmlForm draw = page.getFormByName("draw");
        draw.getInputByName("date").setValueAttribute(date.toString());
        return new DrawPage(draw.getInputByName("draw").click());
    }
}
