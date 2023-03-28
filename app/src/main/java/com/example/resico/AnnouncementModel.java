package com.example.resico;

public class AnnouncementModel {
    String announcementTitle;
    String announcementDate;
    String announcementMessage;

    public AnnouncementModel(String announcementTitle, String announcementDate, String announcementMessage) {
        this.announcementTitle = announcementTitle;
        this.announcementDate = announcementDate;
        this.announcementMessage = announcementMessage;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public String getAnnouncementDate() {
        return announcementDate;
    }

    public String getAnnouncementMessage() {
        return announcementMessage;
    }
}
