package tn.example.asus_octadev.tunitour.Model;

/**
 * Created by ASUS-OCTADEV on 2016-10-04.
 */

public class Event {
    String name, date_debut, date_fin, description, lat, lon, type, like, images, target, website,id,useruid;

    Long time;

    public Event() {
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Event(String name, String date_debut, String date_fin, String description,
                 String lat, String lon, String type, String like, String images, String target, String website, Long time,String useruid) {
        this.name = name;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
        this.like = like;
        this.target = target;
        this.images = images;
        this.website = website;
        this.time = time;
        this.useruid=useruid;

    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}