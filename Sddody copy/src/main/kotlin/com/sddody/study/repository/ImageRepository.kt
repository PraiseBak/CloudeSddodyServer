package com.sddody.study.repository

import com.sddody.study.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageRepository : JpaRepository<Image, Long> {

    fun findBySrcAndChatIsNotNull(src : String) : Optional<Image>
}

