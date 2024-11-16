package com.sddody.study.dto

import lombok.ToString

@ToString
class TokenDto(
    var accessToken:String? = null,
    var refreshToken:String? = null,
    var memberId : Long? = 0,
    var nickname : String? = null,
    var isDeleted : Boolean = false
)
{
    fun renewAccessToken(newAccessToken: String) {
        accessToken = newAccessToken
    }


}