package de.ahus1.lottery.adapter.dropwizard.state;

import de.ahus1.lottery.adapter.dropwizard.resource.DrawRequest;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class DrawRessourceState {
    private final URI baseUrl;
    private Response response;
    private String accessToken;

    public DrawRessourceState(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public DrawRessourceState givenNoToken() {
        accessToken = null;
        return this;
    }

    public DrawRessourceState givenToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public DrawRessourceState whenOpened() {
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
