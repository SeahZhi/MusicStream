package com.example.musicstream;

public class Song {

    private String id;
    private String title;
    private String fileLink;
    private String artiste;
    private double songLength;
    private int drawable;

    public Song(String id, String title, String fileLink, String artiste, double songLength, int drawable) {
        this.id = id;
        this.title = title;
        this.fileLink = fileLink;
        this.artiste = artiste;
        this.songLength = songLength;
        this.drawable = drawable;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFileLink() {
        return fileLink;
    }

    public String getArtiste() {
        return artiste;
    }

    public double getSongLength() {
        return songLength;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public void setSongLength(double songLength) {
        this.songLength = songLength;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}

