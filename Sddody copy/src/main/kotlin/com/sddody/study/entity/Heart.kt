package com.sddody.study.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size

/**
 * 추천 엔티티
 * 댓글과 게시물에 사용됨
 *
 */
@Entity
class Heart (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,

    @ManyToOne(optional = true)
    var comment : Comment? = null,

    @ManyToOne(optional = true)
    var board : Board? = null,

    //추천한 멤버
    @ManyToOne
    var member : Member,
) {

}
