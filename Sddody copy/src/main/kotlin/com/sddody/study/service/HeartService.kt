package com.sddody.study.service

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.HeartDto
import com.sddody.study.entity.Heart
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.repository.HeartRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class HeartService(
    private val heartRepository: HeartRepository,
    private val memberService: MemberService,
    private val boardService: BoardService,
    private val commentService: CommentService
)
{

    /**
     * board나 comment에 대한 heart를 추가하는 메서드(취소 불가)
     * heart를 생성할 때 중복체크 한 뒤 create함
     *
     * @param heartDto
     * @param memberId
     */
    @Transactional
    fun heartCreate(heartDto: HeartDto, memberId: Long) {
        val member = memberService.findByMemberIdOrThrow(memberId)

        heartDto.boardId?.let {
            val board = boardService.findBoardByIdOrThrow(boardId = it)

            board.heartList.forEach { heart ->
                if (heart.member.kakaoId == memberId) {
                    throw SddodyException(SddodyExceptionError.DUPLICATE_REQUEST)
                }
            }

            val savedHeart = heartRepository.save(Heart(board = board,member = member))
            board.heartList.add(savedHeart)
            savedHeart.board = board
            member.heartList.add(savedHeart)
        } ?: heartDto.commentId?.let {
            val comment = commentService.findCommentByIdOrThrow(it)

            comment.heartList.forEach { heart ->
                if (heart.member.kakaoId == memberId) {
                    throw SddodyException(SddodyExceptionError.DUPLICATE_REQUEST)
                }
            }

            val savedHeart = heartRepository.save(Heart(comment = comment,member = member))
            comment.heartList.add(savedHeart)
            savedHeart.comment = comment
            member.heartList.add(savedHeart)
        }

    }
}