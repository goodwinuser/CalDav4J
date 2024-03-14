package com.webdev.modules;

import com.webdev.enteties.CalDavEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Serializer {
    public static String serializeEventToString(CalDavEvent targetEvent, ZoneId zoneId, String login) {
        StringBuilder result = new StringBuilder("BEGIN:VCALENDAR\r\n");
        result.append("BEGIN:VEVENT").append("\r\n");
        result.append("DTEND:").append(serializeTimeToString(targetEvent.getEnd(), zoneId)).append("\r\n");
        result.append("DTSTART:").append(serializeTimeToString(targetEvent.getStart(), zoneId)).append("\r\n");
        result.append("CREATED:").append(serializeTimeToString(targetEvent.getCreated(), zoneId)).append("\r\n");
        result.append("LAST-MODIFIED:").append(serializeTimeToString(targetEvent.getLastModified(), zoneId)).append("\r\n");
        result.append("SEQUENCE:0").append("\r\n");
        result.append("SUMMARY:").append(targetEvent.getSummary()).append("\r\n");
        result.append("UID:").append(targetEvent.getUid()).append("\r\n");
        result.append("LOCATION:").append(targetEvent.getLocation()).append("\r\n");
        result.append("DESCRIPTION:").append(targetEvent.getDescription()).append("\r\n");
        if(targetEvent.getMembers().size() > 0) {
            result.append("ORGANIZER;CN=").append(login.split("@")[0]).append(":mailto:").append(login).append("\r\n");
            for (String pt : targetEvent.getMembers()) {
                result.append("ATTENDEE;PARTSTAT=NEEDS-ACTION;CN=")
                        .append(pt.split("@")[0])
                        .append(":mailto:")
                        .append(pt)
                        .append("\r\n");
            }
        }
        result.append("END:VEVENT").append("\r\n");
        result.append("END:VCALENDAR").append("\r\n");

        return result.toString();
    }

    private static String serializeTimeToString(LocalDateTime date, ZoneId zoneId) {
        int timeZoneOffsetInHours = LocalDateTime.now(zoneId).getHour() - LocalDateTime.now(ZoneId.of("UTC")).getHour();
        date = date.minusHours((long)timeZoneOffsetInHours);

        String year = String.valueOf(date.getYear());

        int mText = date.getMonthValue();
        String month = String.valueOf(mText);
        if (mText < 10) {
            month = "0" + month;
        }

        String day = String.valueOf(date.getDayOfMonth());
        if (date.getDayOfMonth() < 10) {
            day = "0" + day;
        }

        String hour = String.valueOf(date.getHour());
        if (date.getHour() < 10) {
            hour = "0" + hour;
        }

        String minute = String.valueOf(date.getMinute());
        if (date.getMinute() < 10) {
            minute = "0" + minute;
        }

        String second = String.valueOf(date.getSecond());
        if (date.getSecond() < 10) {
            second = "0" + second;
        }
        return year + month + day + "T" + hour + minute + second + "Z";
    }
}
