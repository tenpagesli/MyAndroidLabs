package com.my.myandroidlabs;

public class Message {

    private String content;
    private int messageDirection; // 1: send;  2: receive

    public Message() {
        this(null, -1);
    }

    public Message(String content, int messageDirection) {
        this.setContent(content);
        this.setMessageDirection(messageDirection);
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    public int getMessageDirection() {
        return messageDirection;
    }

    private void setMessageDirection(int messageDirection) {
        this.messageDirection = messageDirection;
    }

    @Override
    public String toString() {
        return this.getContent();
    }
}
