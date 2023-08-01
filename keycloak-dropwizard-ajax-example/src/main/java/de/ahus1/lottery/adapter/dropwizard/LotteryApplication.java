package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.keycloak.dropwizard.KeycloakBundle;
import de.ahus1.keycloak.dropwizard.KeycloakConfiguration;
import de.ahus1.lottery.adapter.dropwizard.resource.DrawRessource;
import de.ahus1.lottery.adapter.dropwizard.resource.WhoamiRessource;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.views.common.ViewBundle;
import org.keycloak.enums.TokenStore;

public class LotteryApplication extends Application<LotteryConfiguration> {
    public static void main(String[] args) throws Exception {
        new LotteryApplication().run(args);
    }

    @Override
    public String getName() {
        return "lottery";
    }

    @Override
    public void initialize(Bootstrap<LotteryConfiguration> bootstrap) {

        // set up folders for static content
        bootstrap.addBundle(new AssetsBundle("/assets/ajax", "/ajax", null, "ajax"));

        // setup Freemarker views - this is only needed for the WhoamiResource when in non-bearer-only-mode
        bootstrap.addBundle(new ViewBundle());

        // tag::keycloak[]
        bootstrap.addBundle(new KeycloakBundle<>() {
            @Override
            protected KeycloakConfiguration getKeycloakConfiguration(LotteryConfiguration configuration) {
                return configuration.getKeycloakConfiguration();
            }
            /* OPTIONAL: override getUserClass(), createAuthorizer() and createAuthenticator() if you want to use
             * a class other than de.ahus1.keycloak.dropwizard.User to be injected by @Auth */
        });
        // end::keycloak[]
    }

    @Override
    public void run(LotteryConfiguration configuration, Environment environment) {

        environment.jersey().register(new DrawRessource());

        boolean isUsingCookieStore = configuration.getKeycloakConfiguration().getTokenStore() != null
                && configuration.getKeycloakConfiguration()
                .getTokenStore().equalsIgnoreCase(TokenStore.COOKIE.toString());
        environment.jersey().register(new WhoamiRessource(isUsingCookieStore));

    }
}
