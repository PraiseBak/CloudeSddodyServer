package com.sddody.study.service

import com.sddody.study.entity.BlockedIP
import com.sddody.study.entity.Member
import com.sddody.study.helper.AlarmDto
import com.sddody.study.helper.MemberStatus
import com.sddody.study.helper.StudyStateEnum
import com.sddody.study.repository.BlockedIPRepository
import com.sddody.study.repository.MemberRepository
import com.sddody.study.repository.RequestStudyMemberRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@RequiredArgsConstructor
class AuthService(
    private val memberRepository : MemberRepository,
    private val managerBuilder : AuthenticationManagerBuilder,
    private val blockedIPRepository : BlockedIPRepository,
    private val requestStudyMemberRepository: RequestStudyMemberRepository,
    private val studyService : StudyService
) {



    //블록됐으면 시간 확인후 차단시간 지나지 않았으면 true 리턴
    @Transactional
    fun isBlockedIP(ip : String) : Boolean{
        val blockedIP = blockedIPRepository.findByIp(ip).orElse(null) ?: return false

        return blockedIP.let { it ->
            if(it.blockEndTime.isBefore(LocalDateTime.now())){
                    blockedIP.isBlocked = false;
            }
            blockedIP.isBlocked
        }
    }

    /*
    잘못된 요청인 경우에 block시도 ip 없으면 ip 저장

     */
    @Transactional
    fun blockIfCountExceed(ip: String) : Boolean{
        var blockedIP = blockedIPRepository.findByIp(ip).orElseGet {
            blockedIPRepository.save(
                BlockedIP(ip = ip, blockEndTime = LocalDateTime.now())
            )
        }

        blockedIP?.let {
            blockedIP.addCount()
            if(blockedIP.count % 10 == 0){
                blockedIP.isBlocked = true
                if(blockedIP.count >= 20){
                    blockedIP.blockEndTime = LocalDateTime.now().plusHours(2L)
                }else{
                    blockedIP.blockEndTime = LocalDateTime.now().plusMinutes(30L)
                }
            }
            return blockedIP.isBlocked
        }

        //예외처리로 항상 true반환
        return true
    }

    fun getIP(request: HttpServletRequest): String? {
        var clientIp = request.getHeader("X-Forwarded-For")
        if (clientIp == null || clientIp.isEmpty() || "unknown".equals(clientIp, ignoreCase = true)) {
            clientIp = request.getHeader("Proxy-Client-IP")
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equals(clientIp, ignoreCase = true)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP")
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equals(clientIp, ignoreCase = true)) {
            clientIp = request.remoteAddr
        }

        return clientIp;
    }

    fun getAlarmDtoList(member: Member): List<AlarmDto> {
        return member.alarmList.filterNot { it.isRead }
            .map { it.convertEntityToDto() }
    }

    @Transactional
    fun readAlarm(member : Member) {
        member.alarmList.forEach{
            it.setRead()
        }
    }

    /**
     * 유저 비활성화 - 프로필에 접근할 수 없음
     * 유저 활성화 - 다시 프로필에 접근할 수 있음
     * @param member
     */

    @Transactional
    fun changeMemberStatus(member: Member) {
        member.memberStatus = if(member.memberStatus == MemberStatus.ROLE_DELETED) MemberStatus.ROLE_INFO_VALID else MemberStatus.ROLE_DELETED

        //삭제된 멤버인경우
        if(member.memberStatus == MemberStatus.ROLE_DELETED){
            //자신이 연 스터디들 종료
            member.createdStudyList.forEach{
                studyService.endStudy(it.id!!,member.kakaoId!!)
            }
            //게시물 삭제
            member.studyReviewBoardList.forEach{
                it.isDeleted = true
            }

            member.boardList.forEach{
                it.isDeleted = true
            }

            //참여신청들 삭제
            member.participationRequestStudyList.forEach{
                requestStudyMemberRepository.deleteById(it.id!!)
            }
            member.participationRequestStudyList.clear()

        }
    }
}


