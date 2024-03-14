package com.webdev.modules;

import com.webdev.enteties.CalDavCalendar;
import com.webdev.enteties.CalDavEvent;
import com.webdev.exceptions.XMLDataException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Deserializer {
    public static CalDavCalendar deserializeCalendar(String source, CalDavCalendar calendar) throws XMLDataException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(source)));
        } catch (SAXException | ParserConfigurationException exception) {
            throw new XMLDataException("Wrong calendar xml data");
        }

        String content = document.getElementsByTagName("href").item(0).getTextContent();
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

        NodeList events = document.getElementsByTagName("C:calendar-data");
        ArrayList<CalDavEvent> deserializedEvents = new ArrayList<>();
        for (int i = 0; i < events.getLength(); i++) {
            deserializedEvents.add(
                    deserializeEvent(events.item(i).getTextContent(), calendar.getZoneId())
            );
        }
        calendar.setEvents(deserializedEvents);

        return calendar;
    }


    public static CalDavEvent deserializeEvent(String source, ZoneId zoneId) {
        CalDavEvent targetEvent = new CalDavEvent();

        List<String> dirtyStrings = Arrays
                .stream(source.split("\n"))
                .collect(Collectors.toList());

        List<String> items = Arrays
                .stream(source.split("\n"))
                .skip(dirtyStrings.indexOf("BEGIN:VEVENT"))
                .map(x -> x.replaceAll(";\\S*:", ":"))
                .collect(Collectors.toList());


        Optional<String> item = items.stream()
                .filter(x -> x.contains("DTSTART:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setStart(
                    getDateTime(
                            item.get()
                                    .replace("DTSTART:", ""),
                            zoneId
                    )
            );
        } else {
            targetEvent.setStart(LocalDateTime.MIN);
        }


        item = items.stream()
                .filter(x -> x.contains("DTEND:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setEnd(
                    getDateTime(
                            item.get()
                                    .replace("DTEND:", ""),
                            zoneId
                    )
            );
        } else {
            targetEvent.setEnd(LocalDateTime.MIN);
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
                    getDateTime(
                            item.get()
                                    .replace("CREATED:", ""),
                            zoneId
                    )
            );
        } else {
            targetEvent.setCreated(LocalDateTime.MIN);
        }

        item = items.stream()
                .filter(x -> x.contains("LAST-MODIFIED:"))
                .findFirst();
        if (item.isPresent()) {
            targetEvent.setLastModified(
                    getDateTime(
                            item.get()
                                    .replace("LAST-MODIFIED:", ""),
                            zoneId
                    )
            );
        } else {
            targetEvent.setLastModified(LocalDateTime.MIN);
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

        List<String> allItems = items.stream()
                .filter(x -> x.contains("ATTENDEE:"))
                .map(x -> x.replace("ATTENDEE:", ""))
                .collect(Collectors.toList());
        if (allItems.size() > 0) {
            for (String element : allItems) {
                String[] parts = element.split(":");

                if (parts.length == 1) {
                    targetEvent.addMember(parts[0]);
                }
            }
        }

        return targetEvent;
    }

    private static LocalDateTime getDateTime(String time, ZoneId zoneId) {
        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(4, 6));
        int day = Integer.parseInt(time.substring(6, 8));

        int hour = Integer.parseInt(time.substring(9, 11));
        int minutes = Integer.parseInt(time.substring(11, 13));
        int seconds = Integer.parseInt(time.substring(13, 15));

        int timeZoneOffsetInHours = LocalDateTime.now(zoneId).getHour() - LocalDateTime.now(ZoneId.of("UTC")).getHour();

        return LocalDateTime.of(year, month, day, hour + timeZoneOffsetInHours, minutes, seconds);
    }
}
