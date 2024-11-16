package com.sddody.study.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import kotlin.math.max

@Entity
class RequestBlock(

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    var member : Member,

    @field:Size(max=4000, message = "최대 4000자입니다")
    var reason : String,

) {


}
