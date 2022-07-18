package com.webdev.modules;

import com.webdev.enteties.CalDavEvent;

import java.util.Calendar;
import java.util.TimeZone;

public class Serializer {
    public static String serializeEventToString(CalDavEvent targetEvent, TimeZone timeZone, String login) {
        var result = new StringBuilder("BEGIN:VCALENDAR\r\n");
        result.append("BEGIN:VEVENT").append("\r\n");
        result.append("DTEND:").append(serializeTimeToString(targetEvent.getEnd(), timeZone)).append("\r\n");
        result.append("DTSTART:").append(serializeTimeToString(targetEvent.getStart(), timeZone)).append("\r\n");
        result.append("CREATED:").append(serializeTimeToString(targetEvent.getCreated(), timeZone)).append("\r\n");
        result.append("LAST-MODIFIED:").append(serializeTimeToString(targetEvent.getLastModified(), timeZone)).append("\r\n");
        result.append("SEQUENCE:0").append("\r\n");
        result.append("SUMMARY:").append(targetEvent.getSummary()).append("\r\n");
        result.append("UID:").append(targetEvent.getUid()).append("\r\n");
        result.append("LOCATION:").append(targetEvent.getLocation()).append("\r\n");
        result.append("DESCRIPTION:").append(targetEvent.getDescription()).append("\r\n");
        if(targetEvent.getMembers().size() > 0) {
            result.append("ORGANIZER;CN=").append(login.split("@")[0]).append(":mailto:").append(login).append("\r\n");
            for (var pt : targetEvent.getMembers()) {
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

    private static String serializeTimeToString(Calendar dt, TimeZone timeZone) {

        //convert local time to UTC format
        var timeZoneOffsetInHours = timeZone.getRawOffset()/(60 * 60 * 1000);
        dt.set(Calendar.HOUR_OF_DAY, dt.get(Calendar.HOUR_OF_DAY) - timeZoneOffsetInHours);

        String year = String.valueOf(dt.get(Calendar.YEAR));

        int mText = dt.get(Calendar.MONTH) + 1;
        String month = String.valueOf(mText);
        if (mText < 10) {
            month = "0" + month;
        }

        String day = String.valueOf(dt.get(Calendar.DAY_OF_MONTH));
        if (dt.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + day;
        }

        String hour = String.valueOf(dt.get(Calendar.HOUR_OF_DAY));
        if (dt.get(Calendar.HOUR_OF_DAY) < 10) {
            hour = "0" + hour;
        }

        String minute = String.valueOf(dt.get(Calendar.MINUTE));
        if (dt.get(Calendar.MINUTE) < 10) {
            minute = "0" + minute;
        }

        String second = String.valueOf(dt.get(Calendar.SECOND));
        if (dt.get(Calendar.SECOND) < 10) {
            second = "0" + second;
        }
        return year + month + day + "T" + hour + minute + second + "Z";
    }
}
