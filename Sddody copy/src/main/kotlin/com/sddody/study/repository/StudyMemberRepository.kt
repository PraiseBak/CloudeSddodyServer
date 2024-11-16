package com.sddody.study.repository

import com.sddody.study.entity.StudyMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudyMemberRepository : JpaRepository<StudyMember,Long>{

}
