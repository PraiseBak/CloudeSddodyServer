package com.sddody.study.service

import com.sddody.study.dto.TokenDto
import org.springframework.stereotype.Service

@Service
class TestService (

    private val alarmService: AlarmService,
    private val blockService: BlockService,
    private val studyService: StudyService,
    private val memberService: MemberService,

){
    private final val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMTc5OTkwOTk4IiwiZXhwIjoxNzA2Njg1MzEyfQ.C-ulzKXBgwX6NKxIOeR4IRhT5-qPrOz6n8GcTFXzSS4"
    private final val refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTE4NjkzMTJ9.Jb4B6aRTRB2VLLxFiqHYIBUARNpJozkt84NkSqLlyRQ";
    fun getTokenDto() : TokenDto {
        return TokenDto(accessToken,refreshToken)
    }

    @jakarta.transaction.Transactional
    fun testAlarm() {
        val memberId = "3179990998"
        val member = memberService.findByMemberIdOrThrow(memberId.toLong())
        val study = studyService.getStudy(15L)

        alarmService.studyRequestParticipationResultAlarm(member, studyId =study.studyId, isAllow = true, study.title)
        alarmService.studyRequestParticipationResultAlarm(member, studyId =study.studyId, isAllow = false, study.title)
        alarmService.studyRequestParticipationAlarm(member,15L,study.title)
    }
}

