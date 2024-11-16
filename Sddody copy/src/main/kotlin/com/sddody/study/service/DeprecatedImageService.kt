package com.sddody.study.service

import com.sddody.study.common.SddodyException
import com.sddody.study.entity.Image
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.repository.ImageRepository
import lombok.Getter
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * deprecated
 * 이미지 서비스는 s3로 대체되었습니다.
 *
 * @property imageRepository
 * @property memberService
 */
@Service
@Slf4j
class DeprecatedImageService (private val imageRepository : ImageRepository,
                              private val memberService: MemberService){




    @Getter
    @Value("\${upload.path}") // properties 파일에서 이미지 저장 경로를 지정하세요
    private val uploadPath: String? = null
    private val allowedExtensions = arrayOf(".jpg", ".jpeg", ".png", ".gif")

    fun getUploadPath(): String{
        return uploadPath  ?: throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)
    }

    @Throws(IOException::class)
    fun saveImage(img: MultipartFile?): String {
        if(img == null) return ""
        if (!validation(img)) {
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }
        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        val today = formatter.format(Date())
        val originalName = img.originalFilename // 실제 파일명
        val originalNameExtension = originalName!!.substring(originalName.lastIndexOf(".") + 1)
            .lowercase(Locale.getDefault()) // 실제파일 확장자 (소문자변경)
        val modifyName = today + "-" + UUID.randomUUID().toString().substring(20) + "." + originalNameExtension
        val uploadPath: String? = getUploadPath() ?: throw SddodyException(SddodyExceptionError.CANNOT_FIND_RESOURCE)
        val outputFile = File(uploadPath + File.separator + modifyName)
        img.transferTo(outputFile)
        return modifyName
    }

    private fun validation(img: MultipartFile): Boolean {

        if (img.size > 10 * 1024 * 1024) {
            return false
        }
        val originalFilename = img.originalFilename
        if (originalFilename != null) {
            val extension = originalFilename.substring(originalFilename.lastIndexOf(".")).lowercase(Locale.getDefault())
            var isAllowedExtension = false
            for (allowedExtension in allowedExtensions) {
                if (allowedExtension == extension) {
                    isAllowedExtension = true
                    break
                }
            }
            if (!isAllowedExtension) {
                return false
            }
        }
        return true

    }


    /**
     * 채팅 이미지 가져오기
     *
     * @param fileName
     * @return
     */
    fun findByChatImageSrc(fileName: String) : Optional<Image> {
        return imageRepository.findBySrcAndChatIsNotNull(fileName)

    }

    /**
     *
     *
     * @param fileName
     * @param authentication
     * @throws SddodyExceptionError.BAD_REQUEST
     * @return
     */
    fun isUserHasAuthenticationOrThrow(fileName: String, authentication: Authentication) : Boolean{
        val chatImage = findByChatImageSrc(fileName)
        if (chatImage.isPresent) {
            val study = chatImage.get().study
            val member = memberService.findByMemberIdOrThrow(authentication.name.toLong())

            study?.let {
                if (!memberService.isMemberContainsStudy(it, member)) {
                    throw SddodyException(SddodyExceptionError.BAD_REQUEST)
                }
            }

        }

        //열려있는 이미지
        return true
    }

    fun save(image: Image) {
        imageRepository.save(image)
    }




}

