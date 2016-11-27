package tn.example.asus_octadev.tunitour.Model;

/**
 * Created by ASUS-OCTADEV on 2016-11-27.
 */

public class Add {
    String id,description,image,lieux;
    String name,photo,like,comment,iduser;
    long created;

    public Add() {
    }

    public Add(String iduser, String description, String image, String lieux, long created,String like) {
        this.iduser = iduser;
        this.description = description;
        this.image = image;
        this.lieux = lieux;
        this.created = created;
        this.like=like;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLieux() {
        return lieux;
    }

    public void setLieux(String lieux) {
        this.lieux = lieux;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
