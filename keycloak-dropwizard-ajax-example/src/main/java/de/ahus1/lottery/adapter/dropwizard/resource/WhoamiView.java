package de.ahus1.lottery.adapter.dropwizard.resource;

import io.dropwizard.views.common.View;

public class WhoamiView extends View {

    private final WhoamiBean bean;

    public WhoamiView(WhoamiBean aBean) {
        super("whoami.ftl");
        bean = aBean;
    }

    public WhoamiBean getBean() {
        return bean;
    }
}
