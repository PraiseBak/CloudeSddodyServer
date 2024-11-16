package com.sddody.study.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.sddody.study.helper.SddodyExceptionError
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.Data
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.Arrays
import java.util.Collections




@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        try {
            filterChain.doFilter(request, response)
        } catch (e: SddodyException) {
            e.printStackTrace()
            setErrorResponse(response, e.sddodyExceptionError)
        } catch (e: RuntimeException) {
            e.printStackTrace()
            setErrorResponse(response, SddodyExceptionError.INTERNAL_SERVER_ERROR)
        }
    }

    private fun setErrorResponse(
        response: HttpServletResponse,
        errorCode: SddodyExceptionError
    ) {
        val objectMapper = ObjectMapper()
        response.status = errorCode.code.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse = ErrorResponse(code = errorCode.code.value(), msg = errorCode.msg)
        try {
            response.writer.write(objectMapper.writeValueAsString(errorResponse))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Data
    class ErrorResponse (
        val code: Int? = null,
        val msg: String? = null
    ){
    }
}