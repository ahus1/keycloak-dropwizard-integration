package de.ahus1.keycloak.dropwizard;

import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.authentication.DeferredAuthentication;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Request;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterTokenStore;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.jetty.JettyAdapterSessionStore;
import org.keycloak.adapters.jetty.KeycloakJettyAuthenticator;
import org.keycloak.adapters.jetty.core.JettySessionTokenStore;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class KeycloakDropwizardAuthenticator extends KeycloakJettyAuthenticator {
    @Override
    public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory) throws ServerAuthException {
        HttpServletRequest request = ((HttpServletRequest) req);
        if (request.getQueryString() != null && request.getQueryString().contains("code=")) {
            // we receive a code as part of the query string that is returned by OAuth
            mandatory = true;
        } else if (request.getHeaders("Authorization").hasMoreElements()) {
            // we receive Authorization, might be Bearer or Basic Auth (both supported by Keycloak)
            mandatory = true;
        }
        HttpSession session = ((HttpServletRequest) req).getSession(false);
        if (session != null && session.getAttribute(JettyAdapterSessionStore.CACHED_FORM_PARAMETERS) != null) {
            // this is a redirect after the code has been received for a FORM
            mandatory = true;
        } else if (session != null && session.getAttribute(KeycloakSecurityContext.class.getName()) != null) {
            // there is an existing authentication in the session, use it
            mandatory = true;
        }
        Authentication authentication = super.validateRequest(req, res, mandatory);
        if (authentication instanceof DeferredAuthentication) {
            // resolving of a deferred authentication later will otherwise lead to a NullPointerException
            authentication = null;
        }
        return authentication;
    }

    @Override
    public AdapterTokenStore createSessionTokenStore(Request request, KeycloakDeployment resolvedDeployment) {
        return new JettySessionTokenStore(request, resolvedDeployment, new JettyAdapterSessionStore(request));
    }
}
