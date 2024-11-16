package com.sddody.study.controller

import com.sddody.study.dto.PageDto
import com.sddody.study.dto.RequestBlockDto
import com.sddody.study.entity.RequestBlock
import com.sddody.study.service.BlockService
import com.sddody.study.service.MemberService
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
class AdminAPIController (
    private val blockService: BlockService,
    private val memberService: MemberService){

    /**
     * 차단에는 차단 사유필요함
     *
     * @param requestBlock
     * @param authentication
     * @return
     */
    @PostMapping("/blockMember")
    fun blockMemberList(@RequestBody requestBlock: RequestBlockDto, authentication: Authentication) : ResponseEntity<String>{
        blockService.blockUser(requestBlock.memberId,authentication.name.toLong())
        return ResponseEntity("차단완료",HttpStatus.OK)
    }

    @GetMapping("/blockMemberList")
    fun blockMemberList(@RequestBody pageDto: PageDto, authentication: Authentication) : ResponseEntity<MutableList<RequestBlock>> {
        val requestBlockList = blockService.getBlockMemberList(authentication.name.toLong(),pageDto )
        return ResponseEntity(requestBlockList,HttpStatus.OK)
    }
}