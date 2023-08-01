package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.keycloak.dropwizard.User;
import de.ahus1.lottery.domain.DrawingService;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/draw")
public class DrawRessource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public DrawResponse draw(@Valid DrawRequest input, @Auth User user) {
        return new DrawResponse(DrawingService.drawNumbers(input.getDate()));
    }

}
