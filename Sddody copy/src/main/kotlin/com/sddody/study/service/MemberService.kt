package com.sddody.study.service;

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.GithubUserProfileDto
import com.sddody.study.dto.MemberDto
import com.sddody.study.dto.MemberInfoDto
import com.sddody.study.dto.TokenDto
import com.sddody.study.entity.Member
import com.sddody.study.entity.PrevSignupMember
import com.sddody.study.entity.Study
import com.sddody.study.helper.Authority
import com.sddody.study.helper.MemberStatus
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.repository.LocationRepository
import com.sddody.study.repository.MemberRepository
import com.sddody.study.repository.PrevSignupMemberRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MemberService(
    private val memberRepository: MemberRepository,
    private val prevSignupMemberRepository: PrevSignupMemberRepository,
    private val jwtService: JwtService,
    private val locationRepository: LocationRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun start(memberId: Long) : TokenDto{
        if(prevSignupMemberRepository.findById(memberId).isEmpty) {
            prevSignupMemberRepository.save(PrevSignupMember(memberId))
        }
        return jwtService.generateToken(memberId)
    }


    fun isExistMember(memberId: Long) : Boolean {
        return memberRepository.existsById(memberId)
    }

    fun isDeletedMember(memberId : Long) : Boolean{
        return findByMemberIdOrThrow(memberId).memberStatus == MemberStatus.ROLE_DELETED
    }

    fun findByMemberIdOrThrow(memberId: Long): Member {
        return this.memberRepository.findById(memberId).orElseThrow { SddodyException(SddodyExceptionError.CANNOT_FIND_USER) }
    }


    fun findByMemberNickname(nickname: String): Member {
        return this.memberRepository.findByNickname(nickname).orElseThrow{ SddodyException(SddodyExceptionError.CANNOT_FIND_USER) };
    }

    fun isExistPrevSignupMember(memberId: Long): Boolean {
        return prevSignupMemberRepository.existsById(memberId)
    }

    /**
     * 처음 회원가입시와 유저 정보 수정에 모두 사용됨
     * @param memberDto
     * @param memberId
     */
    @Transactional
    fun setMemberInfo(memberDto: MemberDto, memberId: Long, profileImgSrc: String) {
        if(!isExistPrevSignupMember(memberId)){
            throw SddodyException(SddodyExceptionError.CANNOT_FIND_USER)
        }

        if(isExistNickname(memberDto.nickname)){
            //현재 닉네임과 다른경우에만 중복에러
            if(findByMemberIdOrThrow(memberId).nickname != memberDto.nickname){
                throw SddodyException(SddodyExceptionError.DUPLICATE_NICKNAME)
            }
        }

        memberDto.memberLocation = memberDto.memberLocation.let { location ->
            locationRepository.save(location)
        }

        var member = memberRepository.findById(memberId)
        memberDto.kakaoId = memberId
        if(member.isEmpty){
            val newMember = MemberDto.toMember(memberDto, profileImgSrc)
            memberRepository.save(newMember)
        }else{
            memberDto.setIsEnable(member.get().memberStatus == MemberStatus.ROLE_DELETED)
            member.get().updateMemberInfo(MemberDto.toMember(memberDto,profileImgSrc))
        }
    }

    private fun isExistNickname(nickname: String): Boolean {
        return memberRepository.findByNickname(nickname).isPresent
    }

    fun isMemberContainsStudy(study: Study, member: Member) : Boolean{
        for(studyMember in study.studyMemberList){
            if(studyMember.member.nickname == member.nickname){
                return true
            }
        }
        return false
    }

    fun ifAlreadyMemberParticipationThenThrow(study: Study,participationMemberId : Long){
        val participant = study.participationRequestMemberList.find { it.member.kakaoId == participationMemberId }
        participant?.let {
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }
    }

    fun isMemberOwnerOfStudy(study: Study,member: Member): Boolean {
        return member.kakaoId == study.studyRoomOwner.kakaoId;
    }

    fun isUserAdmin(adminId: Long): Boolean {
        return findByMemberIdOrThrow(adminId).authority == Authority.ROLE_ADMIN

    }

    fun getMemberInfoDto(memberId: Long): MemberInfoDto {
        return findByMemberIdOrThrow(memberId).getMemberInfoDto()
    }

    @Transactional
    fun setGithubProfileDto(memberId: Long, githubUserProfileDto: GithubUserProfileDto) {
        var member = findByMemberIdOrThrow(memberId)
        member.updateGithubNickname(githubUserProfileDto.name)
    }
}
