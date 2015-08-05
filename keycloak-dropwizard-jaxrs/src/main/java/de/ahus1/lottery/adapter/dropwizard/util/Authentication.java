package de.ahus1.lottery.adapter.dropwizard.util;

import de.ahus1.lottery.adapter.dropwizard.resource.Role;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.WebApplicationException;
import java.util.Locale;

public class Authentication {
    private HttpServletRequest request;
    private KeycloakSecurityContext securityContext;

    public Authentication(KeycloakSecurityContext securityContext, HttpServletRequest request) {
        this.request = request;
        this.securityContext = securityContext;
    }

    public IDToken getIdToken() {
        return securityContext.getIdToken();
    }

    public void checkUserInRole(Role role) {
        if(!securityContext.getToken().getRealmAccess()
                .isUserInRole(
                        role.name().toLowerCase(Locale.ENGLISH))
                ) {
            throw new ForbiddenException();
        }
    }

    public void logout() {
        AdapterDeploymentContext deploymentContext = (AdapterDeploymentContext) request.getAttribute(AdapterDeploymentContext.class.getName());
        KeycloakSecurityContext ksc = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        if (ksc != null) {
            JaxrsHttpFacade facade = new JaxrsHttpFacade(request);
            KeycloakDeployment deployment = deploymentContext.resolveDeployment(facade);
            if (ksc instanceof RefreshableKeycloakSecurityContext) {
                ((RefreshableKeycloakSecurityContext) ksc).logout(deployment);
            }
            request.removeAttribute(KeycloakSecurityContext.class.getName());
            HttpSession session = request.getSession();
            if (session != null) {
                session.invalidate();
            }
        }
    }

}
