package com.hankkin.reading.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class HotBean implements Serializable{
    private static final long serialVersionUID = 5492576431298717167L;
    @Id
    private long id;
    private String name;
    private String link;
    private int order;
    private int visible;

    @Generated(hash = 1116396725)
    public HotBean(long id, String name, String link, int order, int visible) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.order = order;
        this.visible = visible;
    }

    @Generated(hash = 1964254435)
    public HotBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
