package de.ahus1.keycloak.dropwizard;

import io.dropwizard.auth.Authorizer;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;

public class UserAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role, @Nullable ContainerRequestContext requestContext) {
        try {
            user.checkUserInRole(role);
            return true;
        } catch (ForbiddenException e) {
            return false;
        }
    }
}
