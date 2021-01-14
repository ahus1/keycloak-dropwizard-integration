package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.keycloak.dropwizard.User;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * Ressourcce to show the name of the user ("Who am I").
 * This is a simple resource that offers a GET request to test the redirect on accessing a protected resource.
 * This will work only in non-bearer-only mode.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class WhoamiRessource {

    private final boolean logoutShouldRemoveCookie;

    public WhoamiRessource(boolean logoutShouldRemoveCookie) {
        this.logoutShouldRemoveCookie = logoutShouldRemoveCookie;
    }

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/whoami")
    @RolesAllowed("user")
    public WhoamiView whoami(@Auth User user) {
        WhoamiBean whoami = new WhoamiBean();
        whoami.setName(user.getName());
        return new WhoamiView(whoami);
    }

    @GET
    @Path("/logout")
    public Response logout(@Context SecurityContext context) throws ServletException {
        if (context.getUserPrincipal() != null) {
            request.logout();
        }
        Response.ResponseBuilder response = Response.status(Response.Status.OK).entity(new LogoutView());

        // logout will not remove the cookie if the state is stored in a cookie. Therefore remove it manually.
        if (logoutShouldRemoveCookie) {
            response.cookie(new NewCookie("KEYCLOAK_ADAPTER_STATE", null, "/", null, null, 0, false, true));
        }

        return response.build();
    }

}
