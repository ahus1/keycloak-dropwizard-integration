package de.ahus1.keycloak.dropwizard;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jersey.sessions.HttpSessionFactory;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.keycloak.adapters.jetty.KeycloakJettyAuthenticator;
import org.keycloak.enums.TokenStore;

import javax.ws.rs.container.ContainerRequestFilter;
import java.security.Principal;
import java.util.Locale;

public abstract class KeycloakBundle<T> implements ConfiguredBundle<T> {

    // tag::keycloak[]

    @Override
    @SuppressWarnings("checkstyle:emptyblock")
    public void run(T configuration, Environment environment) {

        /* setup the authenticator in front of the requests to allow for pre-auth integration */
        // tag::authenticator[]
        KeycloakJettyAuthenticator keycloak = new KeycloakDropwizardAuthenticator();
        keycloak.setAdapterConfig(getKeycloakConfiguration(configuration));
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        environment.getApplicationContext().setSecurityHandler(securityHandler);
        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
        // end::authenticator[]

        // tag::authfactory[]
        environment.jersey().register(new AuthDynamicFeature(
                createAuthFactory(configuration)));
        // To use @RolesAllowed annotations
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        // To use @Auth to inject a custom Principal type into your resource
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(getUserClass()));
        // end::authfactory[]

        if (getKeycloakConfiguration(configuration).isBearerOnly()) {
            // no session needed
        } else if (getKeycloakConfiguration(configuration).getTokenStore() != null
                && getKeycloakConfiguration(configuration).getTokenStore().toLowerCase(Locale.ENGLISH)
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
     *
     * @param configuration the application's configuration
     * @return Keycloak auth factory
     */
    protected ContainerRequestFilter createAuthFactory(T configuration) {
        return new KeycloakAuthFilter.Builder<Principal>()
                .setConfig(getKeycloakConfiguration(configuration))
                .setAuthenticator(createAuthenticator(getKeycloakConfiguration(configuration)))
                .setAuthorizer(createAuthorizer())
                .setRealm(getRealm(configuration))
                .buildAuthFilter();
    }

    /**
     * Return the class that will be used to pass in creditionals using the @Auth annotation.
     * Override this method to provide a different class. Ensure that you also override
     * createAuthorizer() and createAuthFactory() as well.
     *
     * @return the class.
     */
    protected Class<? extends Principal> getUserClass() {
        return User.class;
    }

    /**
     * Return the Authorizer instance that will be used to check the @RolesAllowed annotations.
     * Override this method to provide an instance of a different instance of another class.
     *
     * @return the class.
     */
    protected Authorizer createAuthorizer() {
        return new UserAuthorizer();
    }

    /**
     * Return the Authenticator instance that will be used to convert the keycloak context to a
     * user object that can be passed into the Authorizer or in your methods. Override this
     * method if you want to use a different or derived class.
     *
     * @return the authenticator.
     */
    protected Authenticator createAuthenticator(KeycloakConfiguration configuration) {
        return new KeycloakAuthenticator(configuration);
    }

    /**
     * Prepare the realm name. Override as needed to provide a different name.
     *
     * @param configuration the application's configuration
     * @return realm name
     */
    protected String getRealm(T configuration) {
        return getKeycloakConfiguration(configuration).getRealm();
    }

    protected abstract KeycloakConfiguration getKeycloakConfiguration(T configuration);

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

}
