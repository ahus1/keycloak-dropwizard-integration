package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.keycloak.dropwizard.User;
import de.ahus1.lottery.domain.DrawingService;
import io.dropwizard.auth.Auth;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

// tag::ressource[]
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class DrawRessource {

    // end::ressource[]
    @GET
    public DrawView show(@Auth User auth) {
        auth.checkUserInRole("user");
        DrawBean bean = new DrawBean();
        DrawView view = new DrawView(bean);
        bean.setName(auth.getName());
        return view;
    }

    // tag::ressource[]
    @POST
    @Path("/draw")
    public DrawView draw(@FormParam("date") String dateAsString, @Auth User auth) { // <1>
        auth.checkUserInRole("user");
        DrawBean bean = new DrawBean();
        LocalDate date = LocalDate.parse(dateAsString);
        bean.setDraw(DrawingService.drawNumbers(date));
        DrawView view = new DrawView(bean);
        bean.setName(auth.getName());
        return view;
    }

    @GET
    @Path("/logout")
    public LogoutView logout(@Auth(required = false) User auth) throws ServletException { // <2>
        // request.logout(); // no effect as we didn't hook into jetty
        if (auth != null) {
            auth.logout();
        }
        return new LogoutView();
    }

}
// end::ressource[]
