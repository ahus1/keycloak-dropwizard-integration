package de.ahus1.lottery.adapter.dropwizard.util;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.IDToken;

import javax.servlet.http.HttpServletRequest;

public class KeycloakAuthenticator implements Authenticator<HttpServletRequest, Authentication> {

    @Override
    public Optional<Authentication> authenticate(HttpServletRequest request) throws AuthenticationException {
        KeycloakSecurityContext securityContext = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        if (securityContext != null) {
            return Optional.fromNullable(new Authentication(securityContext, request));
        } else {
            return Optional.absent();
        }
    }
}
