package com.sddody.study.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import kotlin.math.pow
import kotlin.math.sqrt

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var latitude : Double = 37.3846,

    var longitude : Double =127.1250,


)
{


    //초기 거리값은 30km
    fun isNearBy(objLocation : Location,distance : Int = 30) : Boolean{
        val distance = sqrt((objLocation.latitude - latitude).pow(2) + (objLocation.longitude - longitude).pow(2))
        return distance <= distance
    }

}
