package com.hankkin.reading.domain

import java.io.Serializable

data class HotBean(val id: Int,
                   val link: String,
                   val name: String,
                   val order: Int,
                   val visible: Int) : Serializable