package de.ahus1.lottery.adapter.dropwizard.state;

import de.ahus1.lottery.adapter.dropwizard.resource.DrawRequest;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class DrawResourceState {
    private final URI baseUrl;
    private Response response;
    private String accessToken;

    public DrawResourceState(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public DrawResourceState givenNoToken() {
        accessToken = null;
        return this;
    }

    public DrawResourceState givenToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public DrawResourceState whenOpened() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(baseUrl).path("/draw");
        DrawRequest request = new DrawRequest();
        request.setDate(LocalDate.parse("2015-01-01"));
        Invocation.Builder builder = target.request();
        if (accessToken != null) {
            builder.header("Authorization", "Bearer " + accessToken);
        }
        response = builder.post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        return this;
    }

    public void thenAccessDenied() {
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.UNAUTHORIZED);
    }

    public void thenForbidden() {
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.FORBIDDEN);
    }

}
