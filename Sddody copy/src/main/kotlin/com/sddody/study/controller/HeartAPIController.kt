package com.sddody.study.controller

import com.sddody.study.dto.HeartDto
import com.sddody.study.service.HeartService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/heart")
class HeartAPIController(
    private val heartService: HeartService,
){

    //추천만 취소나 비추천 기능 없음
    @PostMapping("/")
    fun heartPress(@RequestBody heartDto:HeartDto, authentication: Authentication) : ResponseEntity<String> {
        heartService.heartCreate(heartDto,authentication.name.toLong())
        return ResponseEntity<String>("", HttpStatus.OK);
    }
}

