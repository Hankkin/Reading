package com.hankkin.reading.ui.tools.wordnote

import com.hankkin.library.utils.SPUtils
import com.hankkin.reading.common.Constant
import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.greendao.WordNoteBeanDao
import com.hankkin.reading.dao.BaseDao

/**
 * @author Hankkin
 * @date 2018/8/12
 */
class WordNoteDao : BaseDao(), WordNoteDaoContract {

    override fun insertWordNotes(data: MutableList<WordNoteBean>) {
        updateSPTime()
        daoSession.wordNoteBeanDao.insertOrReplaceInTx(data)
    }

    /**
     * 插入更新单词
     */
    override fun updateWord(wordNoteBean: WordNoteBean) {
        updateSPTime()
        daoSession.wordNoteBeanDao.insertOrReplace(wordNoteBean)
    }

    /**
     * 查询重点单词
     */
    override fun queryEmphasisWord(): MutableList<WordNoteBean>? {
        return daoSession.wordNoteBeanDao.queryBuilder().where(WordNoteBeanDao.Properties.IsEmphasis.eq(true)).build().list()
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
        updateSPTime()
        daoSession.wordNoteBeanDao.insertOrReplace(wordNoteBean)
    }

    /**
     * 查询单词本
     */
    override fun queryWordNotes(): MutableList<WordNoteBean> =
            daoSession.wordNoteBeanDao.queryBuilder().list()

    private fun updateSPTime() {
        SPUtils.put(Constant.SP_KEY.DB_UPDATE_TIME, System.currentTimeMillis())
    }

}