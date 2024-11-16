//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sddody.study.StudyApplication
//import com.sddody.study.common.SddodyException
//import com.sddody.study.dto.TokenDto
//import com.sddody.study.helper.SddodyExceptionError
//import com.sddody.study.repository.MemberRepository
//import com.sddody.study.repository.StudyRepository
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
//
//@SpringBootTest(properties = ["spring.config.location=classpath:application.properties"], classes = [StudyApplication::class])
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class AuthTest {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @Autowired
//    private lateinit var objectMapper: ObjectMapper // ObjectMapper 주입
//
//    @Autowired
//    private lateinit var memberRepository: MemberRepository
//
//    @Autowired
//    private lateinit var studyRepository: StudyRepository
//
//
//    /*
//    존재하지 않는 jwt 토큰 케이스
//    */
//    @Test
//    fun wrongJWTTest() {
//        val signupRequestDto = TokenDto(accessToken = "123")
//        val exception = assertThrows<SddodyException> {
//            mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/auth/test")
//                    .header("Authorization", "Bearer 1")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(signupRequestDto))
//            ) // JSON으로 변환하여 content에 설정
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
//
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
//        }
//    }
//
//    /*
//    유효한 토큰인지 테스트
//     */
//    @Test
//    fun correctJWTTest() {
//        //클라이언트 실제 동작으로 임의의 accesstoken를 받아온다
//        val accessToken =
//
//            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMTc5OTkwOTk4IiwiZXhwIjoxNzAzNDIxMjI4fQ.-ljXDB675V9HJ0VsHGv4_Hvzdqqsf6UskUiBtnCd-u0"
//        //실제 accessToken를 줬을때 notFound(url은 존재하지 않는 것으로 주었음)가 뜨는지 확인
//        val exception = assertThrows<SddodyException> {
//            mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/auth/test")
//                    .header("Authorization", "Bearer $accessToken")
//            ).andExpect(MockMvcResultMatchers.status().isNotFound)
//        }
//    }
//
//    /*
//    만료된 access지
//     */
//    @Test
//    fun expiredAccessTokenTest(){
//        //클라이언트 실제 동작으로 임의의 accesstoken를 받아온다
//        val accessToken =
//            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMTc5OTkwOTk4IiwiZXhwIjoxNzAzMzQ1MjQ0fQ.3P_flMNO9hCElyo76LlDfLRKekauF9KzdnCfItCs91Q"
//        //실제 accessToken를 줬을때 notFound(url은 존재하지 않는 것으로 주었음)가 뜨는지 확인
//        val exception = assertThrows<SddodyException> {
//            mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/auth/test")
//                    .header("Authorization", "Bearer $accessToken")
//            ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
//        }
//        assert(exception.sddodyExceptionError == SddodyExceptionError.TOKEN_INVALIDATE)
//    }
//
//
//    /*
//    유효한 refresh token일 경우
//     */
//    @Test
//    fun nonExpiredRefreshTokenTest(){
//        //클라이언트 실제 동작으로 임의의 refreshToken를 받아온다
//        val refreshToken =
//            "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MDMzNTA1NzN9.aKD-jhWL7E0tJSFLWdIhTikZ2oOX-UCCx5c8eWy5ONA"
////        val exception = assertThrows<SddodyException> {
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/api/auth/test")
//                .header("X-Refresh-Token", "$refreshToken")
//        ).andExpect(MockMvcResultMatchers.status().isNotFound)
////        }ㄴ
////        println(exception.toString())
////        assert(exception.sddodyExceptionError == SddodyExceptionError.REFRESH_TOKEN_INVALIDATE)
//    }
//
//
//    /*
//    만료된 refresh
//     */
//    @Test
//    fun expiredRefreshTokenTest(){
//        //클라이언트 실제 동작으로 임의의 refreshToken를 받아온다
//        val refreshToken =
//            "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MDMzNDg5NjB9.aLig7zP23JeZ2W6H46qnOT_VWIi2hUoxtWh1emy4_q0"
//        //실제 accessToken를 줬을때 notFound(url은 존재하지 않는 것으로 주었음)가 뜨는지 확인
//        val exception = assertThrows<SddodyException> {
//            mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/auth/test")
//                    .header("X-Refresh-Token", "$refreshToken")
//            ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
//        }
//        assert(exception.sddodyExceptionError == SddodyExceptionError.REFRESH_TOKEN_INVALIDATE)
//    }
//
//
//
//}
