//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sddody.study.StudyApplication
//import com.sddody.study.dto.*
//import com.sddody.study.repository.MemberRepository
//import com.sddody.study.service.TestService
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//
//@SpringBootTest(properties = ["spring.config.location=classpath:application.properties"], classes = [StudyApplication::class])
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class BoardTest {
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
//    fun `게시물 작성`(){
//        var boardDto = BoardDto(title = "10 스터디 대상",content="content", boardTargetStudyId = 10L)
//        val tokenDto = testService.getTokenDto()
//
//
//        //스터디 대상일떄
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/board/")
//            .content(objectMapper.writeValueAsString(boardDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//
//        //일반 글일때
//        boardDto = BoardDto(title = "일단 스터디 대상",content="content")
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/board/")
//            .content(objectMapper.writeValueAsString(boardDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//
//    @Test
//    fun `게시물 수정`(){
//        var boardDto = BoardDto(title = "10 스터디 대상 수정",content="content", boardTargetStudyId = 10L)
//        val tokenDto = testService.getTokenDto()
//
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/board/1")
//            .content(objectMapper.writeValueAsString(boardDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//
//
//    @Test
//    fun `게시물 삭제`(){
//        val tokenDto = testService.getTokenDto()
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/board/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//
//    }
//
//
//    @Test
//    fun `포트폴리오 작성`(){
//        var boardDto = BoardDto(title = "포폴 테스트",content="포폴", isPortfolio = true)
//        val tokenDto = testService.getTokenDto()
//
//        //스터디 대상일떄
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/board/")
//            .content(objectMapper.writeValueAsString(boardDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//
//    }
//
//    @Test
//    fun `포트폴리오 출력`(){
//        val tokenDto = testService.getTokenDto()
//
//        val pageDto = PageDto(page=1)
//        //스터디 대상일떄
//        val response = mockMvc.perform(MockMvcRequestBuilders.get("/api/board/portfolioList")
//            .content(objectMapper.writeValueAsString(pageDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andReturn().response.contentAsString
//
//        print("테스트 결과")
//    }
//}
