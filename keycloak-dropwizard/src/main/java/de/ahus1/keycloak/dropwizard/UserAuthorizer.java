package de.ahus1.keycloak.dropwizard;

import io.dropwizard.auth.Authorizer;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.checkerframework.checker.nullness.qual.Nullable;

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
