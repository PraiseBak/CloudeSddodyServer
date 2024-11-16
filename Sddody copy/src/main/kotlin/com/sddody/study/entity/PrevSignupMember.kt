package com.sddody.study.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import kotlinx.serialization.Serializable
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

@Entity
@Serializable
@AllArgsConstructor
@NoArgsConstructor
@Builder
class PrevSignupMember (
    @Id val id : Long?,
){


}