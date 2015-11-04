package de.ahus1.keycloak.dropwizard;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * This is a base class you can use for your own applications authentication. Feel free to
 * roll your own, as I don't want to impose any class dependencies on your (domain) model.
 */
public abstract class AbstractUser implements Principal {
    protected HttpServletRequest request;
    protected KeycloakSecurityContext securityContext;

    public AbstractUser(HttpServletRequest request, KeycloakSecurityContext securityContext) {
        this.request = request;
        this.securityContext = securityContext;
    }

    public void logout() throws ServletException {
        if(request.getUserPrincipal() != null) {
            request.logout();
        }
    }
}
