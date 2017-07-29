package de.ahus1.keycloak.dropwizard;

import org.keycloak.KeycloakSecurityContext;

import javax.servlet.http.HttpServletRequest;

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
    protected User prepareAuthentication(KeycloakSecurityContext securityContext, HttpServletRequest request, KeycloakConfiguration configuration) {
        return new User(securityContext, request, configuration);
    }
}
