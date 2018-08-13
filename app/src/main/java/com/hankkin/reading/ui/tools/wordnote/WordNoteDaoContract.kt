package com.hankkin.reading.ui.tools.wordnote

import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.mvp.model.BaseDaoContract

/**
 * @author Hankkin
 * @date 2018/8/12
 */
interface WordNoteDaoContract : BaseDaoContract{
    fun queryWordNotes(): MutableList<WordNoteBean>?
    fun addWordToNote(wordNoteBean: WordNoteBean)
    fun removeWordNote(wordNoteBean: WordNoteBean)
}