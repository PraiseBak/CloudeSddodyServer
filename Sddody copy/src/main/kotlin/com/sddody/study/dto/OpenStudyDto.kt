package com.sddody.study.dto

import com.sddody.study.entity.Location
import com.sddody.study.helper.*
import jakarta.persistence.*

//study time으로 뭔가할수있지않을까?
//time으로 구현
//interface는 제한된 interface로 시간을 선택할수있게 구현
data class OpenStudyDto(
        // 스터디 제목
    var title: String,

        // 스터디 내용
    var content: String,

    var maxStudyMemberNum: Int = 2,

    var studyImageSrc: String = "",

    @Enumerated(EnumType.STRING)
    var studyField: List<DeveloperEnum>,

    @Enumerated(EnumType.STRING)
    var studyTechStack: List<FrameworkEnum>,

    var isOpenToEveryone : Boolean = false,

    var location : Location,

    var devLevel : DevLevelEnum,

    var devYear : DevYearEnum

) {

}

