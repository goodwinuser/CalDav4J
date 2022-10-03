package com.webdev;

import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.time.ZoneId;

public class DeleteEventExample {
    public static void deleteEvent() {
        try {
            var urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            var caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", ZoneId.of("UTC+4"));

            //method RemoveEvent remove event from calendar and return uuid of this deleted event to calendar or throw Exception
            var uuid = caldavClient.removeEvent("85d27377-155c-470e-8b2a-a187d7e95373");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
