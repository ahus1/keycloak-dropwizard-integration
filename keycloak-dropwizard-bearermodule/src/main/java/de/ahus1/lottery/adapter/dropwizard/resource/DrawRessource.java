package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.keycloak.dropwizard.User;
import de.ahus1.lottery.domain.DrawingService;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/draw")
public class DrawRessource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public DrawResponse draw(@Valid DrawRequest input, @Auth User user) {
        return new DrawResponse(DrawingService.drawNumbers(input.getDate()));
    }

}
