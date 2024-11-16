package com.sddody.study.service;

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.*
import com.sddody.study.entity.*
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.helper.StudyStateEnum
import com.sddody.study.repository.ChatRepository
import com.sddody.study.repository.ChatRoomRepository
import com.sddody.study.repository.StudyRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Service
@RequiredArgsConstructor
class ChatRoomService(
    private val memberService: MemberService,
    private val chatRoomRepository: ChatRoomRepository,
    private val studyRepository: StudyRepository,
    private val chatRepository: ChatRepository,
    private val fcmService: FCMService,
    private val imageService: ImageService,

)
{

    /**
     * 채팅을 저장하는 메서드
     * study.chatRoom.chatList에 chat 추가됨
     * 이후 브로드캐스팅
     * study 대기,시작한 단계에서는 메시지 추가 가능
     * @param chatroomId
     * @param msg
     * @param img
     * @param nickname
     * @throws SddodyExceptionError.BAD_REQUEST
     */
    @Transactional
    fun saveMsg(chatDto: ChatDto?, imageList: List<MultipartFile>, memberId: Long, studyId: Long) {
        var study = studyRepository.findById(studyId).orElseThrow{throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
        val member = memberService.findByMemberIdOrThrow(memberId)
        val chatRoom = study.mainChatRoom ?: throw SddodyException(SddodyExceptionError.BAD_REQUEST);

        if(study.currentState == StudyStateEnum.END){
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }

        if(!memberService.isMemberContainsStudy(study,member)) throw SddodyException(SddodyExceptionError.BAD_REQUEST)



        val chat = chatDto?.run {
            Chat(msg = this.msg, member = member, chatRoom = chatRoom)
        } ?: Chat(msg = "",member = member, chatRoom = chatRoom)


        if(chat.msg.isEmpty() && imageList.isEmpty()) throw SddodyException(SddodyExceptionError.BAD_REQUEST)


        chatRepository.save(chat)
        val savedImageList : ArrayList<Image> = arrayListOf()

        imageList.forEach{
            val chatImage = Image(src = imageService.saveImage(it), chat = chat)
            savedImageList.add(chatImage)
            imageService.save(chatImage)
        }

        chat.imageList = savedImageList

        addStudyMemberReadableCount(study.studyMemberList,chatRoom.chatReadMap)

        chatRoom.chatList.add(chat)
        studyBroadCast(study,memberId)
    }

    fun broadCast(studyId : Long,memberId : Long){
        var study = studyRepository.findById(studyId).orElseThrow{throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
        studyBroadCast(study,memberId)
    }




    //스터디의 각 멤버에게 읽을 수 있는 메시지 count +1
    private fun addStudyMemberReadableCount(studyMemberList: MutableList<StudyMember>, chatReadMap: MutableMap<Long, Long>) {
        studyMemberList.forEach{

            val memberId = it.member.kakaoId!!
            val count = chatReadMap[memberId]
            count?.let {
                chatReadMap.set(memberId, if(count >= 300 ) 301 else count+1)
            }
        }
    }

    /*
        브로드 캐스트
     */
    fun studyBroadCast(study: Study, writeMemberId: Long) {
        val topic = study.mainChatRoom?.topic
        topic?.let {topic ->
            fcmService.sendNotificationToChatRoom(topic,"${study.title}으로 부터의 메시지가 왔습니다","${study.title}으로 부터의 메시지가 왔습니다")

            //해당 스터디에 해당하는 사람들에게 알람추가
            study.studyMemberList.forEach{
                val memberId = it.id!!
                if(memberId != writeMemberId){
                    fcmService.sendNewNotification("notification_$memberId","${study.title}으로 부터의 메시지가 왔습니다","${study.title}으로 부터 새 메시지가 있습니다",study.id)
                }
            }
        }


    }

    fun findByChatRoomId(chatroomId : Long) : ChatRoom{
        return chatRoomRepository.findById(chatroomId).orElseThrow{ SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)};
    }

    /**
     * 채팅방 생성 이때 토픽도 생성됨(알림용)
     * @param study
     * @return
     */
    @Transactional
    fun createMainChatRoom(study: Study) : ChatRoom{
        val topic = "chat_${study.id}"
        val chatRoom = ChatRoom(study = study, topic = topic)
        chatRoomRepository.save(chatRoom).study = study
        study.updateMainChatRoom(chatRoom)
        return chatRoom
    }

    @Transactional
    fun endChatRoom(chatRoomId: Long, memberId: Long){
        var chatRoom = findByChatRoomId(chatRoomId)
        if(chatRoom.study.studyRoomOwner.kakaoId != memberId) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        chatRoom.setChatRoomEnd()
    }

    @Transactional
    fun deleteMsg(deleteChatId: Long, memberId: Long) {
        val deleteTargetChat = findChatByIdOrThrow(deleteChatId)
        if(deleteTargetChat.member.kakaoId != memberId) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        deleteTargetChat.isDeleted = true
    }

    fun findChatByIdOrThrow(chatId : Long) : Chat{
        return chatRepository.findById(chatId).orElseThrow{SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
    }

    fun getChatRoomDto(studyId : Long) : ChatRoomDto?{
        var study = studyRepository.findById(studyId).orElseThrow{throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
        study.mainChatRoom?.let {
            return study.getChatRoomDto(it.id!!)
        } ?: {
            throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)
        }

        return null
    }

    fun getChatRoomList(memberId : Long): MutableList<ChatRoomDto> {
        val member = memberService.findByMemberIdOrThrow(memberId)
        val studyIdList : MutableList<Long> = mutableListOf()

        val chatRoomDtoList : MutableList<ChatRoomDto> = mutableListOf()
        member.studyMemberList.forEach { studyMember ->
            val study = studyMember.study

            studyMember.study.id?.let {
                val studyId = it
                studyIdList.add(it)

                val studyMemberNum = study.studyMemberList.size
                val studyName = study.title

                val notReadMsgNum = 0

                val memberInfoDtoList : MutableList<MemberInfoDto> = mutableListOf()
                study.studyMemberList.forEach{
                    memberInfoDtoList.add(it.member.getMemberInfoDto())
                }


                study.mainChatRoom?.id?.let { chatRoomId ->
                    val chatRoomDto = ChatRoomDto(
                        id = chatRoomId,
                        studyId = it,
                        studyName = studyName,
                        studyMemberNum = studyMemberNum,
                        notReadMsgNum = -1,
                        isChatOver = (study.currentState == StudyStateEnum.END),
                        chatRoomMemberInfoDtoList = memberInfoDtoList,
                        recentMsg = study.mainChatRoom?.run { this.chatList.lastOrNull()?.msg } ?: "",
                        chatReadMap = study.mainChatRoom?.chatReadMap ?: null
                    )

                    chatRoomDtoList.add(chatRoomDto)
                }
            }
        }

        return chatRoomDtoList.asReversed()
    }

    fun getChatInfo(memberId : Long , studyId: Long): List<ChatInfoDto> {
        val study = studyRepository.findById(studyId).orElseThrow{SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
        val chatRoom = study.mainChatRoom ?: throw SddodyException(SddodyExceptionError.BAD_REQUEST)

        val member = memberService.findByMemberIdOrThrow(memberId)
        if(!memberService.isMemberContainsStudy(chatRoom.study,member)) throw SddodyException(SddodyExceptionError.BAD_REQUEST)

        val chatInfoList : MutableList<ChatInfoDto> = mutableListOf()
        chatRoom.let { chatRoom ->
            chatRoom.chatList.forEach{
                chatInfoList.add(it.toChatInfoDto())
            }
        }

        return chatInfoList.toList()
    }

    fun subscribeList(member: Member): List<String> {
        val resultList : MutableList<String> = mutableListOf()

        member.studyMemberList.forEach{
            it.study.mainChatRoom?.topic?.let { it1 -> resultList.add(it1) }
        }

        return resultList.toList()
    }

    /**
     * 해당 스터디의 특정 유저에 한해서 메시지를 읽음처리(count를 0으로)
     *
     * @param study
     * @param memberId
     */
    @Transactional
    fun readChat(study : Study,memberId : Long) {
        val member = memberService.findByMemberIdOrThrow(memberId)
        if(!memberService.isMemberContainsStudy(study,member)) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        study.mainChatRoom?.chatReadMap?.set(memberId,0)
    }
}
