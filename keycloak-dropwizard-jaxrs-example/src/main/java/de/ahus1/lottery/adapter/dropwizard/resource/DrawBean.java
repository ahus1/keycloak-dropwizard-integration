package de.ahus1.lottery.adapter.dropwizard.resource;

import de.ahus1.lottery.domain.Draw;
import org.keycloak.representations.IDToken;

public class DrawBean {
    private String name;
    private Draw draw;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    public Draw getDraw() {
        return draw;
    }
}
