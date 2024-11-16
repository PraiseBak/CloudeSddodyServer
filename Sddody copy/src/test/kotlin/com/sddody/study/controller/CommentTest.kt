//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sddody.study.StudyApplication
//import com.sddody.study.dto.BoardDto
//import com.sddody.study.dto.CommentDto
//import com.sddody.study.dto.RequestIdDto
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
//class CommentTest {
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
//    fun `댓글 작성`(){
//        val commentWriteBoardId = 1L
//        val commentWriteStudyId = 10L
//
//        var commentDto = CommentDto(studyId = commentWriteStudyId,content="study에 쓰여진 댓글")
//        val tokenDto = testService.getTokenDto()
//
//
//        //게시물 대상일때
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/")
//            .content(objectMapper.writeValueAsString(commentDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//
//        commentDto = CommentDto(boardId = commentWriteBoardId,content="board에 쓰여진 댓글")
//
//        //스터디 대상일떄
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/")
//            .content(objectMapper.writeValueAsString(commentDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//
//    @Test
//    fun `댓글 삭제`(){
//        var requestIdDto: RequestIdDto = RequestIdDto(requestId = 1)
//        val tokenDto = testService.getTokenDto()
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comment/")
//            .content(objectMapper.writeValueAsString(requestIdDto))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header("Authorization", "Bearer ${tokenDto.accessToken}"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//    }
//
//
//}
