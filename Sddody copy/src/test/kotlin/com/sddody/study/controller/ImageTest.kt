//package com.sddody.study.controller
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sddody.study.StudyApplication
//import com.sddody.study.repository.MemberRepository
//import com.sddody.study.service.TestService
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.core.io.ResourceLoader
//import org.springframework.http.MediaType
//import org.springframework.mock.web.MockMultipartFile
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import org.springframework.util.ResourceUtils
//import java.nio.file.Files
//
//@SpringBootTest(properties = ["spring.config.location=classpath:application.properties"], classes = [StudyApplication::class])
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class ChatRoomTest {
//
//    @Autowired
//    private lateinit var testService: TestService
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc;
//
//    @Autowired
//    private lateinit var memberRepository: MemberRepository;
//
//    @Autowired
//    private lateinit var resourceLoader: ResourceLoader
//
//
//    @Autowired
//    private lateinit var objectMapper: ObjectMapper // ObjectMapper 주입
//
//    @Test
//    fun `이미지 전송`() {
//
//        val tokenDto = testService.getTokenDto()
//        // MockMultipartFile 생성
//        val file = MockMultipartFile(
//            "img",
//            "test.jpg", // 여기서 "test.jpg"는 파일명입니다. 필요에 따라 수정하세요.
//            MediaType.IMAGE_JPEG_VALUE,
//            "Some file content".toByteArray() // 여기서는 임의의 파일 내용을 넣습니다.
//        )
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/img/add")
//            .file(file)
//            .contentType(MediaType.MULTIPART_FORM_DATA)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//        // 이후에 테스트에 맞게 필요한 검증 로직을 추가하시면 됩니다.
//    }
//
//
//    @Test
//    fun `이미지 포함 채팅 전송`() {
//        val path1 = "static/img/1.jpeg"
//        val path2 = "static/img/2.jpeg"
//
//        val file1 = ResourceUtils.getFile("classpath:$path1")
//        val file2 = ResourceUtils.getFile("classpath:$path2")
//
//        val multipartFile1 = MockMultipartFile("imageList", file1.name, MediaType.IMAGE_JPEG_VALUE, Files.readAllBytes(file1.toPath()))
//        val multipartFile2 = MockMultipartFile("imageList", file2.name, MediaType.IMAGE_JPEG_VALUE, Files.readAllBytes(file2.toPath()))
//
//        val tokenDto = testService.getTokenDto()
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/chatRoom/msg")
//            .file(multipartFile1)
//            .file(multipartFile2)
//            .param("msg", "메시지 테스트")
//            .param("chatRoomId", "4")
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//
//
//    @Test
//    fun `채팅 이미지 출력`(){
//        val fileName = "default_img.jpeg"
//        val tokenDto = testService.getTokenDto()
//        // 여기에 파일 다운로드와 관련된 추가적인 테스트 코드를 작성할 수 있음
//
//        val response = mockMvc.perform(MockMvcRequestBuilders.get("/api/img/get.cf?fileName=$fileName")
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andReturn().response.contentAsString
//        println(response)
//    }
//
//
//    /*
//    생략
//     */
//    @Test
//    fun `이미지 출력`(){
//
//
//    }
//
//
//}
