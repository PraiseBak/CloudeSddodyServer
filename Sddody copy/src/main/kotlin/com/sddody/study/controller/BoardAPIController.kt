package com.sddody.study.controller;


import com.sddody.study.dto.*
import com.sddody.study.service.*
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
class BoardAPIController(
        private val boardService : BoardService,
){

        @PostMapping("/")
        fun createBoard(authentication: Authentication,
                        @RequestPart("boardRequestDto") boardRequestDto : BoardDto,
                        @RequestPart("imageList[]") imageList : List<MultipartFile>? = mutableListOf()
        ) : ResponseEntity<String>{
                boardService.createBoard(boardRequestDto,imageList,authentication.name.toLong())
                return ResponseEntity<String>("", HttpStatus.OK);
        }

        @GetMapping("/{boardId}")
        fun getBoard(@PathVariable boardId: Long) : BoardResponseDto {
                return boardService.getBoard(boardId);
        }

        @PutMapping("/{boardId}")
        fun updateBoard(
                        authentication: Authentication,
                        @PathVariable boardId: Long,
                        @RequestPart("boardRequestDto") boardRequestDto : BoardDto,
                        @RequestPart("imageList[]") imageList : List<MultipartFile>? = mutableListOf()) : ResponseEntity<String>{
                boardService.updateBoard(boardId,authentication.name.toLong(),boardRequestDto,imageList)
                return ResponseEntity<String>("", HttpStatus.OK);
        }

        /**
         * get 일반 보드
         * 이때 한 페이지당 20개씩 가져온다
         * @param pageDto
         */
        @GetMapping("/list")
        fun getBoardList(
                @RequestParam(defaultValue = "1") page : Int,
                @RequestParam(defaultValue = "false") isPortfolio : Boolean,
                ): List<BoardResponseDto> {


                if(isPortfolio) return boardService.getPortfolioList(PageDto(page))
                return boardService.getBoardList(PageDto(page))
        }



        @DeleteMapping("/{boardId}")
        fun deleteBoard(@PathVariable boardId : Long, authentication: Authentication): ResponseEntity<String> {
                boardService.deleteBoard(authentication.name.toLong(),boardId)
                return ResponseEntity<String>("", HttpStatus.OK);
        }

        /**
         * get 포트폴리오 리스트
         * 이때 한 페이지당 20개씩 가져온다
         * @param pageDto
         */
        @GetMapping("/portfolioList")
        fun getPortfolioList(@RequestBody pageDto : PageDto, authentication: Authentication):List<BoardResponseDto> {
                return boardService.getPortfolioList(pageDto)
        }

        @GetMapping("/member/list")
        fun getMemberBoardList(
                @RequestParam(defaultValue = "false") isPortfolio : Boolean,
                @RequestParam(defaultValue = "0") memberId : Long,
                authentication: Authentication,
        ) : ResponseEntity<List<BoardResponseDto>>{
                val id = if(memberId == 0L) authentication.name.toLong() else memberId
                var resultList : List<BoardResponseDto> = boardService.getMemberBoardList(id,isPortfolio)
                return ResponseEntity(resultList,HttpStatus.OK)
        }


        /**
         * 유저가 작성한 스터디 리뷰
         *
         * @param memberId
         * @param authentication
         * @return
         */
        @GetMapping("/member/{memberId}/studyReviewList")
        fun getStudyReviewByMemberId(@PathVariable memberId : Long,authentication: Authentication): List<BoardResponseDto> {
                val actualMemberId = if(memberId == 0L) authentication.name.toLong() else memberId
                return boardService.getStudyReviewListByMemberId(actualMemberId)
        }

        /**
         * 스터디의 스터디의 리뷰를 가져옴
         * @param authentication
         * @return
         */
        @GetMapping("/study/{studyId}/studyReviewList")
        fun getStudyReviewByStudyId(@PathVariable studyId: Long): List<BoardResponseDto> {
                return boardService.getStudyReviewListByStudyId(studyId)
        }




}

