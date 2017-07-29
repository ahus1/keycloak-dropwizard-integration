package de.ahus1.keycloak.dropwizard;

import io.dropwizard.auth.Authorizer;

import javax.ws.rs.ForbiddenException;

public class UserAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        try {
            user.checkUserInRole(role);
            return true;
        } catch (ForbiddenException e) {
            return false;
        }
    }
}
