package com.sddody.study.image

import com.sddody.study.StudyApplication
import com.sddody.study.service.ImageService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile

@SpringBootTest(properties = ["spring.config.location=classpath:application.properties"], classes = [StudyApplication::class])
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ImageTest {
    @Autowired
    private lateinit var imageService : ImageService

    @Test
    fun S3_이미지_업로드_테스트시_에러가_출력되지_않음(){


        // MockMultipartFile을 사용하여 가짜 파일 생성
        val mockFile = MockMultipartFile(
                "file",  // 필드 이름
                "test-image.jpg",  // 파일 이름
                "image/jpeg",  // MIME 타입
                "test image content".toByteArray() // 파일 내용
        )

        val imageUrl = imageService.saveImage(mockFile)
        Assertions.assertNotNull(imageUrl)
        println("Image URL: $imageUrl")
    }
}