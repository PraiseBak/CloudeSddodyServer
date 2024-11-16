package com.sddody.study.repository

import com.sddody.study.entity.Alarm
import com.sddody.study.entity.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AlarmRepository : JpaRepository<Alarm, Long> {

}
