package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class Page {
    @Drone
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected WebDriver browser;

    public abstract void verify();

    protected <U extends Page> U createPage(Class<U> clazz) {
        return Page.createPage(clazz, browser);
    }

    protected static <U extends Page> U createPage(Class<U> clazz, WebDriver browser) {
        U page = Graphene.createPageFragment(clazz, browser.findElement(By.cssSelector("html")));
        page.verify();
        return page;
    }


}
