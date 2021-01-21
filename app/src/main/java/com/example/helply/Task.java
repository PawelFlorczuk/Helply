package com.example.helply;

import java.util.UUID;

public class Task {
    private UUID uuid;
    private String address;
    private String contactPhoneNumber;
    private String description;
    private User user;
    private String clientMessage;
    private boolean isTaken ;

    public Task(UUID uuid, String address, String contactPhoneNumber, String description, User user, String clientMessage, boolean isTaken) {
        this.uuid = uuid;
        this.address = address;
        this.contactPhoneNumber = contactPhoneNumber;
        this.description = description;
        this.user = user;
        this.clientMessage = clientMessage;
        this.isTaken = isTaken;
    }

    public Task() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    @Override
    public String toString() {
        return "Task{" +
                "address='" + address + '\'' +
                ", contactPhoneNumber='" + contactPhoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", clientMessage='" + clientMessage + '\'' +
                ", isTaken=" + isTaken +
                '}';
    }
}

