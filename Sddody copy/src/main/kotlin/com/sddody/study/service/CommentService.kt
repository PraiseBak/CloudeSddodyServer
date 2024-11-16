package com.sddody.study.service;

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.CommentDto
import com.sddody.study.dto.RequestIdDto
import com.sddody.study.entity.Comment
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.repository.CommentRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberService: MemberService,
    private val boardService: BoardService,
    private val studyService: StudyService,
    )
{
    @Transactional
    fun createComment(commentDto: CommentDto, memberId: Long) {
        println(commentDto)
        commentDto.studyId?.let {
            val study = studyService.findStudyByIdOrThrow(it)
            val member = memberService.findByMemberIdOrThrow(memberId)

            val comment = commentRepository.save(Comment(member = member,
                study = study,
                content = commentDto.content,
                )
            )
            study.commentList.add(comment)
            member.commentList.add(comment)

        } ?: commentDto.boardId?.let {
            val board = boardService.findBoardByIdOrThrow(it)
            val member = memberService.findByMemberIdOrThrow(memberId)
            val comment = commentRepository.save(Comment(member = member,
                board = board,
                content = commentDto.content,
                )
            )
            board.commentList.add(comment)
            member.commentList.add(comment)
        }
    }

    fun findCommentByIdOrThrow(commentId : Long) : Comment{
        return commentRepository.findById(commentId).orElseThrow{SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)}
    }


    @Transactional
    fun deleteComment(memberId : Long, id : Long){
        val comment = findCommentByIdOrThrow(id)
        if(comment.member.kakaoId == memberId){
            comment.isDeleted = true
        }
    }

}
