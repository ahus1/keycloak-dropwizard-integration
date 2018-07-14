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

// tag::logout[]
@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.logout();
        request.getRequestDispatcher("/logout.ftl").forward(request, response);
    }

// end::logout[]

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
// tag::logout[]
}
// end::logout[]
