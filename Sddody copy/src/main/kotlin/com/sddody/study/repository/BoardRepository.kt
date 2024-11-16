package com.sddody.study.repository

import com.sddody.study.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : JpaRepository<Board,Long>{
    //일반 보드
    fun findAllByIsDeletedIsFalseAndIsPortfolioIsFalseAndTargetStudyIsNull(pageRequest: PageRequest) : Page<Board>
    //포트폴리오
    fun findAllByIsDeletedIsFalseAndIsPortfolioIsTrueAndTargetStudyIsNull(pageRequest: PageRequest) : Page<Board>

    //스터디
    fun findAllByIsDeletedIsFalseAndIsPortfolioIsFalseAndTargetStudyIsNotNull(pageRequest: PageRequest) : Page<Board>

}
