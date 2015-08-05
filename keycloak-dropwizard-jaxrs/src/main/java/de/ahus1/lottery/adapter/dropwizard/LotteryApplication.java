package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.lottery.adapter.dropwizard.resource.DrawRessource;
import de.ahus1.lottery.adapter.dropwizard.util.Authentication;
import de.ahus1.lottery.adapter.dropwizard.util.KeycloakAuthFactory;
import de.ahus1.lottery.adapter.dropwizard.util.KeycloakAuthenticator;
import de.ahus1.lottery.adapter.dropwizard.util.KeycloakDropwizardAuthenticator;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.jersey.sessions.HttpSessionFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.keycloak.adapters.jetty.KeycloakJettyAuthenticator;

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

    }

    @Override
    public void run(LotteryConfiguration configuration, Environment environment)
            throws ClassNotFoundException, IOException {

        // tag::constraint[]
        KeycloakJettyAuthenticator keycloak = new KeycloakDropwizardAuthenticator();
        keycloak.setAdapterConfig(configuration.getKeycloakConfiguration());
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        environment.getApplicationContext().setSecurityHandler(securityHandler);
        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
        // end::constraint[]

        // tag::keycloak[]
        KeycloakAuthFactory authFactory = new KeycloakAuthFactory(configuration.getKeycloakConfiguration(), "dropwizard", new KeycloakAuthenticator(), Authentication.class);
        environment.jersey().register(AuthFactory.binder(authFactory));
        // end::keycloak[]

        // allow (stateful) sessions in Dropwizard
        environment.jersey().register(HttpSessionFactory.class);
        environment.servlets().setSessionHandler(new SessionHandler());

        // register web resources.
        environment.jersey().register(new DrawRessource());

        // support annotation @RolesAllowed
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
