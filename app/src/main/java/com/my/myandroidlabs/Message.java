package com.my.myandroidlabs;

import java.util.Objects;

public class Message {

    long id; // the id that will be stored in database
    private String content;
    private boolean isSent; //

    public Message(long id, String content, boolean isSent) {
        this.id = id;
        this.content = content;
        this.isSent = isSent;
    }

    public Message() {
        this(-1, null, false);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    public boolean isSent() {
        return isSent;
    }

    private void setSent(boolean sent) {
        isSent = sent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return getId() == message.getId() &&
                isSent() == message.isSent() &&
                Objects.equals(getContent(), message.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), isSent());
    }

    @Override
    public String toString() {
        return content;
    }
}
