package org.dragberry.eshop.model.common;

import java.util.List;

/**
 * This model represents working days
 * @author Drahun Maksim
 *
 */
public class WorkingDays {

    private List<WorkingDay> days;
    
    public WorkingDays(List<WorkingDay> days) {
        super();
        this.days = days;
    }

    public List<WorkingDay> getDays() {
        return days;
    }

    public void setDays(List<WorkingDay> days) {
        this.days = days;
    }
    
}
