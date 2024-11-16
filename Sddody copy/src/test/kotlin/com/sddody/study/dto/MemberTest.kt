//package com.sddody.study.dto
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sddody.study.StudyApplication
//import com.sddody.study.entity.Location
//import com.sddody.study.helper.DeveloperEnum
//import com.sddody.study.helper.FrameworkEnum
//import com.sddody.study.helper.LanguageEnum
//import com.sddody.study.repository.MemberRepository
//import com.sddody.study.repository.PrevSignupMemberRepository
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
//class MemberTest {
//
//    @Autowired
//    private lateinit var memberRepository: MemberRepository
//
//    @Autowired
//    private lateinit var prevSignupMemberRepository: PrevSignupMemberRepository
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @Autowired
//    private lateinit var objectMapper: ObjectMapper // ObjectMapper 주입
//
//    private val testAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMTc5OTkwOTk4IiwiZXhwIjoxNzAzNDgyMTYzfQ.JdEh3ESwYI7jPIKz6PB8bdaapCwou9E7p5TPuHZ-rv4"
//
//    @Test
//    fun `초기 회원가입 테스트`() {
//        val signupRequestDto = TokenDto(accessToken = testAccessToken)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(signupRequestDto))) // JSON으로 변환하여 content에 설정
//            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//    @Test
//    fun `초기 유저 정보 입력 테스트`() {
//
//        val memberDto = MemberDto(
//            kakaoId = 3179990998L,
//            nickname = "praisebak",
//            selfIntroduce = "Introduction",
//            memberLocation = Location(location = "Test Location"),
//            interestField = listOf(DeveloperEnum.BACKEND, DeveloperEnum.DATA_ENGINEERING),
//            haveExperienceTechStack = listOf(FrameworkEnum.Angular,FrameworkEnum.Django),
//            devLanguageField = listOf(LanguageEnum.CSharp),
//        )
//
//        //있는 id로 테스트
//        mockMvc.perform(
//            MockMvcRequestBuilders.put("/api/auth/memberInfo")
//                .header("Authorization", "Bearer $testAccessToken")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(memberDto))
//        ) // JSON으로 변환하여 content에 설정
//            .andExpect(MockMvcResultMatchers.status().isOk)
//    }
//
//
//    @Test
//    fun `유저 정보 수정 테스트`() {
//        val memberDto = MemberDto(
//            kakaoId = 3179990998L,
//            nickname = "praisebak1",
//            selfIntroduce = "Introduction",
//            memberLocation = Location(location = "Test Location"),
//            interestField = listOf(DeveloperEnum.BACKEND, DeveloperEnum.DATA_ENGINEERING),
//            haveExperienceTechStack = listOf(FrameworkEnum.Angular,FrameworkEnum.Django),
//            devLanguageField = listOf(LanguageEnum.CSharp),
//        )
//
//        //있는 id로 테스트
//        mockMvc.perform(
//            MockMvcRequestBuilders.put("/api/auth/memberInfo")
//                .header("Authorization", "Bearer $testAccessToken")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(memberDto))
//        ) // JSON으로 변환하여 content에 설정
//            .andExpect(MockMvcResultMatchers.status().isOk)
//    }
//
//}