package de.ahus1.lottery.adapter.dropwizard.resource;

import io.dropwizard.views.common.View;

public class DrawView extends View {

    private final DrawBean bean;

    public DrawView(DrawBean aBean) {
        super("draw.ftl");
        bean = aBean;
    }

    public DrawBean getBean() {
        return bean;
    }
}
