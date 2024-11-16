package com.sddody.study.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
//class CorsConfig : WebMvcConfigurer {
//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("/**") // 클라이언트의 주소
////            .allowedOrigins("http://localhost:3000") // 클라이언트의 주소
//            .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
//            .allowedHeaders("*") // 허용할 HTTP 헤더
//            .allowCredentials(true) // 크로스 도메인 쿠키 허용
//    }
//}
