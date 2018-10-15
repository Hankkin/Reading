package com.hankkin.reading.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * @author Hankkin
 * @date 2018/8/14
 */
@Entity
public class AccountBean implements Serializable{
    private static final long serialVersionUID = 6762415309876848914L;
    @Id
    private long id;
    private int icon;
    private String name;
    private String cate;
    private String number;
    private String password;
    private boolean isCollected;
    @Property(nameInDb = "createAt")
    private long createAt;
    @Property(nameInDb = "updateAt")
    private long updateAt;
    private String beizhu;




    @Generated(hash = 1956632468)
    public AccountBean(long id, int icon, String name, String cate, String number,
            String password, boolean isCollected, long createAt, long updateAt,
            String beizhu) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.cate = cate;
        this.number = number;
        this.password = password;
        this.isCollected = isCollected;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.beizhu = beizhu;
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
    public String getCate() {
        return this.cate;
    }
    public void setCate(String cate) {
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
    public String getBeizhu() {
        return this.beizhu;
    }
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

}
