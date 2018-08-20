package com.hankkin.reading.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

import com.hankkin.reading.greendao.DaoSession;
import com.hankkin.reading.greendao.TranslateBeanDao;
import com.hankkin.reading.greendao.WordNoteBeanDao;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author Hankkin
 * @date 2018/8/12
 */
@Entity
public class WordNoteBean implements Serializable {
    private static final long serialVersionUID = -5943020412594111239L;
    @Id
    private long id;
    @ToOne(joinProperty = "id")
    private TranslateBean translateBean;
    @Property(nameInDb = "isEmphasis")
    private boolean isEmphasis;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2001773151)
    private transient WordNoteBeanDao myDao;
    @Generated(hash = 1506017723)
    private transient Long translateBean__resolvedKey;
    @Generated(hash = 228743069)
    public WordNoteBean(long id, boolean isEmphasis) {
        this.id = id;
        this.isEmphasis = isEmphasis;
    }
    @Generated(hash = 1122185869)
    public WordNoteBean() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
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
    @Generated(hash = 1986712089)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWordNoteBeanDao() : null;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 990025581)
    public TranslateBean getTranslateBean() {
        long __key = this.id;
        if (translateBean__resolvedKey == null || !translateBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TranslateBeanDao targetDao = daoSession.getTranslateBeanDao();
            TranslateBean translateBeanNew = targetDao.load(__key);
            synchronized (this) {
                translateBean = translateBeanNew;
                translateBean__resolvedKey = __key;
            }
        }
        return translateBean;
    }

    public TranslateBean getMyTranslate(){
        return translateBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1759489943)
    public void setTranslateBean(@NotNull TranslateBean translateBean) {
        if (translateBean == null) {
            throw new DaoException(
                    "To-one property 'id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.translateBean = translateBean;
            id = translateBean.getId();
            translateBean__resolvedKey = id;
        }
    }
    public boolean getIsEmphasis() {
        return this.isEmphasis;
    }
    public void setIsEmphasis(boolean isEmphasis) {
        this.isEmphasis = isEmphasis;
    }
}
