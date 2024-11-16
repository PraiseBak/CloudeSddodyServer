package com.sddody.study.entity

import com.sddody.study.dto.MemberInfoDto
import com.sddody.study.helper.*
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor


/*
그냥 리스트
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Member(
    // 카카오 api에서 가져온 id
    @Id
    var kakaoId: Long?,

    //실제이름말고 닉네임으로 관리
    @NotNull
    @field:Size(min = 2, max = 20, message = "닉네임은 최소 2글자 최대 20자에요")
    var nickname: String,


    //자기소개
    @NotNull
    @field:Size(min = 2, max = 200, message = "자기소개는 최소 2글자 최대 200자에요")
    var selfIntroduce: String,

    @OneToOne
    @JoinColumn(name = "location_id") // 외래 키 컬럼 지정
    var memberLocation: Location,

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = FrameworkEnum::class)
    @NotNull
    @Size(min=1,max=3)
    var haveExperienceTechStack: List<FrameworkEnum>,

    @Enumerated(EnumType.STRING)
    var authority: Authority = Authority.ROLE_USER,

    @Enumerated(EnumType.STRING)
    var memberStatus: MemberStatus = MemberStatus.ROLE_INFO_VALID,

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = DeveloperEnum::class)
    @Size(min=1,max=3)
    @NotNull
    var interestField: List<DeveloperEnum>,

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = LanguageEnum::class)
    @Size(min=1,max=3)
    @NotNull
    var devLanguage: List<LanguageEnum>,

    //참가한 study
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "member")
    var studyMemberList: MutableList<StudyMember> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy="member")
    var boardList: MutableList<Board> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy="member")
    var commentList: MutableList<Comment> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy="member")
    var alarmList: MutableList<Alarm> = mutableListOf(),

    //내가 만든 스터디
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "studyRoomOwner")
    var createdStudyList: MutableList<Study> = mutableListOf(),

    //내가 작성한 스터디 후기
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "member")
    var studyReviewBoardList : MutableList<Board> = mutableListOf(),

    //내가 참가 신청한 스터디들
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "member")
    var participationRequestStudyList: MutableList<RequestStudyMember> = mutableListOf(),

    //내가 추천한 글들
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "member")
    var heartList: MutableList<Heart> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "member")
    var chatList: MutableList<Chat> = mutableListOf(),

    var profileImgSrc: String = "default_img.png",

    @Enumerated(EnumType.STRING)
    var devLevel : DevLevelEnum = DevLevelEnum.HAVING_FUN,

    @Enumerated(EnumType.STRING)
    var devYear : DevYearEnum = DevYearEnum.BEGINNER,

    var githubNickname : String? = null
    ){


    //성별은 바꿀수없음
    fun updateMemberInfo(replaceMember: Member) {
        nickname = replaceMember.nickname
        selfIntroduce = replaceMember.selfIntroduce
        memberLocation = replaceMember.memberLocation
        devLanguage = replaceMember.devLanguage
        haveExperienceTechStack = replaceMember.haveExperienceTechStack
        interestField = replaceMember.interestField
        devYear = replaceMember.devYear
        devLevel = replaceMember.devLevel
    }

    fun setBlock() {
        this.authority = Authority.ROLE_BLOCKED
    }


    fun getMemberInfoDto() : MemberInfoDto
    {
        return MemberInfoDto(
            id = kakaoId!!,
            nickname = nickname,
            memberProfileImgSrc = profileImgSrc ?: "",
            devYearEnum = devYear,
            devLevelEnum = devLevel,
            frameworkEnumList = haveExperienceTechStack,
            developerEnumList = interestField,
            selfIntroduce = selfIntroduce,
            devLanguageEnumList = devLanguage,
            githubNickname = githubNickname,
            location = memberLocation,
            memberStatus = this.memberStatus
        )
    }

    fun updateGithubNickname(nickname: String?) {
        this.githubNickname = nickname
    }

}