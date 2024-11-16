package com.sddody.study.repository

import com.sddody.study.entity.PrevSignupMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
public interface PrevSignupMemberRepository : JpaRepository<PrevSignupMember,Long>{


}
