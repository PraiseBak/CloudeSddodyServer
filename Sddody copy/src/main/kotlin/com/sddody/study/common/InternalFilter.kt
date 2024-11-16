package com.sddody.study.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.sddody.study.dto.TokenDto
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.service.AuthService
import com.sddody.study.service.JwtService
import com.sddody.study.service.MemberService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@RequiredArgsConstructor
class InternalFilter(
    private val memberService: MemberService,
    private val authenticateFilter: AuthenticateFilter,
    private val authService : AuthService,
    private val jwtService: JwtService,
) : OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if(request.requestURI.contains("/api/test")) {
            filterChain.doFilter(request, response)
            return
        }

        val ip = checkIPExistAndGet(request,response) ?: return


        if(authService.isBlockedIP(ip)){
            logger.info("이미 차단된 계정입니다 관리자에게 문의해주세요!")
            throw SddodyException(SddodyExceptionError.BLOCKED_USER)
        }

        //시작하기
        if(request.requestURI.contains("/api/auth/start")) {
            filterChain.doFilter(request, response)
            return
        }

        //만약 휴면계정이면 휴면요청만 허용가능
        val tokenDto = getTokenDto(request)

        logger.info("reqURI" + request.requestURI + " " +tokenDto.toString())

        if(!jwtProcess(request,response,tokenDto)){
            if(tokenDto.accessToken == null){
                throw SddodyException(SddodyExceptionError.BAD_REQUEST)
            }

            //jwt 토큰이 비정상적이므로 차단시도 + 1
            logger.info("비정상적인 토큰으로 차단 시도 +1")
            if (authService.blockIfCountExceed(ip)) {
                logger.info("차단된 계정입니다")
//                response.sendError(HttpServletResponse.SC_FORBIDDEN, "차단된 계정입니다 관리자에게 문의해주세요!")
                throw SddodyException(SddodyExceptionError.BLOCKED_USER)
            }else{
                throw SddodyException(SddodyExceptionError.TOKEN_INVALIDATE)
            }
        }

        //토큰값을 전송하는 경우
        if(response.status == HttpStatus.ACCEPTED.value()){
            response.status = HttpStatus.OK.value()
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun getTokenDto(request: HttpServletRequest) : TokenDto{
        val accessToken = request.getHeader("Authorization")?.let {  it.split(" ")[1]}
        val refreshToken = request.getHeader("X-REFRESH-TOKEN")
        return TokenDto(accessToken,refreshToken)
    }

    private fun checkIPExistAndGet(request: HttpServletRequest, response: HttpServletResponse) : String?{
        val ip = authService.getIP(request)
        if(ip == null){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "ip가 존재하지 않습니다")
            return null
        }
        return ip
    }


    /**
     * 필터에서 jwt 처리를 하는 함수입니다
     * true면 유효한 jwt 토큰
     * false면 유효하지 않은 jwt 토큰인 것이므로 차단 시도 +1를 합니다
     *
     * @throws SddodyException(SddodyExceptionError.ACCESS_TOKEN_INVALIDATE)
     * @throws SddodyException(SddodyExceptionError.REFRESH_TOKEN_INVALIDATE)
     * @param request
     * @param response
     * @return Boolean
     */
    private fun jwtProcess(request: HttpServletRequest,response: HttpServletResponse,tokenDto : TokenDto) : Boolean {
        return tokenDto.accessToken?.let { accessToken ->
            jwtService.getMemberIdByResolveAccessToken(accessToken)?.let { existMemberId ->
                val isExistMember = memberService.isExistMember(existMemberId)

                //삭제된 유저면 휴면계정 해제만 가능
                if(isExistMember && memberService.isDeletedMember(existMemberId) && !(request.requestURI.contains("/api/auth/changeMemberStatus"))){
                    false
                } else if (isExistMember || request.requestURI.contains("/api/auth/memberInfo")) {
                    val authentication = authenticateFilter.getAuthentication(existMemberId)
                    SecurityContextHolder.getContext().authentication = authentication


                    true
                }else {
                    false
                }
            } ?: false
        } ?: run {
            tokenDto.refreshToken?.let { refreshToken ->
                jwtService.getMemberIdByResolveRefreshToken(refreshToken)?.let { existMemberId ->
                    val newTokenDto = jwtService.generateToken(existMemberId)
                    sendTokenDtoToResponse(newTokenDto, response)
                    true
                } ?: false
            } ?: false

        }
    }

    private fun sendTokenDtoToResponse(newTokenDto: TokenDto, response: HttpServletResponse) {
        val objectMapper = ObjectMapper()
        val tokenDtoJson = objectMapper.writeValueAsString(newTokenDto)

        println("재발급 = " + newTokenDto.accessToken + "," + newTokenDto.refreshToken)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpStatus.ACCEPTED.value()
        response.writer.write(tokenDtoJson)

    }
}
