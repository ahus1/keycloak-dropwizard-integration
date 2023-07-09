package de.ahus1.keycloak.dropwizard;

import com.google.common.base.Preconditions;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import org.eclipse.jetty.server.Request;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.AdapterTokenStore;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.jetty.JettyAdapterSessionStore;
import org.keycloak.adapters.jetty.core.JettyCookieTokenStore;
import org.keycloak.adapters.jetty.core.JettyRequestAuthenticator;
import org.keycloak.adapters.jetty.core.JettySessionTokenStore;
import org.keycloak.adapters.spi.AuthChallenge;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.enums.TokenStore;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Optional;

@Priority(Priorities.AUTHENTICATION)
public class KeycloakAuthFilter<P extends Principal> extends AuthFilter<HttpServletRequest, P> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakAuthFilter.class);

    public static final String TOKEN_STORE_NOTE = "TOKEN_STORE_NOTE";

    private AdapterDeploymentContext deploymentContext;

    private AdapterConfig adapterConfig;

    public void initializeKeycloak() {
        KeycloakDeployment kd = KeycloakDeploymentBuilder.build(adapterConfig);
        deploymentContext = new AdapterDeploymentContext(kd);
    }

    private KeycloakAuthFilter(AdapterConfig adapterConfig) {
        this.adapterConfig = adapterConfig;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        validateRequest(requestContext);
        HttpServletRequest request =
                (HttpServletRequest) requestContext.getProperty(HttpServletRequest.class.getName());
        final Optional<P> principal;
        try {
            principal = authenticator.authenticate(request);
            if (principal.isPresent()) {
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return principal.get();
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return authorizer.authorize(principal.get(), role, requestContext);
                    }

                    @Override
                    public boolean isSecure() {
                        return requestContext.getSecurityContext().isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return SecurityContext.BASIC_AUTH;
                    }
                });
            }
        } catch (AuthenticationException e) {
            LOGGER.warn("Error authenticating credentials", e);
            throw new InternalServerErrorException();
        }

        // TODO: re-enable / check if 302 has been returned
        // throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
    }

    public void validateRequest(final ContainerRequestContext requestContext) {
        if (requestContext.getSecurityContext().getUserPrincipal() != null) {
            // the user is already authenticated, further processing is not necessary
            return;
        }
        Request request = Request.getBaseRequest((ServletRequest)
                requestContext.getProperty(HttpServletRequest.class.getName()));
        JaxrsHttpFacade facade = new JaxrsHttpFacade(requestContext, requestContext.getSecurityContext());
        request.setAttribute(AdapterDeploymentContext.class.getName(), deploymentContext);

        KeycloakDeployment deployment = deploymentContext.resolveDeployment(facade);
        if (deployment == null || !deployment.isConfigured()) {
            return;
        }

        AdapterTokenStore tokenStore = getTokenStore(request, facade, deployment);

        tokenStore.checkCurrentToken();
        JettyRequestAuthenticator authenticator = createRequestAuthenticator(request, facade, deployment, tokenStore);
        AuthOutcome outcome = authenticator.authenticate();
        if (outcome == AuthOutcome.AUTHENTICATED) {
            return;
        }
        AuthChallenge challenge = authenticator.getChallenge();
        if (challenge != null) {
            challenge.challenge(facade);
            if (!adapterConfig.isBearerOnly() && deployment.getTokenStore() == TokenStore.SESSION) {
                // create session and set cookie for client
                facade.getResponse().setCookie("JSESSIONID", request.getSession().getId(), "/", null, -1, false, false);
            }
            facade.getResponse().end();
        }
    }

    protected JettyRequestAuthenticator createRequestAuthenticator(HttpServletRequest request, JaxrsHttpFacade facade,
                                                                   KeycloakDeployment deployment,
                                                                   AdapterTokenStore tokenStore) {
        Request r = Request.getBaseRequest(request);
        return new JettyRequestAuthenticator(facade, deployment, tokenStore, -1, r);
    }


    public static AdapterTokenStore getTokenStore(HttpServletRequest request, HttpFacade facade,
                                                  KeycloakDeployment resolvedDeployment) {
        AdapterTokenStore store = (AdapterTokenStore) request.getAttribute(TOKEN_STORE_NOTE);
        if (store != null) {
            return store;
        }

        Request r = Request.getBaseRequest(request);
        if (resolvedDeployment.getTokenStore() == TokenStore.SESSION) {
            store = new JettySessionTokenStore(r, resolvedDeployment, new JettyAdapterSessionStore(r));
        } else {
            store = new JettyCookieTokenStore(r, facade, resolvedDeployment);
        }

        request.setAttribute(TOKEN_STORE_NOTE, store);
        return store;
    }


    /**
     * Builder for {@link KeycloakAuthFilter}.
     * <p>An {@link io.dropwizard.auth.Authenticator} must be provided during the building process.</p>
     *
     * @param <P> the type of the principal
     */
    public static class Builder<P extends Principal>
            extends AuthFilterBuilder<HttpServletRequest, P, KeycloakAuthFilter<P>> {

        private AdapterConfig adapterConfig;

        @Override
        protected KeycloakAuthFilter<P> newInstance() {
            return new KeycloakAuthFilter<>(adapterConfig);
        }

        public Builder<P> setConfig(AdapterConfig adapterConfig) {
            this.adapterConfig = adapterConfig;
            return this;
        }

        @Override
        public KeycloakAuthFilter<P> buildAuthFilter() {
            Preconditions.checkArgument(adapterConfig != null, "Keycloak config is not set");
            KeycloakAuthFilter<P> filter = super.buildAuthFilter();
            filter.initializeKeycloak();
            return filter;
        }
    }
}
