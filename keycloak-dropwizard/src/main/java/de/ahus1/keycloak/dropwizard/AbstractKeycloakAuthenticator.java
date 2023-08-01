package de.ahus1.keycloak.dropwizard;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.KeycloakSecurityContext;

import java.security.Principal;
import java.util.Optional;

/**
 * Authentication to make it work with Keycloak.
 *
 * @param <P> authentication class you will use throughout your application.
 *            You can use AbstractAuthentication as a base class here.
 */
public abstract class AbstractKeycloakAuthenticator<P extends Principal>
        implements Authenticator<HttpServletRequest, P> {

    private final KeycloakConfiguration keycloakConfiguration;

    public AbstractKeycloakAuthenticator(final KeycloakConfiguration keycloakConfiguration) {
        this.keycloakConfiguration = keycloakConfiguration;
    }

    @Override
    public Optional<P> authenticate(HttpServletRequest request) throws AuthenticationException {
        KeycloakSecurityContext securityContext =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        if (securityContext != null) {
            return Optional.ofNullable(prepareAuthentication(securityContext, request, keycloakConfiguration));
        } else {
            return Optional.empty();
        }
    }

    protected abstract P prepareAuthentication(KeycloakSecurityContext securityContext, HttpServletRequest request,
                                               KeycloakConfiguration keycloakConfiguration);
}
