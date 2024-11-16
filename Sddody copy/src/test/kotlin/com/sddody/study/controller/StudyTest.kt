//package com.sddody.study.controller
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.sddody.study.StudyApplication
//import com.sddody.study.dto.*
//import com.sddody.study.entity.Location
//import com.sddody.study.helper.DeveloperEnum
//import com.sddody.study.helper.FrameworkEnum
//import com.sddody.study.repository.MemberRepository
//import com.sddody.study.repository.PrevSignupMemberRepository
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
//
///*
//Study에 관한 기능들 테스트
// */
//
//@SpringBootTest(properties = ["spring.config.location=classpath:application.properties"], classes = [StudyApplication::class])
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class StudyTest {
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
//    private lateinit var testService: TestService
//
//
//    @Autowired
//    private lateinit var objectMapper: ObjectMapper // ObjectMapper 주입
//
//    @Test
//    fun `스터디 생성`() {
//        val tokenDto = testService.getTokenDto()
//        //임의로 할당한 accessToken 사용
////        var openStudyDto = OpenStudyDto(title = "테스트 스터디", content = "테스트용 스터디입니다잉", maxStudyMemberNum = 10, studyField = mutableListOf(DeveloperEnum.Backend), studyTechStack = mutableListOf(FrameworkEnum.SPRING_BOOT), dyisOpenToEveryone = true)
////        mockMvc.perform(
////            MockMvcRequestBuilders.post("/api/study/")
////            .contentType(MediaType.APPLICATION_JSON)
////                .header("Authorization", "Bearer ${tokenDto.accessToken}")
////            .content(objectMapper.writeValueAsString(openStudyDto))) // JSON으로 변환하여 content에 설정
////            .andExpect(MockMvcResultMatchers.status().isOk())
//
////        //제한사항 벗어난 studyDto일때
////        var openStudyDto = OpenStudyDto(title = "", content = "테스트용 스터디입니다잉", maxStudyMemberNum = 10, studyField = FieldEnum.DEV_STUDY, studyTechStack = mutableListOf(TechStackEnum.SRPING_BOOT,TechStackEnum.REACT))
////        mockMvc.perform(
////            MockMvcRequestBuilders.post("/api/study/")
////                .contentType(MediaType.APPLICATION_JSON)
////                .header("Authorization", "Bearer ${tokenDto.accessToken}")
////                .content(objectMapper.writeValueAsString(openStudyDto))) // JSON으로 변환하여 content에 설정
////            .andExpect(MockMvcResultMatchers.status().isOk)
//    }
//
//    @Test
//    fun `스터디 내용 수정`() {
////        val tokenDto = testService.getTokenDto()
////        //임의로 할당한 accessToken 사용
////        var modifyStudyDto = ModifyStudyDto(
////            title = "asd",
////            content = "asdf",
////            maxStudyMemberNum = 2,
////            studyField = mutableListOf(DeveloperEnum.Backend),
////            studyTechStack = mutableListOf(FrameworkEnum.SPRING_BOOT),
////            studyId = 10,
////        )
////        mockMvc.perform(
////            MockMvcRequestBuilders.put("/api/study/")
////                .contentType(MediaType.APPLICATION_JSON)
////                .header("Authorization", "Bearer ${tokenDto.accessToken}")
////                .content(objectMapper.writeValueAsString(modifyStudyDto))) // JSON으로 변환하여 content에 설정
////            .andExpect(MockMvcResultMatchers.status().isOk())
////
////
////        modifyStudyDto = ModifyStudyDto(
////            title = "a",
////            content = "a",
////            maxStudyMemberNum = -1,
////            studyTechStack = mutableListOf(FrameworkEnum.SPRING_BOOT),
////            studyId = 10,
////        )
////        mockMvc.perform(
////            MockMvcRequestBuilders.put("/api/study/")
////                .contentType(MediaType.APPLICATION_JSON)
////                .header("Authorization", "Bearer ${tokenDto.accessToken}")
////                .content(objectMapper.writeValueAsString(modifyStudyDto))) // JSON으로 변환하여 content에 설정
////            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
//
//    }
//
//    @Test
//    fun `스터디 시작`() {
//        val tokenDto = testService.getTokenDto()
//
//        val requestBody = mapOf("studyId" to "13")
//        val jsonContent = objectMapper.writeValueAsString(requestBody)
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.put("/api/study/start")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer ${tokenDto.accessToken}")
//                .content(jsonContent))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//
////        //권한 없을때
////        mockMvc.perform(
////            MockMvcRequestBuilders.put("/api/study/start")
////                .contentType(MediaType.APPLICATION_JSON)
////                .header("Authorization", "Bearer ${tokenDto.accessToken}")
////                .content(objectMapper.writeValueAsString(mapOf("studyId" to "11"))))
////            .andExpect(MockMvcResultMatchers.status().isBadRequest())
//    }
//
//
//    @Test
//    fun `스터디 종료`() {
//        val tokenDto = testService.getTokenDto()
//
//        val requestBody = mapOf("studyId" to "10")
//        val jsonContent = objectMapper.writeValueAsString(requestBody)
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.put("/api/study/end")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer ${tokenDto.accessToken}")
//                .content(jsonContent))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//
//
//    }
//
//
//    /**
//     * 스터디 참가 요청
//     * open된 스터디냐 open되지 않은 스터디냐로 나뉨
//     */
//    @Test
//    fun `스터디 참가 요청`(){
//        val tokenDto = testService.getTokenDto()
//
//        //다른사람이 만든 스터디
//        val studyMemberDto = StudyIdDto(studyId=11L)
//        val jsonContent = objectMapper.writeValueAsString(studyMemberDto)
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/api/study/participationRequest")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer ${tokenDto.accessToken}")
//                .content(jsonContent))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//    }
//
//
//
//    /*
//    관리자가 스터디 멤버 추가한 경우
//     */
//    @Test
//    fun `스터디 멤버 추가`() {
//        val tokenDto = testService.getTokenDto()
//
//        //3062
//        val studyMemberDto = StudyMemberDto(studyId=10L,3062L)
//        val jsonContent = objectMapper.writeValueAsString(studyMemberDto)
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.put("/api/study/memberAdd")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer ${tokenDto.accessToken}")
//                .content(jsonContent))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//
//    }
//
//}