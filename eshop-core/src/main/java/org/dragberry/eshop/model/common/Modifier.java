package org.dragberry.eshop.model.common;

public enum Modifier {

    PRIMARY("primary"), SECONDARY("secondary"), SUCCESS("success"), DANGER("danger"),
    WARNING("warning"), INFO("info"), LIGHT("light"), DARK("dark");
    
    private String value;
    
    private Modifier(String value) {
        this.value = value;
    }
    
    /**
     * Gets value
     * @return
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Return value as prefix-value
     * @param prefix
     * @return
     */
    public String getValue(String prefix) {
        return prefix + '-' + value;
    }
}
