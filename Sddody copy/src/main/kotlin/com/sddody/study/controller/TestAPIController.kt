package com.sddody.study.controller

import com.sddody.study.service.*
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
class TestAPIController (
    private val testService: TestService,

    ){

    @GetMapping("/")
    fun testAlarm() : ResponseEntity<String> {
        testService.testAlarm()
        return ResponseEntity("",HttpStatus.OK)
    }


    @GetMapping("/aop")
    fun testAOP() : ResponseEntity<String> {
        return ResponseEntity("",HttpStatus.OK)

    }
}