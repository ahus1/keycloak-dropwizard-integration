package de.ahus1.lottery.adapter.dropwizard.pages;

import org.htmlunit.HttpMethod;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        assertThat(page.getBody().asNormalizedText())
                .describedAs("successful draw")
                .contains("The lucky numbers are");
    }

    public LogoutPage logout() throws IOException {
        return new LogoutPage(page.getAnchorByName("logout").click());
    }

    public static LoginPage<DrawPage> openWithoutLogin(WebClient webClient, URL url, LocalDate date)
            throws IOException {
        WebRequest request = new WebRequest(new URL(url.toString() + "/draw"), HttpMethod.POST);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new NameValuePair("date", date.format(DateTimeFormatter.ISO_DATE)));
        request.setRequestParameters(parameters);
        return new LoginPage<>(webClient.getPage(request), DrawPage.class);
    }
}
