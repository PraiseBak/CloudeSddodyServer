package com.sddody.study.controller;


import com.sddody.study.dto.*
import com.sddody.study.service.*
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
class CommentAPIController(
        private val commentService : CommentService,
){

        @PostMapping("/")
        fun createComment(@RequestBody commentDto : CommentDto, authentication: Authentication) : ResponseEntity<String>{
                commentService.createComment(commentDto,authentication.name.toLong())
                return ResponseEntity<String>("", HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        fun deleteBoard(@PathVariable id : Long, authentication: Authentication): ResponseEntity<String> {
                commentService.deleteComment(authentication.name.toLong(),id)
                return ResponseEntity<String>("", HttpStatus.OK);
        }

}
