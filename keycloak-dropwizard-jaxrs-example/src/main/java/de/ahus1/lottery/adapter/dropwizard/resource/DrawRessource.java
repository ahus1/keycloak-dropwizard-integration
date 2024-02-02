package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.keycloak.dropwizard.User;
import de.ahus1.lottery.domain.DrawingService;
import io.dropwizard.auth.Auth;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;

// tag::ressource[]
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class DrawRessource {

    @Context
    private HttpServletRequest request;

    // end::ressource[]
    @GET
    @RolesAllowed("user")
    public DrawView show(@Auth User auth) {
        DrawBean bean = new DrawBean();
        DrawView view = new DrawView(bean);
        bean.setName(auth.getName());
        return view;
    }

    // tag::ressource[]
    @POST
    @Path("/draw")
    @RolesAllowed("user")
    public DrawView draw(@FormParam("date") String dateAsString, @Auth User auth) { // <1>
        DrawBean bean = new DrawBean();
        LocalDate date = LocalDate.parse(dateAsString);
        bean.setDraw(DrawingService.drawNumbers(date));
        DrawView view = new DrawView(bean);
        bean.setName(auth.getName());
        return view;
    }

    @GET
    @Path("/logout")
    public LogoutView logout(@Context SecurityContext context) throws ServletException { // <2>
        if (context.getUserPrincipal() != null) {
            request.logout();
        }
        return new LogoutView();
    }

}
// end::ressource[]
