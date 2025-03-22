package com.emsi.recycleviewtp.beans;

public class Star {
    private int id;
    private String name;
    private String img;
    private int star;

    public Star() {
    }

    public Star(int id, String name, String img, int star) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.star = star;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
