package org.dragberry.eshop.model.comment;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductCommentResponse {

    private Long id;
    
    private Long productId;
    
    private String name;
    
    private String text;
    
    private Integer mark;
    
    private LocalDateTime date;
    
}
