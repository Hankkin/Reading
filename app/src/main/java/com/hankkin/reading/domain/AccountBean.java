package com.hankkin.reading.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author Hankkin
 * @date 2018/8/14
 */
@Entity
public class AccountBean implements Serializable{
    private static final long serialVersionUID = 6762415309876844914L;
    @Id
    private long id;
    private int icon;
    private String name;
    private int cate;
    private String number;
    private String password;
    private boolean isCollected;
    @Property(nameInDb = "createAt")
    private long createAt;
    @Property(nameInDb = "updateAt")
    private long updateAt;
    @Generated(hash = 172891373)
    public AccountBean(long id, int icon, String name, int cate, String number,
            String password, boolean isCollected, long createAt, long updateAt) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.cate = cate;
        this.number = number;
        this.password = password;
        this.isCollected = isCollected;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    @Generated(hash = 1267506976)
    public AccountBean() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getIcon() {
        return this.icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCate() {
        return this.cate;
    }
    public void setCate(int cate) {
        this.cate = cate;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getIsCollected() {
        return this.isCollected;
    }
    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }
    public long getCreateAt() {
        return this.createAt;
    }
    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
    public long getUpdateAt() {
        return this.updateAt;
    }
    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }
}
