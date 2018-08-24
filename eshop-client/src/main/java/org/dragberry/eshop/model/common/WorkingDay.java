package org.dragberry.eshop.model.common;

import java.time.DayOfWeek;

/**
 * This class represents a working day
 * @author Drahun Maksim
 *
 */
public class WorkingDay {

    private DayOfWeek day;
    
    private String timeOpen;
    
    private String timeClose;
    
    private boolean dayOff;

    public WorkingDay(DayOfWeek day, String timeOpen, String timeClose, boolean dayOff) {
        super();
        this.day = day;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.dayOff = dayOff;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(String timeOpen) {
        this.timeOpen = timeOpen;
    }

    public String getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(String timeClose) {
        this.timeClose = timeClose;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }
    
}
