package com.hankkin.reading.ui.tools.wordnote

import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.greendao.WordNoteBeanDao
import com.hankkin.reading.mvp.model.BaseDao

/**
 * @author Hankkin
 * @date 2018/8/12
 */
class WordNoteDao : BaseDao(),WordNoteDaoContract{

    override fun insertWordNotes(data: MutableList<WordNoteBean>) {
        daoSession.wordNoteBeanDao.insertOrReplaceInTx(data)
    }

    /**
     * 插入更新单词
     */
    override fun updateWord(wordNoteBean: WordNoteBean) {
        daoSession.wordNoteBeanDao.insertOrReplace(wordNoteBean)
    }

    /**
     * 查询重点单词
     */
    override fun queryEmphasisWord(): MutableList<WordNoteBean>? {
        return  daoSession.wordNoteBeanDao.queryBuilder().where(WordNoteBeanDao.Properties.IsEmphasis.eq(true)).build().list()
    }


    /**
     * 移除单词本
     */
    override fun removeWordNote(wordNoteBean: WordNoteBean) {
        daoSession.wordNoteBeanDao.delete(wordNoteBean)
    }

    /**
     * 加入单词本
     */
    override fun addWordToNote(wordNoteBean: WordNoteBean) {
        daoSession.wordNoteBeanDao.insertOrReplace(wordNoteBean)
    }

    /**
     * 查询单词本
     */
    override fun queryWordNotes(): MutableList<WordNoteBean> =
            daoSession.wordNoteBeanDao.queryBuilder().list()

}