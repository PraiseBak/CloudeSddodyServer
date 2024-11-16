package com.sddody.study.dto

import com.sddody.study.helper.ValidationEnumClass
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class BoardDto (
    var title: String,

    var content : String,

    @Size(min =0,max=10, message = ValidationEnumClass.IMAGE_SIZE_VALIDATION)
    val imageList : ArrayList<MultipartFile> = arrayListOf(),

    var boardTargetStudyId : Long? = null,

    //포트폴리오 여부
    var isPortfolio : Boolean = false,

    var tagList : List<String> = listOf(),

    var link : String = ""
)
{
    /**
     * 스터디 대상 글일시 무조건 false여야함
     *
     */
    fun setPortfolioFalse() {
        isPortfolio = false
    }


}
