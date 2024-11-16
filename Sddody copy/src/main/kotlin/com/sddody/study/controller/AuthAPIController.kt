package com.sddody.study.controller;


import com.sddody.study.common.SddodyException
import com.sddody.study.dto.*
import com.sddody.study.helper.AlarmDto
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.service.*
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
class AuthAPIController(
    private val authService: AuthService,
    @Value("\${kakao.user.url}") private val KAKAO_USER_URL: String,
    @Value("\${kakao.restapi.key}") private val KAKAO_REST_API_KEY: String,
    @Value("\${kakao.redirect.url}") private val KAKAO_REDIRECT_URL: String,
    @Value("\${kakao.client.secret}") private val KAKAO_CLIENT_SECRET: String,
    private val memberService: MemberService,
    private val kakaoService: KakaoService,
    private val blockService: BlockService,
    private val imageService: ImageService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /*
    카카오 로그인으로 시작하기
     */
    @PostMapping("/start")
    fun getUserInfoFromKakao(@RequestBody tokenDto: TokenDto): ResponseEntity<TokenDto> {
        val memberId = kakaoService.getMemberIdFromKakao(tokenDto.accessToken)
        val newTokenDto = memberId?.let {
            memberService.start(memberId)
        } ?: throw SddodyException(SddodyExceptionError.AUTHORIZATION_FAIL)

        logger.info("발급된 start member info id= $memberId ${newTokenDto.nickname} ${newTokenDto.accessToken} 리프래쉬토큰 = ${newTokenDto.refreshToken}")
        return ResponseEntity<TokenDto>(newTokenDto, HttpStatus.OK);

    }

    @PutMapping("/memberInfo", consumes = ["multipart/form-data"])
    fun getUserInfoFromKakao(@RequestPart("memberDto") memberDto : MemberDto,
                             @RequestPart("profileImage", required = false) profileImage : MultipartFile?,
                             authentication: Authentication): ResponseEntity<TokenDto> {
        var savedImage = ""
        profileImage?.let {
            savedImage = imageService.saveImage(profileImage)
        }
        val memberId = authentication.name.toLong()
        memberService.setMemberInfo(memberDto,memberId,savedImage);
        return ResponseEntity<TokenDto>(TokenDto(memberId = memberId, nickname = memberDto.nickname, isDeleted = memberDto.isDeleted), HttpStatus.OK);
    }

    @GetMapping("/checkTokenValid")
    fun isValidToken(): ResponseEntity<Void> {
        return ResponseEntity<Void>(HttpStatus.OK);
    }



    @PostMapping("/requestBlock")
    fun requestBlock(@RequestBody requestBlockDto: RequestBlockDto,authentication: Authentication){
        blockService.requestBlock(requestBlockDto,authentication.name.toLong())
    }


    @GetMapping("/memberInfo/{id}")
    fun getMemberInfo(authentication: Authentication,@PathVariable id : Long): ResponseEntity<MemberInfoDto> {
        //id가 0이면 자기자신
        val memberId = if(id == 0L) authentication.name.toLong() else id
        return ResponseEntity<MemberInfoDto>(memberService.getMemberInfoDto(memberId),HttpStatus.OK)
    }

    @PostMapping("/githubInfo")
    fun setGithubInfo(@RequestBody githubUserProfileDto: GithubUserProfileDto, authentication: Authentication){
        memberService.setGithubProfileDto(authentication.name.toLong(),githubUserProfileDto)
    }

    @GetMapping("/notification")
    fun getNotification(authentication: Authentication) : ResponseEntity<List<AlarmDto>> {
        val memberId = authentication.name.toLong()
        val member = memberService.findByMemberIdOrThrow(memberId)
        val alarmDtoList = authService.getAlarmDtoList(member)
        return ResponseEntity<List<AlarmDto>>(alarmDtoList.reversed(),HttpStatus.OK)
    }

    @PutMapping("/readAlarm")
    fun readAlarm(authentication: Authentication) : ResponseEntity<String>{
        val memberId = authentication.name.toLong()
        val member = memberService.findByMemberIdOrThrow(memberId)
        authService.readAlarm(member)
        return ResponseEntity("",HttpStatus.OK)
    }

    /**
     * 회원탈퇴
     */
    @PutMapping("/changeMemberStatus")
    fun changeMemberStatus(authentication: Authentication) : ResponseEntity<String>{
        val memberId = authentication.name.toLong()
        val member = memberService.findByMemberIdOrThrow(memberId)
        authService.changeMemberStatus(member)
        return ResponseEntity("",HttpStatus.OK)
    }



}