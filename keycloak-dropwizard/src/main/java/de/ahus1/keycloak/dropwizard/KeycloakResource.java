package de.ahus1.keycloak.dropwizard;

import org.apache.commons.lang3.StringUtils;

/**
 * Map the resource name.
 */
class KeycloakResource {

    private final String resource;

    KeycloakResource(String resource) {
        if (StringUtils.isBlank(resource)) {
            throw new IllegalArgumentException("Cannot build a " + getClass().getName() + " without a resource.");
        }

        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
