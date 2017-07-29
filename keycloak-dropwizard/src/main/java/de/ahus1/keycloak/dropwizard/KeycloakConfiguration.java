package de.ahus1.keycloak.dropwizard;

import org.keycloak.representations.adapters.config.AdapterConfig;

/**
 * This is class currently only exists so users of the
 * dropwizard-keycloak module don't have to import org.keycloak classes.
 * In future this class may have helper methods.
 */
public class KeycloakConfiguration extends AdapterConfig {
}
