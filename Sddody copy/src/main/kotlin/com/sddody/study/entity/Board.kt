package com.sddody.study.entity

import com.sddody.study.common.SddodyException
import com.sddody.study.dto.BoardDto
import com.sddody.study.dto.BoardResponseDto
import com.sddody.study.dto.CommentResponseDto
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.helper.ValidationEnumClass
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import java.time.ZoneId
import java.util.*


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Board (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:Size(min = 2, max = 50, message = "제목은 최소 2자에서 50자 까지에요")    
    var title: String = "",

    @field:Size(min = 2, max = 4000, message = "내용이 너무 길어요")
    var content: String = "",

    @field:Size(min = 0, max = 10, message = ValidationEnumClass.IMAGE_SIZE_VALIDATION)
    @OneToMany
    var imageList: List<Image> = arrayListOf(),

    @ManyToOne
    var member: Member,

    @ManyToOne
    var targetStudy: Study? = null,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var commentList: MutableList<Comment> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var heartList: MutableList<Heart> = mutableListOf(),

    var isDeleted : Boolean = false,

    var isPortfolio : Boolean = false,

    @field:Size(min = 0, max = 200, message = ValidationEnumClass.STUDY_LINK_VALIDATION)
    var link : String = "",

    @ElementCollection()
    @field:Size(min = 0, max = 10, message = ValidationEnumClass.IMAGE_SIZE_VALIDATION)
    var tagList : List<String> = listOf()
) : BaseTimeEntity()
{
    fun updateBoardTargetStudy(study: Study) {
        this.targetStudy = study
    }


    fun updateImageList(imageList : List<Image>){
        this.imageList = imageList
    }
    fun setDeleted(){
        this.isDeleted = true
    }

    companion object {
        fun boardDtoConvert(boardDto: BoardDto, member: Member) : Board{
            return Board(
                title = boardDto.title,
                content = boardDto.content,
                member = member,
                isPortfolio = boardDto.isPortfolio,
                tagList = boardDto.tagList,
                link = boardDto.link
            )

        }

        fun boardToResponseConvert(board : Board) : BoardResponseDto{
            val imageSrcList : MutableList<String> = mutableListOf()
            board.imageList.forEach{imageSrcList.add(it.src) }

            val commentList : MutableList<CommentResponseDto> = mutableListOf()
            board.commentList.forEach{
                if(!it.isDeleted){
                    commentList.add(
                        CommentResponseDto((it.id)!!,it.content,it.member.nickname,it.member.profileImgSrc,
                            createdAt = Date.from(it.createdAt?.atZone(ZoneId.systemDefault())?.toInstant()),
                            updatedAt = Date.from(it.updatedAt?.atZone(ZoneId.systemDefault())?.toInstant()),
                        )
                    )
                }
            }

            return BoardResponseDto(
                id = board.id ?: throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE),
                title = board.title,
                content = board.content,
                heartCount = board.heartList.size,
                commentList = commentList,
                boardTargetStudy = board.targetStudy?.id,
                nickname = board.member.nickname,
                imageSrcList = imageSrcList.toList(),
                profileImageSrc = board.member.profileImgSrc,
                createdAt = Date.from(board.createdAt?.atZone(ZoneId.systemDefault())?.toInstant()),
                updatedAt = Date.from(board.updatedAt?.atZone(ZoneId.systemDefault())?.toInstant()),
                tagList = board.tagList,
                link = board.link,
                memberId = board.member.kakaoId!!
            )
        }
    }

}