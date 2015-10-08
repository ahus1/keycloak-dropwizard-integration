package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.keycloak.dropwizardjaxrs.KeycloakAuthFactory;
import de.ahus1.keycloak.dropwizardjaxrs.KeycloakBundle;
import de.ahus1.lottery.adapter.dropwizard.resource.DrawRessource;
import de.ahus1.lottery.adapter.dropwizard.util.Authentication;
import de.ahus1.lottery.adapter.dropwizard.util.KeycloakAuthenticator;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.io.IOException;

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
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
        bootstrap.addBundle(new AssetsBundle("/assets/js", "/js", null, "js"));
        bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "fonts"));
        bootstrap.addBundle(new AssetsBundle("/assets/html", "/html", null, "html"));

        // setup Freemarker views.
        bootstrap.addBundle(new ViewBundle());

        // tag::keycloak[]
        bootstrap.addBundle(new KeycloakBundle<LotteryConfiguration>() {
            @Override
            protected KeycloakAuthFactory createAuthFactory(LotteryConfiguration configuration) {
                return new KeycloakAuthFactory(getKeycloakConfiguration(configuration), "dropwizard",
                        new KeycloakAuthenticator(), Authentication.class);
            }

            @Override
            protected AdapterConfig getKeycloakConfiguration(LotteryConfiguration configuration) {
                return configuration.getKeycloakConfiguration();
            }
        });
        // end::keycloak[]

    }

    @Override
    public void run(LotteryConfiguration configuration, Environment environment)
            throws ClassNotFoundException, IOException {

        // register web resources.
        environment.jersey().register(new DrawRessource());

        // support annotation @RolesAllowed
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
