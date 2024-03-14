package com.webdev;

import com.webdev.enteties.CalDavEvent;
import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CreateEventExample {
    public static void createEvent() {
        try {
            URL urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            CalDavCalendarClient caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", ZoneId.of("UTC+4"));

            //event will start at 15:00
            LocalDateTime startTime = LocalDateTime.of(2022, 10, 3, 15, 0, 0);

            //event will end at 18:33
            LocalDateTime endTime = LocalDateTime.of(2022, 10, 3, 18, 33, 0);

            //create new event
            CalDavEvent newEvent = new CalDavEvent();
            //set information of event
            newEvent.setDescription("description in cart of event in calendar");
            newEvent.setSummary("title in cart of event in calendar");
            newEvent.setLocation("location in cart of event in calendar");
            //add email of member
            newEvent.addMember("example@gmail.com");
            //set start and end time
            newEvent.setStart(startTime);
            newEvent.setEnd(endTime);

            //method SaveEvent save event and return uuid of this event to calendar or throw Exception
            String uuid = caldavClient.saveEvent(newEvent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
