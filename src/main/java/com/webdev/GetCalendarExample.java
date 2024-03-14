package com.webdev;

import com.webdev.enteties.CalDavCalendar;
import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.time.ZoneId;

public class GetCalendarExample {
    public static void getCalendar() {
        try {
            URL urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            CalDavCalendarClient caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", ZoneId.of("UTC+4"));

            //method GetCalendar return object of CalDavCalendar or throw Exception
            CalDavCalendar caldavCalendar = caldavClient.getCalendar();

            //information about calendar
            System.out.println(caldavCalendar.getDisplayName());
            System.out.println(caldavCalendar.getZoneId());
            System.out.println(caldavCalendar.getUid());
            System.out.println(caldavCalendar.getColor());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
