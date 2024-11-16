package com.sddody.study.dto

import com.sddody.study.entity.Location
import com.sddody.study.helper.*
import java.util.Date

data class StudyResponseDto(
    //스터디 번호
    var studyId: Long,
    // 스터디 제목
    var title: String,

    // 스터디 내용
    var content: String,

    var maxStudyMemberNum: Int,

    var studyImageSrcList: List<String>,

    var studyField: List<DeveloperEnum> = arrayListOf(),

    var studyTechStack: List<FrameworkEnum> = arrayListOf(),

    var isOpenToEveryone: Boolean = false,

    var chatRoomResponseDto: ChatRoomDto? = null,

    var createdAt: Date,

    var updatedAt: Date,

    var location: Location,

    var studyMemberInfoDtoList: List<MemberInfoDto> = listOf(),
    var devYear: DevYearEnum,
    var devLevel: DevLevelEnum,

    var ownerMemberInfo : MemberInfoDto,

    var studyStateEnum: StudyStateEnum,
    ){


}


