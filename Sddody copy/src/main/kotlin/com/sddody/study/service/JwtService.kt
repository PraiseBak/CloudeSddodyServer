package com.sddody.study.service

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.TokenDto
import com.sddody.study.entity.RefreshToken
import com.sddody.study.helper.MemberStatus
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.repository.BlockedIPRepository
import com.sddody.study.repository.MemberRepository
import com.sddody.study.repository.RefreshTokenRepository
import com.sddody.study.utility.log.logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.log

@Component
@RequiredArgsConstructor
class JwtService(
    private val memberRepository : MemberRepository,
    private val managerBuilder : AuthenticationManagerBuilder,
    private val blockedIPRepository : BlockedIPRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    //1달
    private val ACCESS_TOKEN_EXPIRATION : Long = 86400000L * 30
    //3달
    private val REFRESH_TOKEN_EXPIRATION : Long = 86400000L * 90

    @Value("\${jwt.access.secret}")
    private val SECRET_ACCESS_KEY: String = ""

    @Value("\${jwt.refresh.secret}")
    private val SECRET_REFRESH_KEY: String = ""

    fun generateRefreshToken() : String{
        val key = Keys.hmacShaKeyFor(SECRET_REFRESH_KEY.toByteArray())
        val expiration = Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)
        return Jwts.builder()
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateAccessToken(memberId : Long) : String{
        val key = Keys.hmacShaKeyFor(SECRET_ACCESS_KEY.toByteArray())
        val expiration = Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)
        return Jwts.builder()
            .setSubject(memberId.toString())
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    @Transactional
    fun generateToken(memberId : Long) : TokenDto{
        val accessToken = generateAccessToken(memberId)
        val refreshToken = generateRefreshToken()
        val refreshTokenEntity = refreshTokenRepository.findById(memberId)
            .orElseGet { RefreshToken(kakaoId = memberId, refreshToken = "") }

        refreshTokenEntity.updateRefreshToken(refreshToken)
        refreshTokenRepository.save(refreshTokenEntity)

        //member로 존재한다면 ( 이미 memberInfo까지 set 됐다면 memberId도 포함해서 리턴합니다)
        val existMember = memberRepository.findById(memberId).orElse(null)
        val existMemberId = existMember?.kakaoId
        val nickname = existMember?.nickname
        val isDeleted = existMember?.memberStatus == MemberStatus.ROLE_DELETED
        return TokenDto(accessToken,refreshToken,existMemberId,nickname, isDeleted = isDeleted)
    }

    fun getMemberIdByResolveRefreshToken(refreshToken: String): Long? {
        println("accessToken 유효하지 않음 refreshToken = $refreshToken")
        val storedToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow{SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}

        if(storedToken.refreshToken == refreshToken){
            val key = Keys.hmacShaKeyFor(SECRET_REFRESH_KEY.toByteArray())
            try{
                Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(storedToken.refreshToken)
                return storedToken.kakaoId;
            } catch (ex: io.jsonwebtoken.ExpiredJwtException) {
                throw SddodyException(SddodyExceptionError.REFRESH_TOKEN_INVALIDATE)
            } catch (ex2: Exception) {
                ex2.printStackTrace()
                return null
            }
        }
        return null
    }


    /*
    description expired면 예외 처리 비정상적인 토큰일 경우에는 null을 리턴합니다
     */
    fun getMemberIdByResolveAccessToken(accessToken : String) : Long? {
        val key = Keys.hmacShaKeyFor(SECRET_ACCESS_KEY.toByteArray())
        var memberId : Long? = null
        try{
            memberId = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .body
                .subject
                .toLong()
        } catch (ex: io.jsonwebtoken.ExpiredJwtException) {
            throw SddodyException(SddodyExceptionError.TOKEN_INVALIDATE)
        } catch (ex2: io.jsonwebtoken.JwtException) {
            throw SddodyException(SddodyExceptionError.TOKEN_INVALIDATE)
        }
        return memberId
    }

}
