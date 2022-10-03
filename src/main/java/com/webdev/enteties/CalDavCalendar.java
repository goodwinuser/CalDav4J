package com.webdev.enteties;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    private ZoneId zoneId;

    private LocalDateTime lastModified;

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

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public List<CalDavEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CalDavEvent> events) {
        this.events = events;
    }
}