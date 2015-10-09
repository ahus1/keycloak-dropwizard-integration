package de.ahus1.keycloak.dropwizard;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.MultiMap;
import org.glassfish.jersey.server.ContainerRequest;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.jetty.JettySessionTokenStore;

import javax.ws.rs.core.Form;
import java.util.List;
import java.util.Map;

public class JaxrsSessionTokenStore extends JettySessionTokenStore {

    private final ContainerRequest requestContext;

    public JaxrsSessionTokenStore(Request request, ContainerRequest requestContext, KeycloakDeployment deployment) {
        super(request, deployment);
        this.requestContext = requestContext;
    }

    @Override
    protected MultiMap<String> extractFormParameters(Request base_request) {
        MultiMap<String> formParameters = super.extractFormParameters(base_request);
        if (formParameters.size() == 0 && requestContext != null) {
            Form form = requestContext.readEntity(Form.class);
            for (Map.Entry<String, List<String>> entry : form.asMap().entrySet()) {
                formParameters.addValues(entry.getKey(), entry.getValue());
            }
        }
        return formParameters;
    }

}
