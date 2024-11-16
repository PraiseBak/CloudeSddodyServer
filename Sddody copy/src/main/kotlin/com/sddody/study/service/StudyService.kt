package com.sddody.study.service;

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.*
import com.sddody.study.entity.*
import com.sddody.study.helper.*
import com.sddody.study.repository.RequestStudyMemberRepository
import com.sddody.study.repository.StudyMemberRepository
import com.sddody.study.repository.StudyRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@RequiredArgsConstructor
class StudyService(
    private val studyRepository: StudyRepository,
    private val memberService: MemberService,
    private val chatRoomService: ChatRoomService,
    private val studyMemberRepository: StudyMemberRepository,
    private val imageService: ImageService,
    private val alarmService : AlarmService,
    private val fcmService: FCMService,
    private val requestStudyMemberRepository : RequestStudyMemberRepository
) {

//    @Transactional
//    fun add(study: Study, member: Member) {
////        study.memberList.add(member)
////        studyRepository.save(study)
//    }


    @Transactional
    fun openStudy(openStudyDto: OpenStudyDto, ownerMemberId: Long, imageList: List<MultipartFile>) {
        val ownerMember = memberService.findByMemberIdOrThrow(ownerMemberId);
        val study = Study.convertDtoToEntityWithOwnerMember(openStudyDto,ownerMember);
        val studyMember = StudyMember(study = study, member = ownerMember)

        studyMemberRepository.save(studyMember)
        ownerMember.studyMemberList.add(studyMember)
        study.studyMemberList.add(studyMember)

        val savedStudy = studyRepository.save(study)

        val savedImageList : MutableList<Image> = mutableListOf()
        imageList.forEach{
            val studyImage = Image(src = imageService.saveImage(it), study = study)
            savedImageList.add(studyImage)
            imageService.save(studyImage)
        }

        savedStudy.saveImageList(savedImageList)

        ownerMember.createdStudyList.add(savedStudy)
        study.updateMainChatRoom(chatRoomService.createMainChatRoom(study))
        fcmService.sendNotificationToChatRoom("member_${ownerMemberId.toString()}","채팅방 구독","채팅방 구독")
    }

    fun findStudyByIdOrThrow(studyId : Long) : Study{
        return studyRepository.findById(studyId).orElseThrow{SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
    }


    /**
     * 참가 신청한 경우
     * 참가 오픈 -> 바로 멤버에 추가
     * 참가 오픈 x -> 관리자가 직접 걸러냄
     * @param studyId
     * @param participationMemberId
     */
    @Transactional
    fun participationRequest(studyId: Long, participationMemberId: Long, message: String){
        val study = findStudyByIdOrThrow(studyId);

        if(study.currentState != StudyStateEnum.PREPARATION){
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }

        val participationMember = memberService.findByMemberIdOrThrow(participationMemberId)

        //이미 참가된 유저면 throw
        if(memberService.isMemberContainsStudy(study, memberService.findByMemberIdOrThrow(participationMemberId))){
            println("이미 참가한 유저")
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }

        //바로 참가라면 바로 addStudyMember
        if(study.isOpenToEveryone) {
            addStudyMember(studyId,participationMemberId,null)
            return
        }

        //아니면 참가신청

        //이미 참가신청했으면 throw
        memberService.ifAlreadyMemberParticipationThenThrow(study,participationMemberId)

        //아니라면 팀장에게 참가 요청
        val requestStudyMember = RequestStudyMember(member=participationMember, study = study, message = message)

        println("${study.studyRoomOwner.kakaoId} ${participationMember.kakaoId}")
        alarmService.studyRequestParticipationAlarm(study.studyRoomOwner,studyId,study.title)

        study.participationRequestMemberList.add(requestStudyMember)
        participationMember.participationRequestStudyList.add(requestStudyMember)
    }


    private fun isStudyEndedThenThrow(study: Study){
        if(study.currentState == StudyStateEnum.END) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
    }

    //항상 관리자만 삭제가능
    @Transactional
    fun deleteStudyMember(studyId : Long, memberId : Long,ownerMemberId: Long?){
        val study = findStudyByIdOrThrow(studyId)
        isStudyEndedThenThrow(study)
        val member = memberService.findByMemberIdOrThrow(memberId)

        //관리자는 삭제불가능
        if((member.kakaoId != study.studyRoomOwner.kakaoId) && memberService.isMemberContainsStudy(study, member)){
            study.studyMemberList.forEachIndexed{index , it ->
                if(it.member.kakaoId == member.kakaoId){
                    studyMemberRepository.deleteById(it.id!!)
                    study.studyMemberList.removeAt(index)
                    return
                }
            }
        }else{
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }
    }


    /**
     * 관리자가 참가 허용한 경우 혹은 오픈된 채팅방일 경우
     * 참가가 open된 채팅방이거나 관리자가 허용한 참가신청자일경우 참가자 추가
     * @param studyId
     * @param memberId
     */
    @Transactional
    fun addStudyMember(studyId : Long, memberId : Long,ownerMemberId: Long?,isAccept: Boolean = true){
        val study = findStudyByIdOrThrow(studyId);

        if(study.currentState != StudyStateEnum.PREPARATION){
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }

        val member = memberService.findByMemberIdOrThrow(memberId)

        //관리자가 참가 허용한 경우
        if(ownerMemberId != null){
            //관리자가 아닌 경우 throw
            if(!memberService.isMemberOwnerOfStudy(study,memberService.findByMemberIdOrThrow(ownerMemberId))) throw SddodyException(SddodyExceptionError.AUTHORIZATION_FAIL)
        }


        //이미 참가된 유저면 throw
        if(memberService.isMemberContainsStudy(study, member)){
                throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }
        //참가신청한 유저아니면 throw
        study.participationRequestMemberList.find {it.member.kakaoId == memberId} ?: throw SddodyException(SddodyExceptionError.BAD_REQUEST)

        //범위 벗어나면 throw
        if(!isNotExceedStudyMemberList(study)) throw SddodyException(SddodyExceptionError.BAD_REQUEST)


        if(isAccept){
            val studyMember = StudyMember(study = study, member = member)
            study.studyMemberList.add(studyMember)
            member.studyMemberList.add(studyMember)
            study.mainChatRoom?.chatReadMap?.set(memberId,0L)
            fcmService.sendNotificationToChatRoom("member_${ownerMemberId.toString()}","채팅방 구독","채팅방 구독")
        }

        //스터디 참가 요청
        alarmService.studyRequestParticipationResultAlarm(participationMember = member,studyId, isAllow = isAccept,studyTitle = study.title)

        //참여하던 거절하던 목록에서는 삭제된다
        deleteRequestStudyMember(studyId,memberId)
    }

    @Transactional
    fun deleteRequestStudyMember(studyId : Long,memberId : Long) {

        var study = findStudyByIdOrThrow(studyId)

        study.participationRequestMemberList.forEachIndexed{index, it ->
            if(it.member.kakaoId == memberId){
                study.participationRequestMemberList.removeAt(index)
                requestStudyMemberRepository.deleteById(it.id!!)
                return
            }
        }

        throw SddodyException(SddodyExceptionError.BAD_REQUEST)
    }

    fun isNotExceedStudyMemberList(study: Study) : Boolean{
        return study.studyMemberList.size < study.maxStudyMemberNum
    }

    @Transactional
    fun modify(modifyStudyDto: ModifyStudyDto, memberId: Long, imageList: List<MultipartFile>) {
        val study = findStudyByIdOrThrow(modifyStudyDto.studyId)
        isStudyEndedThenThrow(study)
        val member = memberService.findByMemberIdOrThrow(memberId);
        if(!memberService.isMemberOwnerOfStudy(study,member)) throw SddodyException(SddodyExceptionError.AUTHORIZATION_FAIL)

        val savedImageList : MutableList<Image> = mutableListOf()
        imageList.forEach{
            val studyImage = Image(src = imageService.saveImage(it), study = study)
            savedImageList.add(studyImage)
            imageService.save(studyImage)
        }

        study.modifyStudy(modifyStudyDto,savedImageList)
    }

    /**
     *
     * @param studyId
     * @param ownerMemberId
     */
    @Transactional
    fun endStudy(studyId: Long, ownerMemberId: Long) {
        val study = findStudyByIdOrThrow(studyId)
        if(!memberService.isMemberOwnerOfStudy(study, member = memberService.findByMemberIdOrThrow(ownerMemberId))){
            throw SddodyException(SddodyExceptionError.AUTHORIZATION_FAIL)
        }

        study.mainChatRoom?.id?.let {
            chatRoomService.endChatRoom(it,ownerMemberId)
        }
        study.setStatusClose()
    }

    fun getStudy(id: Long) : StudyResponseDto{
        val study = findStudyByIdOrThrow(id)
        return study.convertEntityToResponseDto()
    }

    //해당 유저가 쓴 스터디 가져오기
    fun getMemberStudyList(orderType: StudyOrderType, memberId : Long) : MutableList<StudyResponseDto> {
        val member = memberService.findByMemberIdOrThrow(memberId)
        val resultList : MutableList<StudyResponseDto> = mutableListOf()
        member.studyMemberList.forEach{
            resultList.add(it.study.convertEntityToResponseDto(isGetLocation = true))
        }
        return resultList
    }

    fun getStudyList(page: Int, orderType: StudyOrderType,memberId :  Long): MutableList<StudyResponseDto> {
//        val pageRequest = PageHelper.getPageRequestDescending(PageDto(page))
        val member = memberService.findByMemberIdOrThrow(memberId)
        //열려있는것만 탐색
        val studyList = when(orderType){
            StudyOrderType.NEARBY -> {
//                val tmpList = studyRepository.findAllByCurrentState(pageRequest,StudyStateEnum.PREPARATION)
                val tmpList = studyRepository.findAllByCurrentState(StudyStateEnum.PREPARATION)
                orderByNearBy(tmpList,member.memberLocation)
            }
            StudyOrderType.MOST_FIT -> {
                val tmpList = studyRepository.findAllByCurrentState(StudyStateEnum.PREPARATION)
                orderByMostFit(tmpList = tmpList.toList(),member)
            }
            StudyOrderType.RECENT_CREATE -> {
                val tmpList = studyRepository.findAllByCurrentState(StudyStateEnum.PREPARATION)
                orderByCreatedDate(tmpList)
            }
            StudyOrderType.STATISTIC -> {
                val tmpList = studyRepository.findAllByCurrentState(StudyStateEnum.PREPARATION)
                orderByStatistic(studyList = tmpList.toList(),member)
            }

            StudyOrderType.END -> {
                val tmpList = studyRepository.findAllByCurrentState(StudyStateEnum.END)
                orderByCreatedDate(tmpList)
            }

            StudyOrderType.STUDYING-> {
                val tmpList = studyRepository.findAllByCurrentState(StudyStateEnum.STUDYING)
                orderByCreatedDate(tmpList)
            }

        }

        val studyDtoList : MutableList<StudyResponseDto> = mutableListOf()

        studyList.forEach{
            studyDtoList.add(
                it.convertEntityToResponseDto()
            )
        }

        return studyDtoList
    }

    private fun orderByCreatedDate(tmpList: List<Study>): List<Study> {
        return tmpList.sortedByDescending { it.createdAt }
    }

    //가까운순으로 출력
    private fun orderByNearBy(tmpList: List<Study>,memberLocation : Location): List<Study> {
        //(b-a)^2 + (d-c)^2
        val sortedList = tmpList.sortedWith(compareBy {
            var result = Double.MAX_VALUE
            it.location?.let {
                val x = Math.pow(it.latitude - memberLocation.latitude, 2.0)
                val y = Math.pow(it.longitude - memberLocation.longitude, 2.0)
                result = x+y
            }

            result
        } )
        return sortedList
    }

    /*
    통계 기반
     */
    private fun orderByStatistic(studyList : List<Study>,member : Member) : List<Study>{
        val resultList = studyList.sortedByDescending {

            //수준과 직종이 같을수록 높은값
            var weightBaseValue  = if(it.devLevel == member.devLevel) 1.0 else 0.5
            weightBaseValue += if(it.devYear == member.devYear) 1.0 else 0.5

            var resultWeight = 0.0
            it.studyTechStack.forEach{
                if(member.devYear == DevYearEnum.BEGINNER){
                    resultWeight += weightBaseValue * (BeginnerStatisticEnum.entries[it.ordinal].weight)
                }else{
                    resultWeight += weightBaseValue * (ExpertStatisticEnum.entries[it.ordinal].weight)
                }
            }
            resultWeight
        }

        return resultList
    }

    //값이 높을수록 일치하지 않는 것
    //개발 수준에 맞춰서 통계값 내기
    private fun orderByMostFit(tmpList : List<Study>,member : Member) : List<Study>{
        val sortedList = tmpList.sortedWith(compareBy {
            //id가져와서 일치하는지
            var isSameField = 1
            var isSameStack = 1
            member.interestField.forEach{studyField ->
                if(studyRepository.findByIdAndCurrentStateAndStudyField(
                        it.id!!,
                        StudyStateEnum.PREPARATION,
                        studyField = studyField
                    ).isNotEmpty()
                ){
                    isSameField = 0
                    return@forEach
                }
            }

            member.haveExperienceTechStack.forEach{techStack ->
                if(studyRepository.findByIdAndCurrentStateAndStudyTechStack(
                        it.id!!,
                        StudyStateEnum.PREPARATION,
                        techStack
                    ).isNotEmpty()
                ){
                    isSameStack = 0
                    return@forEach
                }
            }

            isSameField + isSameStack
        })
        return sortedList
    }



    fun getStudyLocationInfoList(isOnlyNearbyRecommend: Boolean, memberId: Long): List<StudyResponseDto> {

        if(isOnlyNearbyRecommend){
            val member = memberService.findByMemberIdOrThrow(memberId)
            val memberLocation = member.memberLocation

            val tmpStudyList = studyRepository.findAllByCurrentState(StudyStateEnum.PREPARATION)

            val nearbyList = tmpStudyList
                .filter {
                    //기본값 30km로
                    it.location.isNearBy(memberLocation)
                }

            //리스트가없으면 100km로
            if(nearbyList.isEmpty()){
                tmpStudyList
                    .filter {
                        it.location.isNearBy(memberLocation, distance = 100)
                    }
            }

            val orderByFitList = orderByMostFit(nearbyList, member)
            val resultList = if (orderByFitList.size >= 30) {
                orderByFitList.subList(0, 10)
            } else {
                orderByFitList // nearbyList 크기가 10 미만이면 그대로 반환
            }.map {
                it.convertEntityToResponseDto(true)
            }

            return resultList.toList()

        }
        return studyRepository.findAllByCurrentState(StudyStateEnum.PREPARATION).map{
            it.convertEntityToResponseDto(true)
        }

    }

    fun getRecommendStudy(memberId: Long): List<StudyResponseDto> {
        val pageRequest = PageHelper.getPageRequestDescending(PageDto(1))
        val member = memberService.findByMemberIdOrThrow(memberId)
        val tmpList = studyRepository.findAllByCurrentState(pageRequest,StudyStateEnum.PREPARATION)
        val orderedList = orderByStatistic(tmpList.toList(),member)
        val resultList = orderByMostFitDevInfo(orderedList, member)

        val substringList =  resultList.map {
            it.convertEntityToResponseDto()
        }

        return substringList.subList(0,Math.min(3,substringList.size))
    }

    private fun orderByMostFitDevInfo(
        orderedList: List<Study>,
        member: Member
    ): List<Study> {

        val sortedByDevInfoList = orderedList.sortedWith(compareBy {
            //값이 낮을수록 적합함
            var isSameDevLevel = 1
            var isSameDevYear = 1
            member.devLevel.let { devLevel ->
                if (studyRepository.existsByIdAndCurrentStateAndDevLevel(
                        it.id!!,
                        StudyStateEnum.PREPARATION,
                        devLevel
                    )
                ) {
                    isSameDevLevel = 0
                    return@let
                }
            }


            member.devYear.let { devYear ->
                if (studyRepository.existsByIdAndCurrentStateAndDevYear(
                        it.id!!,
                        StudyStateEnum.PREPARATION,
                        devYear
                    )
                ) {
                    isSameDevYear = 0
                    return@let
                }
            }

            isSameDevYear + isSameDevLevel
        })

        return sortedByDevInfoList
    }


    fun getParticipationMemberList(studyId: Long, memberId: Long): List<MemberInfoDto> {
        val study = findStudyByIdOrThrow(studyId)
        //관리자만 허용가능
        if(!memberService.isMemberOwnerOfStudy(study,memberService.findByMemberIdOrThrow(memberId))) throw SddodyException(SddodyExceptionError.AUTHORIZATION_FAIL)

        return study.participationRequestMemberList.map {
            it.member.getMemberInfoDto()
        }
    }

    @Transactional
    fun changeStatus(studyId: Long, memberId: Long, newStatus: StudyStateEnum) {
        val study = findStudyByIdOrThrow(studyId)
        isStudyEndedThenThrow(study)
        //관리자만 변경가능
        if(!memberService.isMemberOwnerOfStudy(study,memberService.findByMemberIdOrThrow(memberId))) throw SddodyException(SddodyExceptionError.AUTHORIZATION_FAIL)

        val curStatus = study.currentState

        study.updateStatus(newStatus)
    }


}
