package com.sddody.study.common

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SddodyExceptionHandler {


    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(SddodyException::class)
    fun handleSddodyException(e: SddodyException): ResponseEntity<*> {
//        println("${e.sddodyExceptionError.name} ${e.sddodyExceptionError.msg}")
//        logger.error("API error", e.sddodyExceptionError.toString())
//        e.printStackTrace()
        return ResponseEntity<Any>(e.sddodyExceptionError.msg, e.sddodyExceptionError.code)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<*> {
//        println(e.message)
//        println(e.cause?.message)
//        logger.error("API error", e)
//        logger.error("API error", e.message)
        return ResponseEntity<Any>("예상치 못한 에러가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}