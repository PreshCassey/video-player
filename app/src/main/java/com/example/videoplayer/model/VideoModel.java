package com.example.videoplayer.model;

public class VideoModel {
    public String fileName;
    public String filePath;
    private int imageResource;


    public VideoModel(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getImageResource() {
        return imageResource;
    }
}