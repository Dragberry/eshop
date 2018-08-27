package org.dragberry.eshop.model.common;

import lombok.Data;

/**
 * Data model for phone number
 * @author Drahun Maksim
 *
 */
@Data
public class Phone {

    public final static String VELCOM = "velcom";
    public final static String MTS = "mts";
    public final static String LIFE = "life";
    
    private String type;
    
    private String number;

    public Phone(String type, String number) {
        this.type = type;
        this.number = number;
    }

}
