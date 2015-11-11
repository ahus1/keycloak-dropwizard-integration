package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.keycloak.dropwizard.KeycloakConfiguration;
import io.dropwizard.Configuration;
import org.keycloak.representations.adapters.config.AdapterConfig;

// tag::config[]
public class LotteryConfiguration extends Configuration {

    private KeycloakConfiguration keycloakConfiguration = new KeycloakConfiguration();

    public KeycloakConfiguration getKeycloakConfiguration() {
        if(keycloakConfiguration.isBearerOnly() == false) {
            throw new RuntimeException("For this application the backend must be configured 'bearer only'. " +
                    "This avoids confusion of the keycloak dropwizard plugin when detecting OAuth redirects");
        }
        return keycloakConfiguration;
    }

    public void setKeycloakConfiguration(KeycloakConfiguration keycloakConfiguration) {
        this.keycloakConfiguration = keycloakConfiguration;
    }
}
// end::config[]

                

