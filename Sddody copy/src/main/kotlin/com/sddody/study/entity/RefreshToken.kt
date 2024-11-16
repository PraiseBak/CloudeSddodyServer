package com.sddody.study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor

@Entity
@AllArgsConstructor
class RefreshToken (
    @Id
    var kakaoId : Long,

    @NotNull
    var refreshToken: String
)
{
    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

}
