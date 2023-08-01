package de.ahus1.keycloak.dropwizard;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a base class you can use for your own applications authentication. Feel free to
 * roll your own, as I don't want to impose any class dependencies on your (domain) model.
 */
public abstract class AbstractUser implements Principal {

    // TODO: change visibility to hidden and add (protected) getters
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected HttpServletRequest request;
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected KeycloakSecurityContext securityContext;
    private final Set<String> roles;

    public AbstractUser(HttpServletRequest request, KeycloakSecurityContext securityContext,
                        KeycloakConfiguration keycloakConfiguration) {
        this.request = request;
        this.securityContext = securityContext;

        this.roles = selectRolesToApply(keycloakConfiguration);
    }

    /**
     * The configuration parameter use-resource-role-mappings define if the module should use Realm roles OR
     * Resources roles.
     * Resources roles correspond to the role given by the client in Keycloak
     *
     * @param keycloakConfiguration Keycloak configuration
     * @return list of user's roles.
     */
    private Set<String> selectRolesToApply(KeycloakConfiguration keycloakConfiguration) {
        if (keycloakConfiguration.isUseResourceRoleMappings()) {
            return this.selectResourceRoles(new KeycloakResource(keycloakConfiguration.getResource()));
        }
        return this.selectRealmRoles();
    }

    private Set<String> selectResourceRoles(KeycloakResource keycloakResource) {
        Set<String> roles = new HashSet<>();

        AccessToken.Access resourceAccess =
                securityContext.getToken().getResourceAccess(keycloakResource.getResource());
        if (resourceAccess != null && resourceAccess.getRoles() != null) {
            roles.addAll(resourceAccess.getRoles());
        }
        return Collections.unmodifiableSet(roles);
    }

    private Set<String> selectRealmRoles() {
        Set<String> roles = new HashSet<>();

        AccessToken.Access realmAccess = securityContext.getToken().getRealmAccess();
        if (realmAccess != null && realmAccess.getRoles() != null) {
            roles.addAll(realmAccess.getRoles());
        }
        return Collections.unmodifiableSet(roles);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void logout() throws ServletException {
        if (request.getUserPrincipal() != null) {
            request.logout();
        }
    }
}
