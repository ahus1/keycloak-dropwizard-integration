package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.keycloak.dropwizard.KeycloakBundle;
import de.ahus1.keycloak.dropwizard.KeycloakConfiguration;
import de.ahus1.lottery.adapter.dropwizard.resource.DrawRessource;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.views.common.ViewBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

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

        // register web resources.
        environment.jersey().register(DrawRessource.class);

        // support annotation @RolesAllowed
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
