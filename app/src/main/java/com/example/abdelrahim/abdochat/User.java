package com.example.abdelrahim.abdochat;

/**
 * Created by Abd Elrahim on 12/24/2017.
 */

public class User {
    private String name;
    private String lmage;
    private String Status;
    private String thumb_image;

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public User() {
    }

    public User(String name, String Image, String Status,String thumb_image ) {
        this.name = name;
        this.lmage=Image;
        this.Status=Status;
        this.thumb_image=thumb_image;

    }

    public String getLmage() {
        return lmage;
    }

    public void setLmage(String lmage) {
        this.lmage = lmage;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatus() {

        return Status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
