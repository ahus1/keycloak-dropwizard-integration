package de.ahus1.keycloak.dropwizard;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This is a base class you can use for your own applications authentication. Feel free to
 * roll your own, as I don't want to impose any class dependencies on your (domain) model.
 */
public abstract class AbstractUser {
    protected HttpServletRequest request;
    protected KeycloakSecurityContext securityContext;

    public AbstractUser(HttpServletRequest request, KeycloakSecurityContext securityContext) {
        this.request = request;
        this.securityContext = securityContext;
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
