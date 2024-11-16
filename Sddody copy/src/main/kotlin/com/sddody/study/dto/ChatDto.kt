package com.sddody.study.dto

import com.sddody.study.helper.ValidationEnumClass
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class ChatDto(
    @field:Size(min = 1, max = 1000, message = ValidationEnumClass.CHAT_MSG_SIZE_VALIDATION)
    val msg : String,


//    @field:Size(min = 0, max = 10, message = ValidationEnumClass.IMAGE_SIZE_VALIDATION)
//    val imageList : ArrayList<MultipartFile> = arrayListOf(),

    ) {

}