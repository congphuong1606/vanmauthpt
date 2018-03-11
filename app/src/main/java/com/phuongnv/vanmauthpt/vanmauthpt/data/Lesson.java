package com.phuongnv.vanmauthpt.vanmauthpt.data;

/**
 * Created by admin on 3/11/2018.
 */

public class Lesson {
    String id;
    String name;
    String des;
    String chapter;
    String path;

    public Lesson(String id, String name, String des, String chapter, String path) {
        this.id = id;
        this.name = name;
        this.des = des;
        this.chapter = chapter;
        this.path = path;
    }

    public Lesson() {
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
