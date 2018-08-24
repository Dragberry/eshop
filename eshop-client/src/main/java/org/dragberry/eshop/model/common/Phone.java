package org.dragberry.eshop.model.common;

/**
 * Data model for phone number
 * @author Drahun Maksim
 *
 */
public class Phone {

    public final static String VELCOM = "velcom";
    public final static String MTS = "mts";
    public final static String LIFE = "life";
    
    private String type;
    
    private String number;

    public Phone(String type, String number) {
        super();
        this.type = type;
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    
    
}
