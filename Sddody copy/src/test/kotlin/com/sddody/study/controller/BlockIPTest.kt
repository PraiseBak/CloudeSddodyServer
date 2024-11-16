//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sddody.study.StudyApplication
//import com.sddody.study.common.SddodyException
//import com.sddody.study.dto.TokenDto
//import com.sddody.study.entity.BlockedIP
//import com.sddody.study.repository.BlockedIPRepository
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import java.time.LocalDateTime
//
//@SpringBootTest(properties = ["spring.config.location=classpath:application.properties"], classes = [StudyApplication::class])
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class BlockIPTest {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @Autowired
//    private lateinit var objectMapper: ObjectMapper // ObjectMapper 주입
//
//    @Autowired
//    private lateinit var blockedIPRepository: BlockedIPRepository
//
//    //ip 차단됨
//    @Test
//    fun blockIPTest() {
//        val blockedIp = blockedIPRepository.findByIp("127.0.0.1").get()
//        blockedIp.count=3
//        blockedIp.isBlocked z= true
//        blockedIp.blockEndTime = LocalDateTime.now().plusHours(50)
//        blockedIPRepository.save(blockedIp)
//
//        val signupRequestDto = TokenDto(accessToken = "123")
//        val exception = assertThrows<SddodyException> {
//            mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
//                .header("Authorization", 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(signupRequestDto)))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andReturn()
//        }
//
//
//        blockedIp.isBlocked = false
//        blockedIp.blockEndTime = LocalDateTime.now()
//        blockedIp.count = 0
//        blockedIPRepository.save(blockedIp)
//
////        for (i in 1..3){
////            mockMvc.perform(MockMvcRequestBuilders.post("/api/chatRoom/msg")
////                .contentType(MediaType.APPLICATION_JSON)
////                .header("Authorization",1L)
////                .content(objectMapper.writeValueAsString(requestBody))) // JSON으로 변환하여 content에 설정
////                .andExpect(MockMvcResultMatchers.status().isNotFound())
////            // 여기서 andExpect 등을 사용하여 원하는 결과를 확인할 수 있습니다.
////        }
////
////        assertEquals(1L,blockedIPRepository.count())
//    }
//}
