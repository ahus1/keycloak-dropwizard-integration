package de.ahus1.lottery.adapter.dropwizard.util;

import de.ahus1.keycloak.dropwizardjaxrs.AbstractAuthentication;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import java.util.Locale;

public class Authentication extends AbstractAuthentication {

    public Authentication(KeycloakSecurityContext securityContext, HttpServletRequest request) {
        super(request, securityContext);
    }

    public void checkUserInRole(Role role) {
        if (!securityContext.getToken().getRealmAccess()
                .isUserInRole(
                        role.name().toLowerCase(Locale.ENGLISH))
                ) {
            throw new ForbiddenException();
        }
    }

    public IDToken getIdToken() {
        return securityContext.getIdToken();
    }

}
