package com.sddody.study.entity

import com.sddody.study.helper.ValidationEnumClass
import jakarta.persistence.*
import jakarta.validation.constraints.Size

/**
 * 스터디에 댓글이거나
 * 게시물에 댓글일 수 있음
 * (Opiotanl로 구분)
 * @property id
 * @property member
 */
@Entity
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,

    @ManyToOne
    var member: Member,

    @ManyToOne(optional = true)
    var study : Study? = null,

    @ManyToOne(optional = true)
    var board : Board? = null,

    @field:Size(min = 2, max = 200, message = ValidationEnumClass.STUDY_CONTENT_VALIDATION)
    var content : String,

    @OneToMany(mappedBy = "comment")
    var heartList : MutableList<Heart> = mutableListOf(),

    var isDeleted : Boolean = false,
) : BaseTimeEntity()
{
}
