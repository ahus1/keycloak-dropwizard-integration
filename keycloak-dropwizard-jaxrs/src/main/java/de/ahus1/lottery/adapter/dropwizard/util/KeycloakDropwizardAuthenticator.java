package de.ahus1.lottery.adapter.dropwizard.util;

import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.server.Request;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterTokenStore;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.jetty.KeycloakJettyAuthenticator;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class KeycloakDropwizardAuthenticator extends KeycloakJettyAuthenticator {
    @Override
    public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory) throws ServerAuthException {
        HttpServletRequest request = ((HttpServletRequest) req);
        if(request.getQueryString() != null && request.getQueryString().contains("code=")) {
            mandatory = true;
        }
        HttpSession session = ((HttpServletRequest) req).getSession(false);
        if(session != null && session.getAttribute(JaxrsSessionTokenStore.CACHED_FORM_PARAMETERS) != null) {
            // workaround for https://issues.jboss.org/browse/KEYCLOAK-1776
            Request r = (req instanceof Request) ? (Request)req : HttpChannel.getCurrentHttpChannel().getRequest();
            r.setContentType("application/x-www-form-urlencoded");
            // end of workaround
            mandatory = true;
        }
        return super.validateRequest(req, res, mandatory);
    }

    @Override
    public AdapterTokenStore createSessionTokenStore(Request request, KeycloakDeployment resolvedDeployment) {
        return new JaxrsSessionTokenStore(request, null, resolvedDeployment);
    }
}
