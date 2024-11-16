package com.sddody.study.repository

import com.sddody.study.entity.Member
import com.sddody.study.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken,Long>{
    fun findByRefreshToken(refreshToken: String): Optional<RefreshToken>

}
