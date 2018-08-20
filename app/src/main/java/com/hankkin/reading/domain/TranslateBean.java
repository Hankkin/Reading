package com.hankkin.reading.domain;

import com.hankkin.reading.greendao.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.hankkin.reading.greendao.DaoSession;
import com.hankkin.reading.greendao.WebExplainDao;
import com.hankkin.reading.greendao.TranslateBeanDao;

/**
 * Created by huanghaijie on 2018/8/10.
 */
@Entity
public class TranslateBean implements Serializable{
    private static final long serialVersionUID = 1475027561504418347L;
    @Id
    private long id;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> translations;
    private String query;
    private int errorCode;
    private String usPhonetic;
    private String phonetic;
    private String from;
    private String to;
    private String le;
    private String deeplink;
    private String dictDeeplink;
    private String ukPhonetic;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> explains;
    @ToMany(referencedJoinProperty = "id")
    private List<WebExplain> webExplains;
    private String speakUrl;
    private String UKSpeakUrl;
    private String USSpeakUrl;
    private String resultSpeakUrl;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 19335675)
    private transient TranslateBeanDao myDao;
    @Generated(hash = 1334799272)
    public TranslateBean(long id, List<String> translations, String query,
            int errorCode, String usPhonetic, String phonetic, String from,
            String to, String le, String deeplink, String dictDeeplink,
            String ukPhonetic, List<String> explains, String speakUrl,
            String UKSpeakUrl, String USSpeakUrl, String resultSpeakUrl) {
        this.id = id;
        this.translations = translations;
        this.query = query;
        this.errorCode = errorCode;
        this.usPhonetic = usPhonetic;
        this.phonetic = phonetic;
        this.from = from;
        this.to = to;
        this.le = le;
        this.deeplink = deeplink;
        this.dictDeeplink = dictDeeplink;
        this.ukPhonetic = ukPhonetic;
        this.explains = explains;
        this.speakUrl = speakUrl;
        this.UKSpeakUrl = UKSpeakUrl;
        this.USSpeakUrl = USSpeakUrl;
        this.resultSpeakUrl = resultSpeakUrl;
    }
    @Generated(hash = 144676373)
    public TranslateBean() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public List<String> getTranslations() {
        return this.translations;
    }
    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }
    public String getQuery() {
        return this.query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public int getErrorCode() {
        return this.errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public String getUsPhonetic() {
        return this.usPhonetic;
    }
    public void setUsPhonetic(String usPhonetic) {
        this.usPhonetic = usPhonetic;
    }
    public String getPhonetic() {
        return this.phonetic;
    }
    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getLe() {
        return this.le;
    }
    public void setLe(String le) {
        this.le = le;
    }
    public String getDeeplink() {
        return this.deeplink;
    }
    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }
    public String getDictDeeplink() {
        return this.dictDeeplink;
    }
    public void setDictDeeplink(String dictDeeplink) {
        this.dictDeeplink = dictDeeplink;
    }
    public String getUkPhonetic() {
        return this.ukPhonetic;
    }
    public void setUkPhonetic(String ukPhonetic) {
        this.ukPhonetic = ukPhonetic;
    }
    public List<String> getExplains() {
        return this.explains;
    }
    public void setExplains(List<String> explains) {
        this.explains = explains;
    }
    public String getSpeakUrl() {
        return this.speakUrl;
    }
    public void setSpeakUrl(String speakUrl) {
        this.speakUrl = speakUrl;
    }
    public String getUKSpeakUrl() {
        return this.UKSpeakUrl;
    }
    public void setUKSpeakUrl(String UKSpeakUrl) {
        this.UKSpeakUrl = UKSpeakUrl;
    }
    public String getUSSpeakUrl() {
        return this.USSpeakUrl;
    }
    public void setUSSpeakUrl(String USSpeakUrl) {
        this.USSpeakUrl = USSpeakUrl;
    }
    public String getResultSpeakUrl() {
        return this.resultSpeakUrl;
    }
    public void setResultSpeakUrl(String resultSpeakUrl) {
        this.resultSpeakUrl = resultSpeakUrl;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1698335246)
    public List<WebExplain> getWebExplains() {
        if (webExplains == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WebExplainDao targetDao = daoSession.getWebExplainDao();
            List<WebExplain> webExplainsNew = targetDao
                    ._queryTranslateBean_WebExplains(id);
            synchronized (this) {
                if (webExplains == null) {
                    webExplains = webExplainsNew;
                }
            }
        }
        return webExplains;
    }

    public List<WebExplain> getMyWebExplains(){
        return webExplains;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1686472250)
    public synchronized void resetWebExplains() {
        webExplains = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1914900845)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTranslateBeanDao() : null;
    }
}
