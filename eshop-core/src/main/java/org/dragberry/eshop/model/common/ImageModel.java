package org.dragberry.eshop.model.common;

import lombok.Data;

@Data
public class ImageModel {

    private Long id;
    
    private String type;
    
    private String name;
    
    private byte[] content;
}
