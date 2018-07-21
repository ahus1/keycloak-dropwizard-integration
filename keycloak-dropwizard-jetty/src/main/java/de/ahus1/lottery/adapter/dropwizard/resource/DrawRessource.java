package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.lottery.domain.DrawingService;
import org.keycloak.KeycloakSecurityContext;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class DrawRessource {

    @Context
    private HttpServletRequest request;

    @GET
    // @RolesAllowed("user")
    public DrawView show() {
        KeycloakSecurityContext session =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        DrawBean bean = new DrawBean();
        DrawView view = new DrawView(bean);
        bean.setIdToken(session.getIdToken());
        return view;
    }

    @POST
    @Path("/draw")
    @RolesAllowed("user")
    public DrawView draw(@FormParam("date") String dateAsString) {
        KeycloakSecurityContext session =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        DrawBean bean = new DrawBean();
        LocalDate date = LocalDate.parse(dateAsString);
        bean.setDraw(DrawingService.drawNumbers(date));
        DrawView view = new DrawView(bean);
        bean.setIdToken(session.getIdToken());
        return view;
    }

    @GET
    @Path("/logout")
    public LogoutView logout() throws ServletException {
        request.logout();
        return new LogoutView();
    }

}
