package de.ahus1.lottery.adapter.servlet;

import de.ahus1.lottery.domain.DrawingService;
import org.keycloak.KeycloakPrincipal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = "/")
public class DrawServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // tag::principal[]
        request.setAttribute("idToken", ((KeycloakPrincipal) request.getUserPrincipal())
                .getKeycloakSecurityContext().getIdToken());
        // end::principal[]
        request.getRequestDispatcher("/index.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LocalDate date = LocalDate.parse(request.getParameter("date"));
        request.setAttribute("draw", DrawingService.drawNumbers(date));
        doGet(request, response);
    }
}
