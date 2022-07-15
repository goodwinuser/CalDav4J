package com.webdev.enteties;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class CalDavCalendar {

    private String uid;

    private URL calendarUrl;

    private String displayName;

    private String owner;

    private String syncToken;

    private String color;

    private String ctag;

    private TimeZone timeZone;

    private Calendar lastModified;

    private List<CalDavEvent> events;

    public CalDavCalendar() {
        this.events = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public URL getCalendarUrl() {
        return calendarUrl;
    }

    public void setCalendarUrl(URL calendarUrl) {
        this.calendarUrl = calendarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSyncToken() {
        return syncToken;
    }

    public void setSyncToken(String syncToken) {
        this.syncToken = syncToken;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCtag() {
        return ctag;
    }

    public void setCtag(String ctag) {
        this.ctag = ctag;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public List<CalDavEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CalDavEvent> events) {
        this.events = events;
    }
}
