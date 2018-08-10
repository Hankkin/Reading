//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hankkin.reading.domain;

import com.hankkin.reading.greendao.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class WebExplain implements Serializable {

    private static final long serialVersionUID = 8231916703833956791L;
    @Id
    private long id;
    private String key;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> means;

    public WebExplain() {
    }

    @Generated(hash = 660983462)
    public WebExplain(long id, String key, List<String> means) {
        this.id = id;
        this.key = key;
        this.means = means;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String var1) {
        this.key = var1;
    }

    public List<String> getMeans() {
        return this.means;
    }

    public void setMeans(List<String> var1) {
        this.means = var1;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
