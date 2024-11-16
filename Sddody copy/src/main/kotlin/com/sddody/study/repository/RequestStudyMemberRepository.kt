package com.sddody.study.repository

import com.sddody.study.entity.RequestStudyMember
import com.sddody.study.entity.StudyMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestStudyMemberRepository : JpaRepository<RequestStudyMember, Long> {

}