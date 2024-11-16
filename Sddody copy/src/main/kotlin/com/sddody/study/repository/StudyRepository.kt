package com.sddody.study.repository

import com.sddody.study.entity.Study
import com.sddody.study.helper.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudyRepository : JpaRepository<Study,Long>{

    fun findAllByOrderByCreatedAtDesc() : List<Study>

    fun findAllByCurrentState(pageRequest: PageRequest, state: StudyStateEnum) : Page<Study>
    fun findAllByCurrentState( state: StudyStateEnum) : List<Study>
    //일치하는거 있는지
    fun findByIdAndCurrentStateAndStudyField(id:Long, state: StudyStateEnum, studyField : DeveloperEnum) : List<Study>
    fun findByIdAndCurrentStateAndStudyTechStack(id:Long, state: StudyStateEnum, techStack : FrameworkEnum) : List<Study>

    fun existsByIdAndCurrentStateAndDevYear(id:Long, state: StudyStateEnum, devYearEnum: DevYearEnum) : Boolean
    fun existsByIdAndCurrentStateAndDevLevel(id:Long, state: StudyStateEnum, devLevelEnum: DevLevelEnum) : Boolean
}
