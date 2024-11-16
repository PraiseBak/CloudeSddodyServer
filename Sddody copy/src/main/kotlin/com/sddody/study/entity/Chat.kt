package com.sddody.study.entity;

import com.sddody.study.dto.ChatInfoDto
import com.sddody.study.helper.ValidationEnumClass
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import lombok.Builder
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap


@Entity
@Builder
class Chat (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    val chatRoom: ChatRoom,

    @field:Size(max = 1000, message = ValidationEnumClass.CHAT_MSG_SIZE_VALIDATION)
    val msg : String,

    @field:Size(min = 0, max = 10, message = ValidationEnumClass.IMAGE_SIZE_VALIDATION)
    @OneToMany
    var imageList : List<Image> = arrayListOf(),

    @ManyToOne
    var member : Member,

    var isDeleted : Boolean = false,

    //본인은 항상 읽은 상태
    var readCount : Long = 1L,


) : BaseTimeEntity(){
    fun addReadCount() {
        readCount++
    }

    fun toChatInfoDto(): ChatInfoDto {
        val imgSrcList : MutableList<String> = mutableListOf()

        imageList.forEach {
            imgSrcList.add(it.src)
        }

        return ChatInfoDto(
            msg = if(isDeleted) "삭제된 메시지입니다" else msg,
            imageSrcList = imgSrcList,
            writeMemberNickname = member.nickname,
            writeMemberProfileSrc = member.profileImgSrc,
            readCount = readCount,
            isDeleted = isDeleted,
            createdDate = Date.from(createdAt?.atZone(ZoneId.systemDefault())?.toInstant()),
            writeMemberId = member.kakaoId!!
        )



    }


}
