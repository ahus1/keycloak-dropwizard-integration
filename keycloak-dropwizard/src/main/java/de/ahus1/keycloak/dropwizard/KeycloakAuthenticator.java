package de.ahus1.keycloak.dropwizard;

import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.KeycloakSecurityContext;

/**
 * This is a default implementation for AbstractKeycloakAuthenticator. When you use a different
 * User representation you'll also want to create your own implementation for
 * AbstractKeycloakAuthenticator.
 */
public class KeycloakAuthenticator extends AbstractKeycloakAuthenticator<User> {

    public KeycloakAuthenticator(KeycloakConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected User prepareAuthentication(KeycloakSecurityContext securityContext, HttpServletRequest request,
                                         KeycloakConfiguration configuration) {
        return new User(securityContext, request, configuration);
    }
}
