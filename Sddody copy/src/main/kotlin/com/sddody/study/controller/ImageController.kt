package com.sddody.study.controller

import com.sddody.study.common.SddodyException
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.service.ImageService
import com.sddody.study.service.MemberService
import com.sddody.study.service.StudyService
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL


@RestController
@RequestMapping("/api/img")
@RequiredArgsConstructor
class ImageController(
    private val imageService: ImageService,
    val studyService: StudyService,
    val memberService: MemberService,

    ){
    /**
     * 이미지를 가져오는 url
     * 채팅의 경우 이미지 권한이 필요하며 이는 chatImage의 Src를 확인하여 구현
     *
     * @param fileName
     * @param response
     */
    @GetMapping("/get.cf")
    @Throws(IOException::class)
    fun getFile(@RequestParam("fileName") fileName: String, response: HttpServletResponse,authentication: Authentication) {
        imageService.isUserHasAuthenticationOrThrow(fileName, authentication)

        response.contentType = "application/jpeg"
        var fileOpenStream: ByteArray? = imageService.getImage(fileName)
        if(fileOpenStream == null){
            return;
        }
        try {
            IOUtils.copy(fileOpenStream.inputStream(), response.outputStream)
        } catch (e: FileNotFoundException) {
            throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)
        }
    }

    @PostMapping("/add")
    @Throws(IOException::class)
    fun saveImage(img: MultipartFile): ResponseEntity<String> {
        val fileName: String = imageService.saveImage(img)
        return ResponseEntity.ok(fileName)
    }
}

