package com.webdev;

import com.webdev.enteties.CalDavEvent;
import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UpdateEventExample {
    public static void updateEvent(){
        try {
            var urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            var caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", ZoneId.of("UTC+4"));

            //event will start at 15:00
            var startTime = LocalDateTime.of(2022, 10, 3, 15, 0, 0);

            //event will end at 18:33
            var endTime = LocalDateTime.of(2022, 10, 3, 18, 33, 0);

            //create new event
            var newEvent = new CalDavEvent();
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
            var uuid = caldavClient.saveEvent(newEvent);

            var calendar = caldavClient.getCalendar();
            var eventOpt = calendar.getEvent(uuid);

            if(eventOpt.isEmpty()){
                throw new Exception("Event not found");
            }

            //change endTime and delete all members
            var event = eventOpt.get();
            var eventEndTime = event.getEnd();
            //reduce endTime by 1 hour
            eventEndTime = eventEndTime.minusHours(1);
            event.setEnd(eventEndTime);
            //delete all members
            event.getMembers().clear();

            //update event
            caldavClient.updateEvent(uuid,event);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
