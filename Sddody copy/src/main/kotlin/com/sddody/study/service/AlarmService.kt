package com.sddody.study.service

import com.sddody.study.entity.Alarm
import com.sddody.study.entity.Member
import com.sddody.study.entity.Study
import com.sddody.study.helper.AlarmCategory
import com.sddody.study.helper.AlarmInfoEnum
import com.sddody.study.repository.AlarmRepository
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

@Service
@Slf4j
class AlarmService(
    val alarmRepository : AlarmRepository,
    val fcmService: FCMService,
) {
    /**
     * 스터디에 대해 참여 요청이 왔을때
     * 스터디의 주인에게 알람이 가야함
     */
    @Transactional
    fun studyRequestParticipationAlarm(member : Member,studyId : Long,studyTitle : String){
        val alarmCategory = AlarmCategory.Study
        var alarmMsg = AlarmInfoEnum.StudyParticipation.infoMsg
        alarmMsg = studyTitle + "\n" + alarmMsg

        val newAlarm = Alarm(info = alarmMsg, alarmCategory = alarmCategory, isRead = false, moveUriId = studyId, member = member)

        val topic = "notification_${member.kakaoId}"
        val savedAlarm = alarmRepository.save(newAlarm)
        member.alarmList.add(savedAlarm)

        fcmService.sendNewNotification(topic,alarmMsg,alarmMsg,studyId)
    }

    /**
     * 스터디에 대해 참여 요청한 결과가 왔을때
     */
    @Transactional
    fun studyRequestParticipationResultAlarm(
        participationMember: Member, studyId: Long,
        isAllow: Boolean = false,
        studyTitle: String
    ){
        val alarmCategory = AlarmCategory.Study
        lateinit var alarmMsg : String
        if(isAllow) alarmMsg = AlarmInfoEnum.StudyParticipationAllow.infoMsg
        else alarmMsg = AlarmInfoEnum.StudyParticipationDenied.infoMsg

        alarmMsg = studyTitle + "\n" + alarmMsg

        val newAlarm = Alarm(info = alarmMsg, alarmCategory = alarmCategory, isRead = false, moveUriId = studyId, member = participationMember)
        val topic = "notification_${participationMember.kakaoId}"

        val savedAlarm = alarmRepository.save(newAlarm)
        participationMember.alarmList.add(savedAlarm)

        fcmService.sendNewNotification(topic,alarmMsg,alarmMsg,studyId)
    }

    //보낸사람을 제외하고 보내야함
    /**
     * 보낸사람을 제외하고
     *
     * @param study
     * @param writeMemberId
     */
    @Transactional
    fun newChatAddAlarm(
        study : Study,
        writeMemberId : Long
    ){
        val alarmCategory = AlarmCategory.Chat
        val studyTitle = study.title
        var alarmMsg = AlarmInfoEnum.ChatAdd.infoMsg
        alarmMsg = "$studyTitle\n$alarmMsg"

        study.studyMemberList.forEach{
            val member = it.member
            //같은 멤버가 아닐떄만 실행한다
            if(member.kakaoId != writeMemberId){
                study.id?.let{
                    val newAlarm = Alarm(info = alarmMsg, alarmCategory = alarmCategory, isRead = false, moveUriId = it, member = member)
                    val topic = "notification_${member.kakaoId}"
                    val savedAlarm = alarmRepository.save(newAlarm)
                    member.alarmList.add(savedAlarm)
                    fcmService.sendNewNotification(topic,alarmMsg,alarmMsg,studyId = it)
                }
            }
        }





    }


}
