package com.hankkin.reading.ui.tools.wordnote

import com.hankkin.reading.domain.WordNoteBean
import com.hankkin.reading.dao.BaseDaoContract

/**
 * @author Hankkin
 * @date 2018/8/12
 */
interface WordNoteDaoContract : BaseDaoContract {
    fun queryWordNotes(): MutableList<WordNoteBean>?
    fun addWordToNote(wordNoteBean: WordNoteBean)
    fun removeWordNote(wordNoteBean: WordNoteBean)
    fun queryEmphasisWord(): MutableList<WordNoteBean>?
    fun updateWord(wordNoteBean: WordNoteBean)
    fun insertWordNotes(data: MutableList<WordNoteBean>)
}