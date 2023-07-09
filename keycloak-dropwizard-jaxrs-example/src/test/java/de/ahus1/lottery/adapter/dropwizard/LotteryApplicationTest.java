package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.lottery.adapter.dropwizard.pages.DrawPage;
import de.ahus1.lottery.adapter.dropwizard.pages.StartPage;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.htmlunit.WebClient;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class LotteryApplicationTest {

    @ClassRule
    public static final DropwizardAppRule<LotteryConfiguration> RULE =
            new DropwizardAppRule<>(LotteryApplication.class,
                    new File("../config.yml").getAbsolutePath()
            );

    @Test
    public void shouldCalculateDraw() throws IOException, ReflectiveOperationException {
        try (WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            // load initial page, will redirect to keycloak
            URL baseUrl = new URL("http://localhost:" + RULE.getLocalPort());
            StartPage
                    .openWithoutLogin(webClient, baseUrl)
                    .login("demo", "demo")
                    .draw(LocalDate.parse("2015-01-01"))
                    .logout();
        }
    }

    @Test
    @Ignore("No longer supported starting with Dropwizard 2.0 - see README.adoc at top of project")
    public void shouldLoginFromPost() throws IOException, ReflectiveOperationException {
        try (WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            // load initial page, will redirect to keycloak
            URL baseUrl = new URL("http://localhost:" + RULE.getLocalPort());
            DrawPage
                    .openWithoutLogin(webClient, baseUrl, LocalDate.parse("2015-01-01"))
                    .login("demo", "demo")
                    .logout();
        }
    }

}
