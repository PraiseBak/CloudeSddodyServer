package com.sddody.study.repository

import com.sddody.study.entity.Heart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HeartRepository  : JpaRepository<Heart,Long>{

}
