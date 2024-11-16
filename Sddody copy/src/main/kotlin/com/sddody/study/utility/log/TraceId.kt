package com.sddody.study.utility.log

import java.util.*


class TraceId{
    private var id: String? = null
    private var level = 0

    constructor(){
        id = createId()
        level = 0
    }

    private constructor(id: String, level: Int){
        this.id = id
        this.level = level
    }

    private fun createId(): String {
        return UUID.randomUUID().toString().substring(0, 8)
    }

    fun createNextId(): TraceId {
        return TraceId(id!!, level + 1)
    }

    fun createPreviousId(): TraceId {
        return TraceId(id!!, level - 1)
    }

    fun isFirstLevel(): Boolean {
        return level == 0
    }

    fun getId(): String? {
        return id
    }

    fun getLevel(): Int {
        return level
    }
}
