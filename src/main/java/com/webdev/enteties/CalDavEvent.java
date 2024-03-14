package com.webdev.enteties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalDavEvent {
    private String uid;

    private LocalDateTime start;

    private LocalDateTime end;

    private LocalDateTime created;

    private LocalDateTime lastModified;

    private String location;

    private String summary;

    private String description;

    private List<String> members;

    public CalDavEvent()
    {
        this.created = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.uid = UUID.randomUUID().toString();
        this.members = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
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

    public void setSummary(String summary) { this.summary = summary; }

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
        this.lastModified = LocalDateTime.now();
        return this.members.add(memberEmail);
    }

    public Boolean removeMember(String memberEmail) {
        boolean status = this.members.remove(memberEmail);
        if (status) {
            this.lastModified = LocalDateTime.now();
        }
        return status;
    }

}
