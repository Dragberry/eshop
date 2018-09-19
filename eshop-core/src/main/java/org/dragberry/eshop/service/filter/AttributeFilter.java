package org.dragberry.eshop.service.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AttributeFilter {

    private String name;
    
    private AttributeFilterAction action;
}
