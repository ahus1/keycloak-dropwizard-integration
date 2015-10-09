package de.ahus1.keycloak.dropwizard;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.jersey.sessions.HttpSessionFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.keycloak.adapters.jetty.KeycloakJettyAuthenticator;
import org.keycloak.enums.TokenStore;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.util.Locale;

public abstract class KeycloakBundle<T> implements ConfiguredBundle<T> {

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        // tag::constraint[]
        KeycloakJettyAuthenticator keycloak = new KeycloakDropwizardAuthenticator();
        keycloak.setAdapterConfig(getKeycloakConfiguration(configuration));
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        environment.getApplicationContext().setSecurityHandler(securityHandler);
        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
        // end::constraint[]

        // tag::keycloak[]
        KeycloakAuthFactory authFactory = createAuthFactory(configuration);
        environment.jersey().register(AuthFactory.binder(authFactory));
        // end::keycloak[]

        if (getKeycloakConfiguration(configuration).isBearerOnly()) {
            // no session needed
        } else if(getKeycloakConfiguration(configuration).getTokenStore() != null &&
                getKeycloakConfiguration(configuration).getTokenStore().toLowerCase(Locale.ENGLISH)
                        .equals(TokenStore.COOKIE.toString().toLowerCase(Locale.ENGLISH))) {
            // no session needed
        } else {
            // allow (stateful) sessions in Dropwizard
            environment.jersey().register(HttpSessionFactory.class);
            environment.servlets().setSessionHandler(new SessionHandler());
        }
    }

    /**
     * Default implementation for the Keycloak auth factory. Please provide your own if you implement
     * your own User's representation.
     * @param configuration the application's configuration
     * @return Keycloak auth factory
     */
    protected KeycloakAuthFactory createAuthFactory(T configuration) {
        return new KeycloakAuthFactory(getKeycloakConfiguration(configuration), getRealm(configuration),
                new KeycloakAuthenticator(), User.class);
    }

    /**
     * Prepare the realm name. Override as needed to provide a different name.
     * @param configuration for future use
     * @return realm name
     */
    protected String getRealm(T configuration) {
        return "dropwizard";
    };

    protected abstract AdapterConfig getKeycloakConfiguration(T configuration);

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

}
