package de.ahus1.lottery.adapter.dropwizard;

import io.dropwizard.Configuration;
import org.keycloak.representations.adapters.config.AdapterConfig;

// tag::config[]
public class LotteryConfiguration extends Configuration {

    private AdapterConfig keycloakConfiguration = new AdapterConfig();

    public AdapterConfig getKeycloakConfiguration() {
        return keycloakConfiguration;
    }

    public void setKeycloakConfiguration(AdapterConfig keycloakConfiguration) {
        this.keycloakConfiguration = keycloakConfiguration;
    }
}
// end::config[]
