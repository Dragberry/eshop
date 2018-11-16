package org.dragberry.eshop.model.comment;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductCommentRequest {

    private Long productId;
    
    private String name;
    
    private String email;
    
    private String text;
    
    private Integer mark;
    
    private String ip;
    
    private LocalDateTime date;
}
