package com.sddody.study.entity

import com.sddody.study.helper.AlarmCategory
import com.sddody.study.helper.AlarmDto
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class Alarm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0L,

    //해당 알람의 정보
    var info : String,
    //이동할 uri의 id부분
    var moveUriId : Long = 0,
    //읽었는지 여부
    var isRead : Boolean = false,

    //알람의 유형
    @Enumerated(EnumType.STRING)
    var alarmCategory: AlarmCategory,

    @ManyToOne
    var member: Member,
){
    fun convertEntityToDto() : AlarmDto{
        return AlarmDto(info = this.info,
                moveUriId = this.moveUriId,
                isRead = this.isRead,
                alarmCategory = this.alarmCategory
            )
    }

    fun setRead() {
        this.isRead = true
    }
}
