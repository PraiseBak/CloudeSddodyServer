package com.sddody.study.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

//study time으로 뭔가할수있지않을까?
//time으로 구현
//interface는 제한된 interface로 시간을 선택할수있게 구현
@Entity
class StudyMember(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        @NotNull
        var member: Member,

        @ManyToOne
        @NotNull
        var study: Study,


) {
}

