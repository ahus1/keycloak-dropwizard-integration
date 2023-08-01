package de.ahus1.lottery.adapter.dropwizard;

import de.ahus1.keycloak.dropwizard.KeycloakConfiguration;
import io.dropwizard.core.Configuration;

// tag::config[]
public class LotteryConfiguration extends Configuration {

    private KeycloakConfiguration keycloakConfiguration = new KeycloakConfiguration();

    public KeycloakConfiguration getKeycloakConfiguration() {
        return keycloakConfiguration;
    }

    public void setKeycloakConfiguration(KeycloakConfiguration keycloakConfiguration) {
        this.keycloakConfiguration = keycloakConfiguration;
    }
}
// end::config[]



