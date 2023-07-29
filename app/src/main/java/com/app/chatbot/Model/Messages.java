package com.app.chatbot.Model;

public class Messages {
    private String nama, uid ,lastChat, chatKey;
    private int unseenChat;
    public Messages(String nama, String uid ,String lastChat, int unseenChat, String chatKey) {
        this.nama = nama;
        this.uid = uid;
        this.lastChat = lastChat;
        this.unseenChat = unseenChat;
        this.chatKey = chatKey;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public void setUnseenChat(int unseenChat) {
        this.unseenChat = unseenChat;
    }

    public String getNama() {
        return nama;
    }

    public String getUid(){
        return uid;
    }
    public String getLastChat() {
        return lastChat;
    }
    public String getChatkey() { return chatKey; }
    public int getUnseenChat() {
        return unseenChat;
    }

    public Messages(){}
}
