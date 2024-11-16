package com.sddody.study.dto

import com.sddody.study.entity.Location
import com.sddody.study.entity.Member
import com.sddody.study.helper.*

data class MemberDto (
    var kakaoId: Long = 0,
    var nickname: String,
    var selfIntroduce: String,
    var memberLocation: Location,
    var interestField: List<DeveloperEnum>,
    var haveExperienceTechStack: List<FrameworkEnum>,
    var devLanguageField : List<LanguageEnum>,
    //개발 수준
    var devLevel : DevLevelEnum,
    //개발 몇년정도했는지
    var devYear : DevYearEnum,
    var isDeleted : Boolean
) {


    fun setId(memberId: Long) {
        kakaoId = memberId
    }

    fun setIsEnable(isEnable: Boolean) {
        this.isDeleted = isEnable

    }


    companion object {
        fun toMember(
            memberDto: MemberDto,
            profileImgSrc: String,
        ): Member {
            return Member(
                kakaoId = memberDto.kakaoId,
                nickname = memberDto.nickname,
                selfIntroduce = memberDto.selfIntroduce,
                memberLocation = memberDto.memberLocation,
                haveExperienceTechStack = memberDto.haveExperienceTechStack,
                interestField = memberDto.interestField,
                devLanguage = memberDto.devLanguageField,
                profileImgSrc = profileImgSrc,
                devLevel = memberDto.devLevel,
                devYear = memberDto.devYear
            )

        }
    }



}