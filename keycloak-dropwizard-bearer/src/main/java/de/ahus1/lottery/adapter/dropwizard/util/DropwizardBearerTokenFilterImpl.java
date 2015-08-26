package de.ahus1.lottery.adapter.dropwizard.util;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.NodesRegistrationManagement;
import org.keycloak.jaxrs.JaxrsBearerTokenFilterImpl;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.PreMatching;


/**
 * Allow the filter to be created from a KeycloakDeployment instance directly to make
 * it easier in a Dropwizard environment. This overrides the initialization functionality
 * in the parent class, but keeps all the filtering logic.
 *
 * annotations are necessary to handle authentication before i.e. role matching.
 * Jersey doesn't look at the annotations on the interface or the parent class.
 *
 * @author Alexander Schwartz (alexander.schwartz@gmx.net)
 */
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class DropwizardBearerTokenFilterImpl extends JaxrsBearerTokenFilterImpl {

    public DropwizardBearerTokenFilterImpl(KeycloakDeployment keycloakDeployment) {
        deploymentContext = new AdapterDeploymentContext(keycloakDeployment);
        nodesRegistrationManagement = new NodesRegistrationManagement();
    }
}
