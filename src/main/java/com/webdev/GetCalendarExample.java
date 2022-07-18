package com.webdev;

import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.util.TimeZone;

public class GetCalendarExample {
    public static void getCalendar() {
        try {
            var urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            var caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", TimeZone.getTimeZone("GMT+4:00"));

            //method GetCalendar return object of CalDavCalendar or throw Exception
            var caldavCalendar = caldavClient.getCalendar();

            //information about calendar
            caldavCalendar.getDisplayName();
            caldavCalendar.getTimeZone();
            caldavCalendar.getUid();
            caldavCalendar.getColor();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
