package com.hankkin.reading.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hankkin.reading.R
import com.hankkin.reading.domain.Phonetics

/**
 * Created by huanghaijie on 2018/5/20.
 */
class PhoneticsAdapter : BaseQuickAdapter<Phonetics, BaseViewHolder>(R.layout.layout_translate_speak) {
    override fun convert(p0: BaseViewHolder, phonetics: Phonetics) {
        p0.setText(R.id.tv_translate_speak,if (phonetics.type == 1) "英  ${phonetics.content}" else "美  ${phonetics.content}")
    }

}