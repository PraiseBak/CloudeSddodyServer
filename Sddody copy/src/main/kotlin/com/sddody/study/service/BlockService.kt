package com.sddody.study.service

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.PageDto
import com.sddody.study.dto.RequestBlockDto
import com.sddody.study.entity.RequestBlock
import com.sddody.study.helper.Authority
import com.sddody.study.helper.PageHelper
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.repository.RequestBlockRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class BlockService (
    private val requestBlockRepository : RequestBlockRepository,
    private val memberService : MemberService){

    /**
     * 차단 요청시에 사용되는 엔티티
     * 나중에 직접 사유를 확인하고 해당되면 block합니다
     *
     * @param memberId
     * @param requestMemberId
     */
    @Transactional
    fun requestBlock(requestBlockDto: RequestBlockDto, requestMemberId: Long) {
        //스스로에게 차단신청할 수 없음
        if(requestBlockDto.memberId == requestMemberId) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        val member = memberService.findByMemberIdOrThrow(requestBlockDto.memberId)

        requestBlockRepository.save(RequestBlock(member = member, reason = requestBlockDto.reason))
    }


    /**
     * 어드민이 유저 차단을 하는 메서드
     * 차단시에도 차단 사유가 필요하다
     * @param memberId
     * @param adminId
     */
    @Transactional
    fun blockUser(memberId: Long, adminId : Long) {
        //어드민만 해당 요청가능
        if(memberService.findByMemberIdOrThrow(adminId).authority != Authority.ROLE_ADMIN) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        memberService.findByMemberIdOrThrow(memberId).setBlock()
    }

    fun getBlockMemberList(adminId: Long,pageDto: PageDto): MutableList<RequestBlock> {
        //어드민 권한 없으면 throw
        if(!memberService.isUserAdmin(adminId)) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        val pageRequest = PageHelper.getPageRequestDescending(pageDto)

        return requestBlockRepository.findAll(pageRequest).toList()

    }




}
