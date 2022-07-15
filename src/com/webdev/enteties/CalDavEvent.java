package com.webdev.enteties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CalDavEvent {

    private String uid;

    private java.util.Calendar start;

    private java.util.Calendar end;

    private java.util.Calendar created;

    private java.util.Calendar lastModified;

    private String location;

    private String summary;

    private String description;

    private List<String> members;

    public CalDavEvent()
    {
        this.created = java.util.Calendar.getInstance();
        this.lastModified = java.util.Calendar.getInstance();
        this.uid = UUID.randomUUID().toString();
        this.members = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMembers() {
        return members;
    }

    public Boolean addMember(String memberEmail) {
        this.lastModified = java.util.Calendar.getInstance();
        return this.members.add(memberEmail);
    }

    public Boolean removeMember(String memberEmail) {
        var status = this.members.remove(memberEmail);
        if (status) {
            this.lastModified = java.util.Calendar.getInstance();
        }
        return status;
    }

}
