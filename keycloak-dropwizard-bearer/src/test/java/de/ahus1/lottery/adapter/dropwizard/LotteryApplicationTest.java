package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.lottery.adapter.dropwizard.pages.StartPage;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

@RunWith(Arquillian.class)
public class LotteryApplicationTest {

    @Drone
    private WebDriver browser;

    @ClassRule
    public static final DropwizardAppRule<LotteryConfiguration> RULE =
            new DropwizardAppRule<>(LotteryApplication.class,
                    getConfig()
            );

    private static String getConfig() {
        File file = new File("config.yml");
        if (!file.exists()) {
            file = new File("../config.yml");
        }
        return file.getAbsolutePath();
    }

    @Test
    public void shouldCalculateDraw() throws IOException, ReflectiveOperationException {
        // load initial page, will redirect to keycloak
        URL baseUrl = new URL("http://localhost:" + RULE.getLocalPort() + "/ajax/index.html");
        StartPage
                .openWithoutLogin(browser, baseUrl)
                .login("demo", "demo")
                .draw(LocalDate.parse("2015-01-01"))
                .logout();
    }

}
