package com.sddody.study.entity

import jakarta.persistence.*


/**
 * 이미지 경로를 담습니다
 * 채팅,게시판,스터디 설명에 담긴 이미지를 의미합니다
 *
 * @property id
 * @property src
 * @property study
 * @property board
 */
@Entity
class Image (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var src: String,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    var study:Study? = null,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    var board : Board? = null,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    var chat : Chat? = null,




)
{
}
