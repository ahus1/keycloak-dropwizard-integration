package de.ahus1.keycloak.dropwizard;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ForbiddenException;
import org.keycloak.KeycloakSecurityContext;

/**
 * This is a default implementation for a user. Consider extending this class or AbstractUser
 * with any needed wrapper around AccessToken and IDToken.
 */
public class User extends AbstractUser {

    public User(KeycloakSecurityContext securityContext, HttpServletRequest request,
                KeycloakConfiguration keycloakConfiguration) {
        super(request, securityContext, keycloakConfiguration);
    }

    public void checkUserInRole(String role) {
        if (!getRoles().contains(role)) {
            throw new ForbiddenException();
        }
    }

    @Override
    public String getName() {
        return securityContext.getToken().getName();
    }

}
