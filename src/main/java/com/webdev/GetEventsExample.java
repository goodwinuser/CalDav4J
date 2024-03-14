package com.webdev;

import com.webdev.enteties.CalDavCalendar;
import com.webdev.enteties.CalDavEvent;
import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.time.ZoneId;
import java.util.List;

public class GetEventsExample {
    public static void getEvents() {
        try {
            URL urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            CalDavCalendarClient caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", ZoneId.of("UTC+4"));

            //method GetCalendar return object of CalDavCalendar or throw Exception
            CalDavCalendar caldavCalendar = caldavClient.getCalendar();

            //list of events in calendar
            List<CalDavEvent> events = caldavCalendar.getEvents();

            for(CalDavEvent event : events){
                //information about event
                System.out.println(event.getSummary());
                System.out.println(event.getDescription());
                System.out.println(event.getLocation());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
