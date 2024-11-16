package com.sddody.study.service

import com.sddody.study.common.SddodyException
import com.sddody.study.helper.SddodyExceptionError
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import com.sddody.study.entity.PrevSignupMember


@Service
class KakaoService(
    @Value("\${kakao.user.url}") private val KAKAO_USER_URL: String,
) {
    fun getMemberIdFromKakao(accessToken: String?): Long? {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val entity = HttpEntity(null, headers)
        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(
            KAKAO_USER_URL,
            HttpMethod.POST,
            entity,
            Map::class.java // 응답을 Map으로 처리
        )

        return if (response.statusCode == HttpStatus.OK) {
            val body = response.body
            // body를 통해 필요한 값을 가져옴
            val memberId = body?.get("id")?.toString()?.toLongOrNull()
            memberId
        } else {
            throw SddodyException(SddodyExceptionError.INTERNAL_SERVER_ERROR)
        }
    }

}