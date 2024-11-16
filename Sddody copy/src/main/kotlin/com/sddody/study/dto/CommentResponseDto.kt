package com.sddody.study.dto

import java.time.LocalDateTime
import java.util.*

data class CommentResponseDto (
    var id : Long,
    var content : String,
    var nickname : String,
    var userProfileImageSrc : String,
    var createdAt : Date?,
    var updatedAt: Date?

){

}
