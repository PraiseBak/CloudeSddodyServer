package com.sddody.study.service

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.BoardDto
import com.sddody.study.dto.BoardResponseDto
import com.sddody.study.dto.PageDto
import com.sddody.study.entity.Board
import com.sddody.study.entity.Image
import com.sddody.study.helper.PageHelper
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.helper.StudyStateEnum
import com.sddody.study.repository.BoardRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.jvm.optionals.getOrElse

@Service
@RequiredArgsConstructor
class BoardService(
    private val boardRepository: BoardRepository,
    private val memberService: MemberService,
    private val imageService : ImageService,
    private val studyService: StudyService,

    ){
    @Transactional
    fun createBoard(boardDto: BoardDto, imageList: List<MultipartFile>?, memberId: Long) {
        val member = memberService.findByMemberIdOrThrow(memberId)
        val board = Board.boardDtoConvert(boardDto,member)


        lateinit var writeBoard : Board

        //스터디 대상 글쓰기
        boardDto.boardTargetStudyId?.let {

            boardDto.setPortfolioFalse()
            writeBoard = Board.boardDtoConvert(boardDto,memberService.findByMemberIdOrThrow(memberId))
            val study = studyService.findStudyByIdOrThrow(it)
            //종료된 스터디에 대해서만 후기작성 가능
            if(study.currentState != StudyStateEnum.END) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
            writeBoard.updateBoardTargetStudy(study)


            member.boardList.add(writeBoard)
            study.boardList.add(writeBoard)

        } ?: run{
            writeBoard = Board.boardDtoConvert(boardDto,memberService.findByMemberIdOrThrow(memberId))

        }

        val savedImageList : MutableList<Image> = mutableListOf()
        imageList?.forEach{
            val image = Image(src = imageService.saveImage(it),board = writeBoard)
            savedImageList.add(image)
            imageService.save(image)
        }
        writeBoard.updateImageList(savedImageList)

        boardRepository.save(writeBoard)
    }


    fun getBoard(boardId: Long): BoardResponseDto {
        val board = findBoardByIdOrThrow(boardId)
        if(board.isDeleted) throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        return Board.boardToResponseConvert(board)
    }

    @Transactional
    fun deleteBoard(memberId: Long,boardId : Long){
        val board = findBoardByIdOrThrow(boardId)
        if(board.member?.kakaoId == memberId){
            board.setDeleted()
        }else{
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }
    }

    /**
     * @param boardId
     * @param memberId
     * @param boardDto
     */
    @Transactional
    fun updateBoard(boardId: Long, memberId: Long, boardDto: BoardDto, imageList: List<MultipartFile>?){
        val board = findBoardByIdOrThrow(boardId)
        if(board.isDeleted) throw SddodyException(SddodyExceptionError.BAD_REQUEST)

        if(board.member?.kakaoId == memberId){
            val savedImageList : MutableList<Image> = mutableListOf()

            imageList?.forEach{
                val image = Image(src = imageService.saveImage(it),board = board)
                savedImageList.add(image)
                imageService.save(image)
            }

            if(board.member?.kakaoId == memberId){
                board.content = boardDto.content
                board.title = boardDto.title
                board.imageList = savedImageList
                board.tagList = boardDto.tagList
            }

        } else{
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }
    }


    fun findBoardByIdOrThrow(boardId : Long) : Board{
        return boardRepository.findById(boardId).getOrElse{throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
    }

    fun getBoardList(pageDto: PageDto): List<BoardResponseDto> {
        val pageRequest = PageHelper.getPageRequestDescending(pageDto)
        val boardPage = boardRepository.findAllByIsDeletedIsFalseAndIsPortfolioIsFalseAndTargetStudyIsNull(pageRequest)
        // Board 페이지에서 필요한 작업 수행
        // BoardResponseDto로 변환해서 반환
        val boardResponseList = boardPage.content.map { board ->
                Board.boardToResponseConvert(board)
            }
        return boardResponseList
    }


    //유저의 포트폴리오 가져오기
    fun getMemberBoardList(memberId : Long,
                           isPortfolio : Boolean = false): List<BoardResponseDto> {
        val member = memberService.findByMemberIdOrThrow(memberId)

        val boardResponseList :MutableList<BoardResponseDto> = mutableListOf()
        member.boardList.forEach { board ->
            if(board.isPortfolio == isPortfolio && !board.isDeleted ){
                boardResponseList.add(Board.boardToResponseConvert(board))
            }
        }
        return boardResponseList
    }

    //포트폴리오만 가져오기
    fun getPortfolioList(pageDto: PageDto): List<BoardResponseDto> {
        val pageRequest = PageHelper.getPageRequestDescending(pageDto)
        val boardPage = boardRepository.findAllByIsDeletedIsFalseAndIsPortfolioIsTrueAndTargetStudyIsNull(pageRequest)

        // Board 페이지에서 필요한 작업 수행
        // BoardResponseDto로 변환해서 반환
        val boardResponseList = boardPage.content.map { board ->
            Board.boardToResponseConvert(board)
        }
        return boardResponseList
    }

    //내가 작성한 스터디의 후기들 가져오기
    fun getStudyReviewListByMemberId(memberId: Long): List<BoardResponseDto> {
        val member = memberService.findByMemberIdOrThrow(memberId)
        val boardList = member.boardList
        val boardResponseList = boardList.filter { board ->
            (board.targetStudy != null)
        }.map { board -> Board.boardToResponseConvert(board)}

        return boardResponseList
    }

    //스터디의 후기들 가져오기
    fun getStudyReviewListByStudyId(studyId : Long) : List<BoardResponseDto> {
        val study = studyService.findStudyByIdOrThrow(studyId)
        val boardList = study.boardList
        val boardResponseList = boardList.map { board ->
            Board.boardToResponseConvert(board)
        }
        return boardResponseList
    }

}
