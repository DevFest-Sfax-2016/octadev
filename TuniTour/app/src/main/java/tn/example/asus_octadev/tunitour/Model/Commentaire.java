package tn.example.asus_octadev.tunitour.Model;

/**
 * Created by ASUS-OCTADEV on 2016-08-22.
 */

public class Commentaire {
    String comment,user_uid,username;
    long created_at;

    public Commentaire() {
    }

    public Commentaire(String comment, String user_uid, long created_at) {
        this.comment = comment;
        this.user_uid = user_uid;
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
