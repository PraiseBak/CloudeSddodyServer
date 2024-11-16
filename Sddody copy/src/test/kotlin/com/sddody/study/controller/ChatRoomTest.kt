//import com.fasterxml.jackson.databind.ObjectMapper
//import com.google.auth.oauth2.GoogleCredentials
//import com.google.firebase.FirebaseApp
//import com.google.firebase.FirebaseOptions
//import com.sddody.study.StudyApplication
//import com.sddody.study.dto.ChatDto
//import com.sddody.study.dto.RequestIdDto
//import com.sddody.study.repository.MemberRepository
//import com.sddody.study.service.TestService
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import java.io.FileInputStream
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
//
//    @Autowired
//    private lateinit var objectMapper: ObjectMapper // ObjectMapper 주입
//
//    @Test
//    fun `채팅 전송`(){
//        /*
//        채팅방, 채팅 보낸사람, 채팅 내용, 이미지 등이 있어야함
//         */
//
//        val chatDto = ChatDto(msg="메시지 테스트")
//
//        val tokenDto = testService.getTokenDto()
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/chatRoom/msg")
//            .content(objectMapper.writeValueAsString(chatDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//    }
//
//    @Test
//    fun `채팅 읽음`(){
//        val requestIdDto = RequestIdDto(requestId = 6L)
//
//        val tokenDto = testService.getTokenDto()
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/chatRoom/read")
//            .content(objectMapper.writeValueAsString(requestIdDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//
//    @Test
//    fun `채팅 이미지 제한 10MB,개수 제한`(){
////        val chatDto = ChatDto(msg="메시지 테스트",studyId=10)
////        val tokenDto = testService.getTokenDto()
////
////        mockMvc.perform(MockMvcRequestBuilders.post("/api/chatRoom/msg")
////            .content(objectMapper.writeValueAsString(chatDto))
////            .contentType(MediaType.APPLICATION_JSON)
////            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
////            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//    companion object {
//        @JvmStatic
//        @BeforeAll
//        fun initFirebase(): Unit {
//            if (FirebaseApp.getApps().isEmpty()) {
//                val serviceAccount =
//                    FileInputStream("./src/main/resources/sddody-83acb-firebase-adminsdk-f3uqm-7576c7e4ec.json")
//                val options: FirebaseOptions = FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build()
//                FirebaseApp.initializeApp(options)
//            }
//        }
//    }
//
//
//}
