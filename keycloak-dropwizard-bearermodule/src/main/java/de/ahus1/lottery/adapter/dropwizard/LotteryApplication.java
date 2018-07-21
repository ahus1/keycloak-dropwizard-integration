package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.keycloak.dropwizard.KeycloakBundle;
import de.ahus1.keycloak.dropwizard.KeycloakConfiguration;
import de.ahus1.lottery.adapter.dropwizard.resource.DrawRessource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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

        // tag::keycloak[]
        bootstrap.addBundle(new KeycloakBundle<LotteryConfiguration>() {
            @Override
            protected KeycloakConfiguration getKeycloakConfiguration(LotteryConfiguration configuration) {
                return configuration.getKeycloakConfiguration();
            }
            /* OPTIONAL: override getUserClass(), createAuthorizer() and createAuthenticator() if you want to use
             * a class other than de.ahus1.keycloak.dropwizard.User to be injected by @User*/
        });
        // end::keycloak[]
    }

    @Override
    public void run(LotteryConfiguration configuration, Environment environment) {

        environment.jersey().register(new DrawRessource());
    }
}
