package de.ahus1.lottery.adapter.dropwizard;

import com.gargoylesoftware.htmlunit.WebClient;
import com.google.common.io.Resources;
import de.ahus1.lottery.adapter.dropwizard.pages.DrawPage;
import de.ahus1.lottery.adapter.dropwizard.pages.StartPage;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class LotteryApplicationTest {

    @ClassRule
    public static final DropwizardAppRule<LotteryConfiguration> RULE =
            new DropwizardAppRule<LotteryConfiguration>(LotteryApplication.class,
                    // "config.yml"
                    // resourceFilePath("config.yml")
                    new File("../config.yml").getAbsolutePath()
            );

    public static String resourceFilePath(final String resourceClassPathLocation) {
        try {
            return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void shouldCalculateDraw() throws IOException, ReflectiveOperationException {
        try (final WebClient webClient = new WebClient()) {
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
    public void shouldLoginFromPost() throws IOException, ReflectiveOperationException {
        try (final WebClient webClient = new WebClient()) {
            // load initial page, will redirect to keycloak
            URL baseUrl = new URL("http://localhost:" + RULE.getLocalPort());
            DrawPage
                    .openWithoutLogin(webClient, baseUrl, LocalDate.parse("2015-01-01"))
                    .login("demo", "demo")
                    .logout();
        }
    }

}
