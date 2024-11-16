package com.sddody.study.dto

import com.sddody.study.entity.Location
import com.sddody.study.helper.*
import jakarta.persistence.*


/**
 *
 *
 * @property title
 * @property content
 * @property maxStudyMemberNum 현재 존재하는 스터디 멤버 수보다 적으면 안됨
 * @property studyImageSrc
 * @property studyField
 * @property studyTechStack 최대 5개
 */
data class ModifyStudyDto(
        // 스터디 제목
    var title: String,

        // 스터디 내용
    var content: String,

        //현재 있는 스터디 멤버 수보다 적으면 안됨
    var maxStudyMemberNum: Int = 2,


    @Enumerated(EnumType.STRING)
    var studyField: List<DeveloperEnum> = listOf(DeveloperEnum.ETC),

    @Enumerated(EnumType.STRING)
    var studyTechStack: List<FrameworkEnum> = arrayListOf(),

    var studyId : Long,

    var location : Location
) {
}

