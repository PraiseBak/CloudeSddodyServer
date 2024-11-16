package com.sddody.study.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * 스터디 참가 요청을 보낸 유저를 나타내는 엔티티
 *
 * @property id
 * @property member
 * @property study
 */
@Entity
class RequestStudyMember(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        @NotNull
        var member: Member,

        @ManyToOne
        @NotNull
        var study: Study,

        @field:Size(max=400, message = "최대 400자입니다")
        var message : String = "",
) {
}

