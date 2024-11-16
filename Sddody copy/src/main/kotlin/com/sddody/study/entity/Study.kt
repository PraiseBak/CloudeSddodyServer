package com.sddody.study.entity

import com.sddody.study.dto.*
import com.sddody.study.helper.*
import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.time.ZoneId
import java.util.*


/**
 *      study의 상태 변화에 주의해야합니다
 *      study가 종료되면 채팅과 같은 기능도 중지됩니다
 *
 * @property title 2-40자의 길이로 제한된 제목입니다.
 * @property content 1-200자의 길이로 제한된 내용입니다.
 * @property maxStudyMemberNum 최대 200명까지 가능하며, 1명으로 설정해서 혼자서도 사용 가능합니다.
 * @property studyImageList 최대 5개의 이미지 경로를 포함할 수 있습니다.
 * @property studyField
 * @property studyTechStack
 * @property studyMemberList
 * @property currentState
 * @property studyRoomOwner
 * @property id
 */
@Entity
class Study(
        // 스터디 제목
    @field:Size(min = 2, max = 50, message = ValidationEnumClass.STUDY_TITLE_VALIDATION)
    var title: String,

    @field:Size(min = 2, max = 4000, message = ValidationEnumClass.STUDY_CONTENT_VALIDATION)
        // 스터디 내용
    var content: String,

    @field:Min(value = 2L, message=ValidationEnumClass.STUDY_MEMBER_VALIDATION)
    @field:Max(value = 200L,message=ValidationEnumClass.STUDY_MEMBER_VALIDATION)
    var maxStudyMemberNum: Int = 2,

    @field:Size(min = 0, max = 10, message = ValidationEnumClass.IMAGE_SIZE_VALIDATION)
    @OneToMany
    var studyImageList: MutableList<Image> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = DeveloperEnum::class)
    var studyField: List<DeveloperEnum> = listOf(DeveloperEnum.ETC),

    @field:Size(min=1,max=5)
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = FrameworkEnum::class)
    var studyTechStack: List<FrameworkEnum> = arrayListOf(),

        //최대 200명
    @field:Size(min=1,max=200)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "study")
    var studyMemberList: MutableList<StudyMember> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    var currentState: StudyStateEnum = StudyStateEnum.PREPARATION,

    @ManyToOne
    @JoinColumn(name = "study_room_owner_id")
    var studyRoomOwner: Member,

    @OneToOne
    var mainChatRoom: ChatRoom? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var isOpenToEveryone: Boolean = false,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "study")
    var participationRequestMemberList: MutableList<RequestStudyMember> = mutableListOf(),


    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "targetStudy")
    var boardList: MutableList<Board> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "study")
    val commentList: MutableList<Comment> = mutableListOf(),


    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var location: Location,

    @Enumerated(EnumType.STRING)
    var devLevel : DevLevelEnum = DevLevelEnum.HAVING_FUN,

    @Enumerated(EnumType.STRING)
    var devYear : DevYearEnum = DevYearEnum.BEGINNER,




    ): BaseTimeEntity() {

    fun setStatusClose() {
            this.currentState = StudyStateEnum.END
    }



    fun modifyStudy(modifyStudyDto: ModifyStudyDto, imageList: MutableList<Image>){
            this.title = modifyStudyDto.title
            this.content = modifyStudyDto.content
            this.studyImageList = imageList

            if(studyMemberList.size <= modifyStudyDto.maxStudyMemberNum){
                    this.maxStudyMemberNum = modifyStudyDto.maxStudyMemberNum
            }

            this.studyField = modifyStudyDto.studyField
            this.studyTechStack = modifyStudyDto.studyTechStack
            modifyStudyDto.location?.let {  this.location = it }
    }

    fun setStatusOpen() {
            this.currentState = StudyStateEnum.STUDYING
    }

    fun updateMainChatRoom(createMainChatRoom: ChatRoom) {
        this.mainChatRoom = createMainChatRoom
    }

    fun saveImageList(savedImageList: MutableList<Image>) {
        this.studyImageList = savedImageList
    }

    fun convertEntityToResponseDto(
        isGetLocation : Boolean = false
    ): StudyResponseDto {
        val studyImageList :MutableList<String> = mutableListOf()

        this.studyImageList.forEach{
            studyImageList.add(it.src)
        }
        val chatRoomId = this.mainChatRoom?.id ?: 0

        return StudyResponseDto(studyId = this.id!!,
            title = this.title,
            content = this.content,
            maxStudyMemberNum = this.maxStudyMemberNum,
            studyImageSrcList = studyImageList,
            studyField =  this.studyField,
            this.studyTechStack,
            this.isOpenToEveryone,
            chatRoomResponseDto =  if(!isGetLocation) getChatRoomDto(chatRoomId = chatRoomId) else null,
            createdAt = Date.from(this.createdAt?.atZone(ZoneId.systemDefault())?.toInstant()),
            updatedAt = Date.from(this.updatedAt?.atZone(ZoneId.systemDefault())?.toInstant()),
            location = this.location,
            studyMemberInfoDtoList = studyMemberList.map {
                it.member.getMemberInfoDto()
            },
            devYear = devYear,
            devLevel = devLevel,
            ownerMemberInfo = studyRoomOwner.getMemberInfoDto(),
            studyStateEnum = currentState
        )
    }

    fun getChatRoomDto(chatRoomId : Long) : ChatRoomDto{
        val chatRoomMemberInfoDtoList : MutableList<MemberInfoDto> = mutableListOf()

        this.studyMemberList.forEach{
            val member = it.member
            val chatRoomMemberInfoDto = MemberInfoDto(
                id = member.kakaoId!!,
                memberProfileImgSrc = member.profileImgSrc ?: "",
                nickname = member.nickname,
                memberStatus = member.memberStatus
            )
            chatRoomMemberInfoDtoList.add(chatRoomMemberInfoDto)
        }

        return ChatRoomDto(
            id = chatRoomId,
            studyId = this.id!!,
            studyName = this.title,
            studyMemberNum = this.studyMemberList.size,
            notReadMsgNum = -1,
            isChatOver = (this.currentState == StudyStateEnum.END),
            chatRoomMemberInfoDtoList = chatRoomMemberInfoDtoList.toList(),
        )

    }

    fun updateStatus(newStatus: StudyStateEnum) {
        this.currentState = newStatus
    }

    companion object{
        fun convertDtoToEntityWithOwnerMember(dto: OpenStudyDto,studyRoomOwner: Member): Study {
                    return Study(
                            title = dto.title,
                            content = dto.content,
                            maxStudyMemberNum = dto.maxStudyMemberNum,
                            studyField = dto.studyField,
                            studyTechStack = dto.studyTechStack,
                            studyMemberList = mutableListOf(), // 우선은 빈 리스트 생성
                            currentState = StudyStateEnum.PREPARATION,
                            studyRoomOwner = studyRoomOwner,
                            isOpenToEveryone = dto.isOpenToEveryone,
                            location = dto.location,
                            devLevel = dto.devLevel,
                            devYear = dto.devYear
                    )
            }

        }



}


