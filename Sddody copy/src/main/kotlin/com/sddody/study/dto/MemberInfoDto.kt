package com.sddody.study.dto

import com.sddody.study.entity.Location
import com.sddody.study.helper.*

data class MemberInfoDto(
    var id : Long,
    var nickname : String,
    var memberProfileImgSrc : String,
    //추가정보

    var selfIntroduce: String? = null,
    var devYearEnum : DevYearEnum? = null,
    var devLevelEnum: DevLevelEnum? = null,
    var frameworkEnumList: List<FrameworkEnum>? = null,
    var developerEnumList: List<DeveloperEnum>? = null,
    var devLanguageEnumList: List<LanguageEnum>? = null,
    var githubNickname : String? = null,
    var location : Location? = null,
    var memberStatus : MemberStatus
) {
    
    
    
}
