package org.dragberry.eshop.navigation;

import java.util.LinkedList;
import java.util.List;

public class Breadcrumb {
    
    private final static String SLASH = "/";
    
    private LinkedList<BreadcrumbLink> links = new LinkedList<>();
    
    private StringBuilder fullRefernce = new StringBuilder();
    
    private Breadcrumb() {}
    
    public static Breadcrumb builder() {
    	return new Breadcrumb();
    }
    
    public Breadcrumb append(String name, String reference) {
    	return append(name, reference, false);
    }
    
    public Breadcrumb append(String name, String reference, boolean i18n) {
        if (!links.isEmpty()) {
            links.getLast().setActive(false);
        } 
        if (!reference.startsWith(SLASH)) {
    		fullRefernce.append(SLASH);
    	}
        fullRefernce.append(reference);
        links.add(new BreadcrumbLink(name, fullRefernce.toString(), true, i18n));
        return this;
    }
    
    public List<BreadcrumbLink> getLinks() {
        return links;
    }
}