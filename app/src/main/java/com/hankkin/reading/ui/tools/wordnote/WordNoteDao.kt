package com.hankkin.reading.ui.tools.wordnote

import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.mvp.model.BaseDao

/**
 * @author Hankkin
 * @date 2018/8/12
 */
class WordNoteDao : BaseDao(),WordNoteDaoContract{


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