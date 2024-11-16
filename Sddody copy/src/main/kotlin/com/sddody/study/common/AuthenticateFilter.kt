package com.sddody.study.common

import com.sddody.study.entity.Member
import com.sddody.study.helper.Authority
import com.sddody.study.service.MemberService
import com.sddody.study.utility.log.logger
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class AuthenticateFilter (private val memberService: MemberService){
    val role : MutableList<SimpleGrantedAuthority> = mutableListOf()
    fun getAuthentication(memberId : Long): Authentication {
        val role : MutableList<SimpleGrantedAuthority> = mutableListOf()
        val principal: UserDetails = User(memberId.toString(), "", role)

        if(memberService.isExistMember(memberId)){
            val member : Member = memberService.findByMemberIdOrThrow(memberId);
            role.add(SimpleGrantedAuthority(member.authority.name))
        }else if(memberService.isExistPrevSignupMember(memberId)){
            role.add(SimpleGrantedAuthority(Authority.ROLE_ANONYMOUS.name))
        }

        return UsernamePasswordAuthenticationToken(principal, "", role)
    }
}