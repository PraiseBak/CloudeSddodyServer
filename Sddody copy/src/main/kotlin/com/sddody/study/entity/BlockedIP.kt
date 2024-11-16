package com.sddody.study.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime


@Entity
class BlockedIP (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0L,
    var ip: String,
    var blockEndTime : LocalDateTime,
    var isBlocked : Boolean = false,
    var count: Int = 0,
){

    fun addCount() : Unit{
        count++
    }
}
