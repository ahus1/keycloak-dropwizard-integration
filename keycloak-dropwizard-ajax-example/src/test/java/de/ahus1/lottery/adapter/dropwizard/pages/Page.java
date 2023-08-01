package de.ahus1.lottery.adapter.dropwizard.pages;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Page {

    private static Logger logger = Logger.getLogger(Page.class.getName());

    @Drone
    private WebDriver browser;

    public abstract void verify();

    protected <U extends Page> U createPage(Class<U> clazz) {
        return Page.createPage(clazz, browser);
    }

    protected static <U extends Page> U createPage(Class<U> clazz, WebDriver browser) {
        U page = Graphene.createPageFragment(clazz, browser.findElement(By.cssSelector("html")));
        try {
            page.verify();
        } catch (RuntimeException ex) {
            logger.log(Level.WARNING, "unable to verify page. title: " + browser.getTitle()
                    + ", source: " + browser.getPageSource(), ex);
            throw ex;
        }
        return page;
    }


}
