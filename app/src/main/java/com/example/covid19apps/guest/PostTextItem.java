package com.example.covid19apps.guest;

public class PostTextItem {
    private String postText;
    private int imageUser;
    private String time;

    public PostTextItem(String postText, int imageUser, String time) {
        this.postText = postText;
        this.imageUser = imageUser;
        this.time = time;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getImageUser() {
        return imageUser;
    }

    public void setImageUser(int imageUser) {
        this.imageUser = imageUser;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
