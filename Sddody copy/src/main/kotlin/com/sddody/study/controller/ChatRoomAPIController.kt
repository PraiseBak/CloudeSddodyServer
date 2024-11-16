package com.sddody.study.controller;


import com.sddody.study.dto.*
import com.sddody.study.helper.ResponseEntityEnum
import com.sddody.study.helper.ResponseEntityWrapper
import com.sddody.study.service.ChatRoomService
import com.sddody.study.service.FCMService
import com.sddody.study.service.MemberService
import com.sddody.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatRoom")
class ChatRoomAPIController(
        private val studyService : StudyService,
        private val chatRoomService: ChatRoomService,
        private val fcmService: FCMService,
        private val memberService: MemberService,
){
        @GetMapping("/list")
        fun getChatRoomList(authentication: Authentication) : ResponseEntity<List<ChatRoomDto>>{
                val chatRoomDtoList = chatRoomService.getChatRoomList(authentication.name.toLong())
                return ResponseEntity<List<ChatRoomDto>>(chatRoomDtoList, HttpStatus.OK)
        }

        @GetMapping("/{studyId}/msg")
        fun getChatInfoList(authentication: Authentication,@PathVariable studyId : Long) : ResponseEntity<List<ChatInfoDto>>{
                val chatDtoList = chatRoomService.getChatInfo(authentication.name.toLong(),studyId)
                return ResponseEntity<List<ChatInfoDto>>(chatDtoList, HttpStatus.OK)
        }

        @GetMapping("/{studyId}")
        fun getChatRoom(authentication: Authentication,@PathVariable studyId : Long) : ResponseEntity<ChatRoomDto?>{
                val chatRoomDto = chatRoomService.getChatRoomDto(studyId)
                return ResponseEntity<ChatRoomDto?>(chatRoomDto, HttpStatus.OK)
        }

        @PostMapping("/{studyId}")
        fun saveMsg(
                @PathVariable studyId: Long,
                @RequestPart("chatDto") chatDto : ChatDto?,
                @RequestPart("imageList[]") imageList : List<MultipartFile>? = mutableListOf(),
                authentication: Authentication): ResponseEntity<String> {
                val actualImageList = imageList?.let {  it } ?: mutableListOf()

                chatRoomService.saveMsg(chatDto,actualImageList,authentication.name.toLong(),studyId)
                chatRoomService.broadCast(studyId,authentication.name.toLong())
                return ResponseEntityWrapper.getResponseEntity(ResponseEntityEnum.SEND_MSG_SUCCESS)
        }


        @DeleteMapping("/{deleteChatId}")
        fun deleteMsg(@PathVariable deleteChatId : Long,authentication: Authentication){
                chatRoomService.deleteMsg(deleteChatId,authentication.name.toLong())
        }


//        @PostMapping("/")
//        fun addChatRoom(study:Study){
//                chatRoomService.createMainChatRoom(study)
//        }

        /**
         * 알람을 위한 채팅방 구독리스트 가져오기
         */
        @GetMapping("/subscribeInfoList")
        fun getSubscribeInfoList(authentication: Authentication): ResponseEntity<List<String>>{
                val userId = authentication.name.toLong()
                val member =  memberService.findByMemberIdOrThrow(userId)
                return ResponseEntity(chatRoomService.subscribeList(member),HttpStatus.OK)
        }


        @PutMapping("/{studyId}/read")
        fun readChat(authentication: Authentication,@PathVariable studyId : Long){
                chatRoomService.readChat(studyService.findStudyByIdOrThrow(studyId),authentication.name.toLong())
        }
}

