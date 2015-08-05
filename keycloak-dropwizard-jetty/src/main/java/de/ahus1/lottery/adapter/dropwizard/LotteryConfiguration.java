package de.ahus1.lottery.adapter.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.keycloak.representations.adapters.config.AdapterConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

                

