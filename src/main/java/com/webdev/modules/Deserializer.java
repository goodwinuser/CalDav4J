package com.webdev.modules;

import com.webdev.enteties.CalDavCalendar;
import com.webdev.enteties.CalDavEvent;
import com.webdev.exceptions.XMLDataException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class Deserializer {
    public static CalDavCalendar DeserializeCalendar(String source, CalDavCalendar calendar) throws XMLDataException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(source)));
        }
        catch(SAXException | ParserConfigurationException exception){
            throw new XMLDataException("Wrong calendar xml data");
        }

        var content = document.getElementsByTagName("href").item(0).getTextContent();
        if (content != null && !content.equals("")) {
            calendar.setUid(content);
        } else {
            calendar.setUid("");
        }

        content = document.getElementsByTagName("D:displayname").item(0).getTextContent();
        if (content != null && !content.equals("")) {
            calendar.setDisplayName(content);
        } else {
            calendar.setDisplayName("");
        }

        content = document.getElementsByTagName("calendar-color").item(0).getTextContent();
        if (content != null && !content.equals("")) {
            calendar.setColor(content);
        } else {
            calendar.setColor("");
        }

        content = document.getElementsByTagName("D:owner").item(0).getTextContent();
        if (content != null && !content.equals("")) {
            calendar.setOwner(content);
        } else {
            calendar.setOwner("");
        }

        content = document.getElementsByTagName("D:sync-token").item(0).getTextContent();
        if (content != null && !content.equals("")) {
            calendar.setSyncToken(content.replace("sync-token:", ""));
        } else {
            calendar.setSyncToken("");
        }

        content = document.getElementsByTagName("getctag").item(0).getTextContent();
        if (content != null && !content.equals("")) {
            calendar.setCtag(content);
        } else {
            calendar.setCtag("");
        }

        var events = document.getElementsByTagName("C:calendar-data");
        var deserializedEvents = new ArrayList<CalDavEvent>();
        for (int i = 0; i < events.getLength(); i++) {
            deserializedEvents.add(
                    DeserializeEvent(events.item(i).getTextContent(), calendar.getTimeZone())
            );
        }
        calendar.setEvents(deserializedEvents);

        return calendar;
    }


    public static CalDavEvent DeserializeEvent(String source, TimeZone timeZone) {

        CalDavEvent targetEvent = new CalDavEvent();

        var items = Arrays
                .stream(source.split("\n"))
                .dropWhile(x -> !x.equals("BEGIN:VEVENT"))
                .map(x -> x.replaceAll(";\\S*:", ":")).toList();


        var item = items.stream()
                .filter(x -> x.contains("DTSTART:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setStart(
                    GetDateTime(
                            item.get()
                                    .replace("DTSTART:", ""),
                            timeZone
                    )
            );
        } else {
            targetEvent.setStart(new GregorianCalendar());
        }


        item = items.stream()
                .filter(x -> x.contains("DTEND:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setEnd(
                    GetDateTime(
                            item.get()
                                    .replace("DTEND:", ""),
                            timeZone
                    )
            );
        } else {
            targetEvent.setEnd(new GregorianCalendar());
        }

        item = items.stream()
                .filter(x -> x.contains("SUMMARY:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setSummary(
                    item.get()
                            .replace("SUMMARY:", "")
            );
        } else {
            targetEvent.setSummary("");
        }

        item = items.stream()
                .filter(x -> x.contains("UID:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setUid(
                    item.get()
                            .replace("UID:", "")
            );
        } else {
            targetEvent.setUid("");
        }

        item = items.stream()
                .filter(x -> x.contains("CREATED:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setCreated(
                    GetDateTime(
                            item.get()
                                    .replace("CREATED:", ""),
                            timeZone
                    )
            );
        } else {
            targetEvent.setCreated(new GregorianCalendar());
        }

        item = items.stream()
                .filter(x -> x.contains("LAST-MODIFIED:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setLastModified(
                    GetDateTime(
                            item.get()
                                    .replace("LAST-MODIFIED:", ""),
                            timeZone
                    )
            );
        } else {
            targetEvent.setLastModified(new GregorianCalendar());
        }

        item = items.stream()
                .filter(x -> x.contains("LOCATION:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setLocation(
                    item.get()
                            .replace("LOCATION:", "")
            );
        } else {
            targetEvent.setLocation("");
        }

        item = items.stream()
                .filter(x -> x.contains("DESCRIPTION:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setDescription(
                    item.get()
                            .replace("DESCRIPTION:", "")
            );
        } else {
            targetEvent.setDescription("");
        }

        var allItems = items.stream()
                .filter(x -> x.contains("ATTENDEE;PARTSTAT=NEEDS-ACTION;CN="))
                .map(x -> x.replace("ATTENDEE;PARTSTAT=NEEDS-ACTION;CN=", ""))
                .collect(Collectors.toList());
        if (allItems.size() > 0) {
            for (var element : allItems) {
                var parts = element.split(":");

                if (parts.length == 3) {
                    targetEvent.addMember(parts[2]);
                }
            }
        }

        return targetEvent;
    }

    private static java.util.Calendar GetDateTime(String time, TimeZone timeZone) {

        var result = new GregorianCalendar(timeZone);

        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(4, 6));
        int day = Integer.parseInt(time.substring(6, 8));

        int hour = Integer.parseInt(time.substring(9, 11));
        int minutes = Integer.parseInt(time.substring(11, 13));
        int seconds = Integer.parseInt(time.substring(13, 15));


        result.set(GregorianCalendar.SECOND, seconds);
        result.set(GregorianCalendar.MINUTE, minutes);
        result.set(GregorianCalendar.HOUR_OF_DAY, hour);
        result.set(GregorianCalendar.DAY_OF_MONTH, day);
        result.set(GregorianCalendar.MONTH, month);
        result.set(GregorianCalendar.YEAR, year);

        //convert UTC format to local time
        Integer timeZoneOffsetInHours = timeZone.getOffset(result.getTimeInMillis()) / (60 * 60 * 1000);
        result.set(java.util.Calendar.HOUR_OF_DAY, result.get(java.util.Calendar.HOUR_OF_DAY) + timeZoneOffsetInHours);

        return result;
    }
}
