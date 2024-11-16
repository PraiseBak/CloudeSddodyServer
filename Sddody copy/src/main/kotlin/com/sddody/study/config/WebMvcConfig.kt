package com.sddody.study.config


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    @Value("\${upload.path}") // properties 파일에서 이미지 저장 경로를 지정하세요

    private val uploadPath: String? = null
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

        // 업로드 경로에 대한 File 객체 생성
        val uploadFolder = File(uploadPath)

        // 업로드 폴더가 없는 경우 폴더 생성
        if (!uploadFolder.exists()) {
            val created = uploadFolder.mkdirs()
            if (!created) {
                throw IllegalStateException("Failed to create upload folder.")
            }
        }


        registry
            .addResourceHandler("/img/")
            .addResourceLocations("file:///$uploadPath")
    }
}
