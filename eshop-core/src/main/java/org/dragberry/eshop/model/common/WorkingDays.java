package org.dragberry.eshop.model.common;

import java.util.List;

import lombok.Data;

/**
 * This model represents working days
 * @author Drahun Maksim
 *
 */
@Data
public class WorkingDays {

    private List<WorkingDay> days;
    
    private String description;
    
    public WorkingDays(List<WorkingDay> days, String description) {
        this.days = days;
        this.description = description;
    }
}
