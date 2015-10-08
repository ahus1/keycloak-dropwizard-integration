package de.ahus1.lottery.adapter.dropwizard.util;

import de.ahus1.keycloak.dropwizardjaxrs.AbstractKeycloakAuthenticator;
import org.keycloak.KeycloakSecurityContext;

import javax.servlet.http.HttpServletRequest;

public class KeycloakAuthenticator extends AbstractKeycloakAuthenticator<Authentication> {

    @Override
    protected Authentication prepareAuthentication(KeycloakSecurityContext securityContext, HttpServletRequest request) {
        return new Authentication(securityContext, request);
    }
}
