package com.webdev;

import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.time.ZoneId;

public class GetCalendarExample {
    public static void getCalendar() {
        try {
            var urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            var caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", ZoneId.of("UTC+4"));

            //method GetCalendar return object of CalDavCalendar or throw Exception
            var caldavCalendar = caldavClient.getCalendar();

            //information about calendar
            caldavCalendar.getDisplayName();
            caldavCalendar.getZoneId();
            caldavCalendar.getUid();
            caldavCalendar.getColor();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
