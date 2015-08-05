package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.lottery.adapter.dropwizard.resource.DrawRessource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.sessions.HttpSessionFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.security.Constraint;
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
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        environment.getApplicationContext().setSecurityHandler(securityHandler);
        securityHandler.addRole("user");
        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec("/*");
        Constraint constraint = new Constraint();
        // end::constraint[]

        /* if I put false here, there will be deferred authentication. This will
        not work when using oAuth redirects (as they will not make it to the front end).
        The DeferredAuthentication will swallow them.

        This might be different with a bearer token?!
         */
        // tag::constraint[]
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user"});
        constraintMapping.setConstraint(constraint);
        securityHandler.addConstraintMapping(constraintMapping);
        // end::constraint[]

        // tag::keycloak[]
        KeycloakJettyAuthenticator keycloak = new KeycloakJettyAuthenticator();
        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
        keycloak.setAdapterConfig(configuration.getKeycloakConfiguration());
        // end::keycloak[]

        // allow (stateful) sessions in Dropwizard, needed for Keycloak
        environment.jersey().register(HttpSessionFactory.class);
        environment.servlets().setSessionHandler(new SessionHandler());

        // register web resources.
        environment.jersey().register(new DrawRessource());

        // support annotation @RolesAllowed
        // tag::roles[]
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        // end::roles[]
    }
}
