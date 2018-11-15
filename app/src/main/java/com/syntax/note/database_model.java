package com.syntax.note;

/**
 * Created by USER on 17-05-2018.
 */

public class database_model {
    String id;
    String name;
    String time;
    String status;
    String title;
    public database_model(String id,String name,String time,String status,String titles)
    {
        this.id=id;
        this.name=name;
        this.time=time;
        this.status=status;
        this.title=titles;
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
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
