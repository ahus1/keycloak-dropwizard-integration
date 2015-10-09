package de.ahus1.keycloak.dropwizard;

import com.google.common.base.Optional;
import io.dropwizard.auth.*;
import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.server.Request;
import org.glassfish.jersey.server.ContainerRequest;
import org.keycloak.adapters.*;
import org.keycloak.adapters.jetty.core.JettyCookieTokenStore;
import org.keycloak.adapters.jetty.core.JettyRequestAuthenticator;
import org.keycloak.enums.TokenStore;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

public class KeycloakAuthFactory<T> extends AuthFactory<HttpServletRequest, T> {

    private AdapterConfig adapterConfig;

    private Authenticator<HttpServletRequest, T> authenticator;

    public static final String TOKEN_STORE_NOTE = "TOKEN_STORE_NOTE";

    private String prefix = "Bearer";

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakAuthFactory.class);

    private final String realm;

    private Class<T> generatedClass;

    private boolean required;

    protected AdapterDeploymentContext deploymentContext;

    private UnauthorizedHandler unauthorizedHandler = new DefaultUnauthorizedHandler();

    @Context
    private HttpServletRequest request;

    public KeycloakAuthFactory(AdapterConfig config, String realm, Authenticator<HttpServletRequest, T> authenticator, Class<T> generatedClass) {
        super(authenticator);
        this.authenticator = authenticator;
        this.adapterConfig = config;
        this.realm = realm;
        this.generatedClass = generatedClass;
        initializeKeycloak();
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public AuthFactory<HttpServletRequest, T> clone(boolean required) {
        this.required = required;
        return new KeycloakAuthFactory(adapterConfig, realm, authenticator, generatedClass);
    }

    @Override
    public Class<T> getGeneratedClass() {
        return generatedClass;
    }

    public void initializeKeycloak() {
        KeycloakDeployment kd = KeycloakDeploymentBuilder.build(adapterConfig);
        deploymentContext = new AdapterDeploymentContext(kd);
    }

    @Override
    public T provide() {
        try {
            validateRequest(request);
            Optional<T> result = authenticator.authenticate(request);
            if (result.isPresent()) {
                return result.get();
            }
        } catch (AuthenticationException e) {
            LOGGER.warn("Error authenticating credentials", e);
            throw new InternalServerErrorException();
        }
        if (required) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        } else {
            return null;
        }
    }


    public void validateRequest(HttpServletRequest request) {
        JaxrsHttpFacade facade = new JaxrsHttpFacade(request);
        request.setAttribute(AdapterDeploymentContext.class.getName(), deploymentContext);
        KeycloakDeployment deployment = deploymentContext.resolveDeployment(facade);
        if (deployment == null || !deployment.isConfigured()) {
            return;
        }

        ContainerRequest requestContext = getContainerRequest();
        AdapterTokenStore tokenStore = getTokenStore(request, requestContext, facade, deployment);

        tokenStore.checkCurrentToken();
        JettyRequestAuthenticator authenticator = createRequestAuthenticator(request, facade, deployment, tokenStore);
        AuthOutcome outcome = authenticator.authenticate();
        if (outcome == AuthOutcome.AUTHENTICATED) {
            return;
        }
        AuthChallenge challenge = authenticator.getChallenge();
        if (challenge != null) {
            challenge.challenge(facade);
            facade.getResponse().setCookie("JSESSIONID", request.getSession().getId(), "/", null, -1, false, false);
            facade.responseFacade.end();
        }
        return;
    }

    protected JettyRequestAuthenticator createRequestAuthenticator(HttpServletRequest request, JaxrsHttpFacade facade,
                                                                   KeycloakDeployment deployment, AdapterTokenStore tokenStore) {
        Request r = (request instanceof Request) ? (Request) request : HttpChannel.getCurrentHttpChannel().getRequest();
        return new JettyRequestAuthenticator(facade, deployment, tokenStore, -1, r);
    }


    public static AdapterTokenStore getTokenStore(HttpServletRequest request, ContainerRequest requestContext, HttpFacade facade, KeycloakDeployment resolvedDeployment) {
        AdapterTokenStore store = (AdapterTokenStore) request.getAttribute(TOKEN_STORE_NOTE);
        if (store != null) {
            return store;
        }

        Request r = (request instanceof Request) ? (Request) request : HttpChannel.getCurrentHttpChannel().getRequest();
        if (resolvedDeployment.getTokenStore() == TokenStore.SESSION) {
            store = new JaxrsSessionTokenStore(r, requestContext, resolvedDeployment);
        } else {
            store = new JettyCookieTokenStore(r, facade, resolvedDeployment);
        }

        request.setAttribute(TOKEN_STORE_NOTE, store);
        return store;
    }

}
