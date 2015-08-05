package de.ahus1.lottery.adapter.dropwizard.pages;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import static org.assertj.core.api.Assertions.assertThat;

public class LogoutPage {
    private final HtmlPage page;

    public LogoutPage(HtmlPage page) {
        this.page = page;
        verify();
    }

    private void verify() {
        assertThat(page.getBody().asText()).contains("You have been logged out");
    }


}
