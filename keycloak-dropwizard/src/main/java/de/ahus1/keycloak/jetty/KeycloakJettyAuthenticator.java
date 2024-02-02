/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ahus1.keycloak.jetty;

import de.ahus1.keycloak.jetty.core.AbstractKeycloakJettyAuthenticator;
import de.ahus1.keycloak.jetty.core.JettyRequestAuthenticator;
import de.ahus1.keycloak.jetty.core.JettySessionTokenStore;
import de.ahus1.keycloak.jetty.spi.JettyHttpFacade;
import de.ahus1.keycloak.jetty.spi.JettyUserSessionManagement;
import jakarta.servlet.ServletRequest;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.UserIdentity;
import org.keycloak.adapters.AdapterTokenStore;
import org.keycloak.adapters.KeycloakDeployment;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class KeycloakJettyAuthenticator extends AbstractKeycloakJettyAuthenticator {

    public KeycloakJettyAuthenticator() {
        super();
    }


    @Override
    protected Request resolveRequest(ServletRequest req) {
        return Request.getBaseRequest(req);
    }

    @Override
    protected Authentication createAuthentication(UserIdentity userIdentity, final Request request) {
        return new KeycloakAuthentication(getAuthMethod(), userIdentity) {
            @Override
            public Authentication logout(ServletRequest servletRequest) {
                logoutCurrent((Request) servletRequest);
                return super.logout(servletRequest);
            }
        };
    }

    @Override
    public AdapterTokenStore createSessionTokenStore(Request request, KeycloakDeployment resolvedDeployment) {
        return new JettySessionTokenStore(request, resolvedDeployment, new JettyAdapterSessionStore(request));
    }

    @Override
    public JettyUserSessionManagement createSessionManagement(Request request) {
        return new JettyUserSessionManagement(new Jetty94SessionManager(request.getSessionHandler()));
    }

    @Override
    protected JettyRequestAuthenticator createRequestAuthenticator(Request request, JettyHttpFacade facade,
                                                                   KeycloakDeployment deployment, AdapterTokenStore tokenStore) {
        return new Jetty94RequestAuthenticator(facade, deployment, tokenStore, -1, request);
    }

}
