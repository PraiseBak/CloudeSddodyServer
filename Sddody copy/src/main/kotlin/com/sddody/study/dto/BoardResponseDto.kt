package com.sddody.study.dto

import java.time.LocalDateTime
import java.util.*

class BoardResponseDto (
    var id : Long,
    var title : String,
    var content : String,
    var heartCount : Int,
    var commentList : MutableList<CommentResponseDto>,
    var boardTargetStudy : Long?,
    var nickname : String,
    var imageSrcList : List<String>,
    var profileImageSrc : String,
    var createdAt : Date?,
    var updatedAt: Date?,
    var tagList : List<String>,
    var link : String,
    var memberId : Long
){

}
