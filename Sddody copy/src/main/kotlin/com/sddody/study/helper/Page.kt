package com.sddody.study.helper

import com.sddody.study.dto.PageDto
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class PageHelper {



    companion object {

        /**
         * page는 양수로
         *
         * @param pageDto
         * @return
         */
        fun getPageRequestDescending(pageDto: PageDto): PageRequest {
            return PageRequest.of(pageDto.page-1, PAGE_SIZE, Sort.by("createdAt").ascending())
        }

        const val PAGE_SIZE : Int = 10
    }
}