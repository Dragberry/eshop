package org.dragberry.eshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BreadcrumbLink {

    private String name;
    
    private String url;
    
    private boolean active;
}
