package com.sddody.study.controller;

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.*
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.helper.StudyOrderType
import com.sddody.study.helper.StudyStateEnum
import com.sddody.study.service.MemberService
import com.sddody.study.service.StudyService
import jakarta.websocket.server.PathParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/study")
class StudyAPIController(
    val studyService: StudyService,
    val memberService: MemberService,
) {
    //추천 스터디 가져옴
    @GetMapping("/list")
    fun getStudyList(
        @RequestParam(defaultValue = "1") page : Int,
        @RequestParam orderType : StudyOrderType? = StudyOrderType.RECENT_CREATE,
        authentication: Authentication
    ) : ResponseEntity<MutableList<StudyResponseDto>> {
        val actualOrderType = orderType ?: StudyOrderType.RECENT_CREATE // null일 경우 기본값 설정
        val studyList = studyService.getStudyList(page, actualOrderType,authentication.name.toLong())
        return ResponseEntity(studyList,HttpStatus.OK)
    }




    //해당 유저가 참여한 스터디 가져오기(종료 상태 관계없이)
    @GetMapping("/member/list/")
    fun getMemberStudyList(@RequestParam(defaultValue = "0") memberId : Long,
                           @RequestParam orderType : StudyOrderType,
                           authentication: Authentication) : ResponseEntity<List<StudyResponseDto>> {
        val id = if(memberId == 0L) authentication.name.toLong() else memberId
        return ResponseEntity(studyService.getMemberStudyList(orderType,id),HttpStatus.OK)
    }

    @GetMapping("/location/list")
    fun getStudyLocationList(authentication: Authentication,
                             @RequestParam(defaultValue = "false") isOnlyNearbyRecommend : Boolean,
    ): ResponseEntity<List<StudyResponseDto>> {
        val studyList = studyService.getStudyLocationInfoList(isOnlyNearbyRecommend,authentication.name.toLong())
        return ResponseEntity(studyList,HttpStatus.OK)
    }


    @GetMapping("/{id}")
    fun getStudy(@PathVariable id : Long): ResponseEntity<StudyResponseDto> {
        return ResponseEntity(studyService.getStudy(id),HttpStatus.OK)
    }



    //study open
    @PostMapping("/")
    fun openStudy(@RequestPart("studyDto") openStudyDto : OpenStudyDto,
                  @RequestPart("imageList[]", required = false) imageList : List<MultipartFile>? = mutableListOf(),
                  authentication: Authentication) {
        openStudyDto.studyTechStack.forEach{
            println(it)
        }

        val actualImageList = imageList ?: mutableListOf()
        studyService.openStudy(openStudyDto, authentication.name.toLong(),imageList = actualImageList)
    }

    //modify
    @PutMapping("/")
    fun modifyStudy(@RequestPart("studyDto") modifyStudyDto: ModifyStudyDto,
                    @RequestPart("imageList[]") imageList : List<MultipartFile> = mutableListOf(),
                    authentication: Authentication) {
        studyService.modify(modifyStudyDto, authentication.name.toLong(),imageList)

    }


    @PutMapping("/end")
    fun endStudy(@RequestBody requestData: StudyIdDto,authentication: Authentication): ResponseEntity<String> {
        studyService.endStudy(requestData.studyId,authentication.name.toLong())
        return ResponseEntity<String>("스터디 중지 완료", HttpStatus.OK);
    }



    //관리자가 멤버 삭제한 경우
    @PutMapping("/memberDelete")
    fun memberDelete(@RequestBody requestData: StudyMemberDto,authentication: Authentication): ResponseEntity<String>{
        //주인인 스터디인가
        if(!memberService.isMemberOwnerOfStudy(studyService.findStudyByIdOrThrow(requestData.studyId),memberService.findByMemberIdOrThrow(authentication.name.toLong()))) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        studyService.deleteStudyMember(requestData.studyId,requestData.memberId,authentication.name.toLong())
        return ResponseEntity<String>("", HttpStatus.OK);
    }


    //관리자가 아닌 유저가 참가 요청
    @PostMapping("/{id}/participationRequest")
    fun participation(@PathVariable id : Long, @RequestBody message : String, authentication: Authentication): ResponseEntity<String>{
        studyService.participationRequest(id,authentication.name.toLong(),message = message)
        return ResponseEntity<String>("", HttpStatus.OK);
    }

    //관리자 페이지에 리턴되는 스터디 참가 요청들
    @GetMapping("/{id}/requestMemberList")
    fun getParticipationList(@PathVariable id : Long, authentication: Authentication) : ResponseEntity<List<MemberInfoDto>>{
        val memberInfoDtoList = studyService.getParticipationMemberList(id,authentication.name.toLong())
        return ResponseEntity(memberInfoDtoList,HttpStatus.OK)
    }

    @GetMapping("/recommend")
    fun getRecommendStudy(authentication: Authentication) : ResponseEntity<List<StudyResponseDto>>{
        return ResponseEntity<List<StudyResponseDto>>(studyService.getRecommendStudy(authentication.name.toLong()),HttpStatus.OK)
    }


    @PostMapping("/{id}/checkParticipation")
    fun checkParticipation(authentication: Authentication,
                           @PathVariable id : Long,
                           @RequestBody requestParticipationDto : RequestParticipationRequestDto) : ResponseEntity<String>{

        studyService.addStudyMember(studyId = id,
            memberId = requestParticipationDto.requestMemberId,
            ownerMemberId = authentication.name.toLong(),
            isAccept = requestParticipationDto.isAccept)
        return ResponseEntity<String>("",HttpStatus.OK)
    }



    /**
     * deprecated delete기능은 필요없음
     */
//    @DeleteMapping
//    fun delete(@RequestBody studyId: Long,@RequestHeader("Authorization") memberId: Long) {
//        studyService.delete(studyId,memberId)
//    }

//    @PutMapping("/banUser")
//    fun quitUser(@Request)

    @PutMapping("/{studyId}/changeStatus")
    fun changeStatus(@PathVariable(name = "studyId") studyId : Long,
                     @RequestBody newStatus : StudyStateEnum,
                     authentication: Authentication
    ): ResponseEntity<String>{
        studyService.changeStatus(studyId,authentication.name.toLong(),newStatus)
        return ResponseEntity("",HttpStatus.OK)

    }

}
