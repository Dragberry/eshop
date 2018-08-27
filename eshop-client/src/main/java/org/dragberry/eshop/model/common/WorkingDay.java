package org.dragberry.eshop.model.common;

import java.time.DayOfWeek;

import lombok.Data;

/**
 * This class represents a working day
 * @author Drahun Maksim
 *
 */
@Data
public class WorkingDay {

    private DayOfWeek day;
    
    private String timeOpen;
    
    private String timeClose;
    
    private boolean dayOff;

    public WorkingDay(DayOfWeek day, String timeOpen, String timeClose, boolean dayOff) {
        this.day = day;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.dayOff = dayOff;
    }

}
