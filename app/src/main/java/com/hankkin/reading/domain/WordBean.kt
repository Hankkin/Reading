package com.hankkin.reading.domain

/**
 * Created by huanghaijie on 2018/5/17.
 */
data class WordBean(val id: Int, val title: String, val star: Int, val type: String,
                val phonetics: MutableList<Phonetics>, val paraphrases: HashMap<String,MutableList<String>>,
                val ranks: MutableList<String>?)

data class Phonetics(val type: Int, val name: String, val url: String, val content: String)

data class Paraphrases(val n: MutableList<String>?, val vt: MutableList<String>?, val vi: MutableList<String>?,
                       val adj: MutableList<String>?, val adv: MutableList<String>?, val abbr: MutableList<String>?)


