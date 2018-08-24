package org.dragberry.eshop.interceptor;

import java.time.DayOfWeek;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dragberry.eshop.model.common.Phone;
import org.dragberry.eshop.model.common.WorkingDay;
import org.dragberry.eshop.model.common.WorkingDays;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor injects common application information which can be used on all pages
 * @author Drahun Maksim
 *
 */
public class AppInfoInterceptor extends HandlerInterceptorAdapter {

    private List<Phone> phones = List.of(new Phone(Phone.MTS, "8033 375-90-80"), new Phone(Phone.VELCOM, "8029 375-90-80"));
    
    private WorkingDays workingDays = new WorkingDays(List.of(
            new WorkingDay(DayOfWeek.MONDAY, "9:00", "21:00", false),
            new WorkingDay(DayOfWeek.TUESDAY, "9:00", "21:00", false),
            new WorkingDay(DayOfWeek.WEDNESDAY, "9:00", "21:00", false),
            new WorkingDay(DayOfWeek.THURSDAY, "9:00", "21:00", false),
            new WorkingDay(DayOfWeek.FRIDAY, "9:00", "21:00", false),
            new WorkingDay(DayOfWeek.SATURDAY, "9:00", "21:00", false),
            new WorkingDay(DayOfWeek.SUNDAY, "9:00", "21:00", false)
            ));
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("phones", phones);
            modelAndView.addObject("workingDays", workingDays);
        }
    }
}
