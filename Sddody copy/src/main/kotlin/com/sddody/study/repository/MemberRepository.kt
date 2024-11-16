package com.sddody.study.repository

import com.sddody.study.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
public interface MemberRepository : JpaRepository<Member,Long>{

    fun findByNickname(nickname : String) : Optional<Member>;
}
