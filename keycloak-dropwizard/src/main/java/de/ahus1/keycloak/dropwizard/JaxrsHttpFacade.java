package de.ahus1.keycloak.dropwizard;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OIDCHttpFacade;
import org.keycloak.adapters.spi.AuthenticationError;
import org.keycloak.adapters.spi.LogoutError;
import org.keycloak.common.util.HostUtils;

import javax.security.cert.X509Certificate;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 * @author <a href="mailto:alexander.schwartz@gmx.net">Alexander Schwartz</a> (adoption for Dropwizard)
 */
public class JaxrsHttpFacade implements OIDCHttpFacade {

    private final ContainerRequestContext requestContext;
    private final SecurityContext securityContext;
    private final RequestFacade requestFacade = new RequestFacade();
    private final ResponseFacade responseFacade = new ResponseFacade();
    private boolean responseFinished;

    public JaxrsHttpFacade(ContainerRequestContext containerRequestContext, SecurityContext securityContext) {
        this.requestContext = containerRequestContext;
        this.securityContext = securityContext;
    }

    @Override
    public KeycloakSecurityContext getSecurityContext() {
        HttpServletRequest httpServletRequest =
                (HttpServletRequest) requestContext.getProperty(HttpServletRequest.class.getName());
        if (httpServletRequest != null) {
            return (KeycloakSecurityContext) httpServletRequest.getAttribute(KeycloakSecurityContext.class.getName());
        } else {
            return null;
        }
    }

    protected class RequestFacade implements Request {

        private InputStream inputStream;

        @Override
        public String getMethod() {
            return requestContext.getMethod();
        }

        @Override
        public String getURI() {
            return requestContext.getUriInfo().getRequestUri().toString();
        }

        @Override
        public String getRelativePath() {
            return requestContext.getUriInfo().getPath();
        }

        @Override
        public boolean isSecure() {
            return securityContext.isSecure();
        }

        @Override
        public String getFirstParam(String param) {
            return getQueryParamValue(param);
        }

        @Override
        public String getQueryParamValue(String param) {
            MultivaluedMap<String, String> queryParams = requestContext.getUriInfo().getQueryParameters();
            if (queryParams == null) {
                return null;
            }
            return queryParams.getFirst(param);
        }

        @Override
        public Cookie getCookie(String cookieName) {
            Map<String, jakarta.ws.rs.core.Cookie> cookies = requestContext.getCookies();
            if (cookies == null) {
                return null;
            }
            jakarta.ws.rs.core.Cookie cookie = cookies.get(cookieName);
            if (cookie == null) {
                return null;
            }
            return new Cookie(cookie.getName(), cookie.getValue(), cookie.getVersion(), cookie.getDomain(),
                    cookie.getPath());
        }

        @Override
        public String getHeader(String name) {
            return requestContext.getHeaderString(name);
        }

        @Override
        public List<String> getHeaders(String name) {
            MultivaluedMap<String, String> headers = requestContext.getHeaders();
            return (headers == null) ? null : headers.get(name);
        }

        @Override
        public InputStream getInputStream() {
            return getInputStream(false);
        }

        @Override
        public InputStream getInputStream(boolean buffered) {
            if (inputStream != null) {
                return inputStream;
            }

            if (buffered) {
                inputStream = new BufferedInputStream(requestContext.getEntityStream());
                return inputStream;
            }

            return requestContext.getEntityStream();
        }

        @Override
        public String getRemoteAddr() {
            // TODO: implement properly
            return HostUtils.getIpAddress();
        }

        @Override
        public void setError(AuthenticationError error) {
            requestContext.setProperty(AuthenticationError.class.getName(), error);
        }

        @Override
        public void setError(LogoutError error) {
            requestContext.setProperty(LogoutError.class.getName(), error);
        }
    }

    protected class ResponseFacade implements Response {

        private jakarta.ws.rs.core.Response.ResponseBuilder responseBuilder =
                jakarta.ws.rs.core.Response.status(Status.NO_CONTENT);

        @Override
        public void setStatus(int status) {
            responseBuilder.status(status);
        }

        @Override
        public void addHeader(String name, String value) {
            responseBuilder.header(name, value);
        }

        @Override
        public void setHeader(String name, String value) {
            responseBuilder.header(name, value);
        }


        @Override
        public void resetCookie(String name, String path) {
            responseBuilder.cookie(new NewCookie(name, "", path, null, null, 0, false));
        }

        @Override
        public void setCookie(String name, String value, String path, String domain, int maxAge, boolean secure,
                              boolean httpOnly) {
            responseBuilder.cookie(new NewCookie(name, value, path, domain, null, maxAge, secure, httpOnly));
        }

        @Override
        public OutputStream getOutputStream() {
            // For now doesn't need to be supported
            throw new IllegalStateException("Not supported yet");
        }

        @Override
        public void sendError(int code) {
            jakarta.ws.rs.core.Response response = responseBuilder.status(code).build();
            requestContext.abortWith(response);
            responseFinished = true;
        }

        @Override
        public void sendError(int code, String message) {
            jakarta.ws.rs.core.Response response = responseBuilder.status(code).entity(message).build();
            requestContext.abortWith(response);
            responseFinished = true;
        }

        @Override
        public void end() {
            jakarta.ws.rs.core.Response response = responseBuilder.build();
            requestContext.abortWith(response);
            responseFinished = true;
        }
    }

    @Override
    public Request getRequest() {
        return requestFacade;
    }

    @Override
    public Response getResponse() {
        return responseFacade;
    }

    @Override
    @SuppressForbidden(reason = "required by API")
    public X509Certificate[] getCertificateChain() {
        throw new IllegalStateException("Not supported yet");
    }

}
