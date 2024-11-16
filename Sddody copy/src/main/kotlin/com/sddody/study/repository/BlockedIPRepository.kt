package com.sddody.study.repository

import com.sddody.study.entity.BlockedIP
import com.sddody.study.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BlockedIPRepository : JpaRepository<BlockedIP,Long>{
    fun findByIp(ip : String) : Optional<BlockedIP>

}
