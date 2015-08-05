package de.ahus1.lottery.adapter.dropwizard.pages;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DrawPage {
    private final HtmlPage page;

    public DrawPage(HtmlPage page) {
        this.page = page;
        verify();
    }

    private void verify() {
        assertThat(page.getBody().asText())
                .describedAs("successful draw")
                .contains("The lucky numbers are");
    }

    public LogoutPage logout() throws IOException {
        return new LogoutPage(page.getAnchorByName("logout").click());
    }

    public static LoginPage<DrawPage> openWithoutLogin(WebClient webClient, URL url, LocalDate parse) throws IOException {
        WebRequest request = new WebRequest(new URL(url.toString() + "/draw"), HttpMethod.POST);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new NameValuePair("date", "2015-01-01"));
        request.setRequestParameters(parameters);
        return new LoginPage<>(webClient.getPage(request), DrawPage.class);
    }
}
