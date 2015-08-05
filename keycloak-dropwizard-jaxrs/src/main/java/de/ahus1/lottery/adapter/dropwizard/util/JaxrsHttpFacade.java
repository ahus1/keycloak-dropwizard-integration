package de.ahus1.lottery.adapter.dropwizard.util;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.HttpFacade;
import org.keycloak.util.HostUtils;

import javax.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class JaxrsHttpFacade implements HttpFacade {

    protected final HttpServletRequest requestContext;
    protected final RequestFacade requestFacade = new RequestFacade();
    protected final ResponseFacade responseFacade = new ResponseFacade();
    protected KeycloakSecurityContext keycloakSecurityContext;

    public JaxrsHttpFacade(HttpServletRequest request) {
        this.requestContext = request;
    }

    protected class RequestFacade implements HttpFacade.Request {

        @Override
        public String getMethod() {
            return requestContext.getMethod();
        }

        @Override
        public String getURI() {
            return requestContext.getRequestURL().toString();
        }

        @Override
        public boolean isSecure() {
            return requestContext.isSecure();
        }

        @Override
        public String getQueryParamValue(String param) {
            return requestContext.getParameter(param);
        }

        @Override
        public Cookie getCookie(String cookieName) {
            javax.servlet.http.Cookie[] cookies = requestContext.getCookies();
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return new Cookie(cookie.getName(), cookie.getValue(), cookie.getVersion(), cookie.getDomain(), cookie.getPath());
                }
            }
            return null;
        }

        @Override
        public String getHeader(String name) {
            return requestContext.getHeader(name);
        }

        @Override
        public List<String> getHeaders(String name) {
            Enumeration<String> headers = requestContext.getHeaders(name);
            ArrayList list = new ArrayList();
            while (headers.hasMoreElements()) {
                list.add(headers.nextElement());
            }
            if (list.size() == 0) {
                return null;
            } else {
                return list;
            }
        }

        @Override
        public InputStream getInputStream() {
            try {
                return requestContext.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getRemoteAddr() {
            return HostUtils.getIpAddress();
        }
    }

    protected class ResponseFacade implements HttpFacade.Response {

        private javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.status(204);

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
        public void setCookie(String name, String value, String path, String domain, int maxAge, boolean secure, boolean httpOnly) {
            responseBuilder.cookie(new NewCookie(name, value, path, domain, null, maxAge, secure, httpOnly));
        }

        @Override
        public OutputStream getOutputStream() {
            // For now doesn't need to be supported
            throw new IllegalStateException("Not supported yet");
        }

        @Override
        public void sendError(int code, String message) {
            javax.ws.rs.core.Response response = responseBuilder.status(code).entity(message).build();
            throw new WebApplicationException(response);
        }

        @Override
        public void end() {
            javax.ws.rs.core.Response response = responseBuilder.build();

            throw new WebApplicationException(response);
        }
    }

    @Override
    public KeycloakSecurityContext getSecurityContext() {
        return keycloakSecurityContext;
    }

    public void setSecurityContext(KeycloakSecurityContext securityContext) {
        this.keycloakSecurityContext = securityContext;
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
    public X509Certificate[] getCertificateChain() {
        throw new IllegalStateException("Not supported yet");
    }

}
