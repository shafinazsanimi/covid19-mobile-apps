package com.example.covid19apps.guest;

public class PostVideoItem {
    private String videoURL;
    private int userImage;
    private String time;

    public PostVideoItem(String videoURL, int userImage, String time) {
        this.videoURL = videoURL;
        this.userImage = userImage;
        this.time = time;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
