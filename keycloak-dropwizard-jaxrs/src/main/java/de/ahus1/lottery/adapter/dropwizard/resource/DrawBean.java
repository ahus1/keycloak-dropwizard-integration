package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.lottery.domain.Draw;
import org.keycloak.representations.IDToken;

public class DrawBean {
    private IDToken idToken;
    private Draw draw;

    public void setIdToken(IDToken idToken) {
        this.idToken = idToken;
    }

    public IDToken getIdToken() {
        return idToken;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    public Draw getDraw() {
        return draw;
    }
}
