package com.webdev;

import com.webdev.enteties.CalDavEvent;
import com.webdev.services.CalDavCalendarClient;

import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

public class UpdateEventExample {
    public static void updateEvent(){
        try {
            var urlToCalendar = new URL("https://caldav.yandex.ru/calendars/login@yandex.ru/events-9999999999/");
            //create connection to calendar
            //for example you can use TimeZone.getDefault() or set your timezone use method getTimeZone()
            var caldavClient = new CalDavCalendarClient(urlToCalendar, "login@yandex.ru", "password", TimeZone.getTimeZone("GMT+4:00"));

            //event will start at 15:00
            var startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 15);
            startTime.set(Calendar.MINUTE, 00);

            //event will end at 18:33
            var endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, 18);
            endTime.set(Calendar.MINUTE, 33);

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
            var events = calendar.getEvents();
            var eventOpt = events.stream().filter(x->x.getUid().equals(uuid)).findFirst();

            if(eventOpt.isEmpty()){
                throw new Exception("Event not found");
            }

            //change endTime and delete all members
            var event = eventOpt.get();
            var eventEndTime = event.getEnd();
            //reduce endTime by 1 hour
            eventEndTime.set(Calendar.HOUR_OF_DAY, eventEndTime.get(Calendar.HOUR_OF_DAY) - 1);
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
