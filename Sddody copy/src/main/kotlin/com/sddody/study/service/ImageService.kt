package com.sddody.study.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.sddody.study.common.SddodyException
import com.sddody.study.entity.Image
import com.sddody.study.helper.SddodyExceptionError
import com.sddody.study.repository.ImageRepository
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*


@Service
@Slf4j
class ImageService (private val imageRepository : ImageRepository,
                    private val memberService: MemberService,
                    private val amazonS3: AmazonS3
        ){
    @Getter
    @Value("\${upload.path}") // properties 파일에서 이미지 저장 경로를 지정하세요
    private val uploadPath: String? = null
    private val allowedExtensions = arrayOf(".jpg", ".jpeg", ".png", ".gif")


    @Value("\${cloud.aws.s3.bucket}")
    private val BUCKET_NAME: String? = null

    @Throws(IOException::class)
    fun saveImage(img: MultipartFile?): String {
        if(img == null) return ""
        if (!validation(img)) {
            throw SddodyException(SddodyExceptionError.BAD_REQUEST)
        }
        val fileName = System.currentTimeMillis().toString() + "_" + img.originalFilename
        var inputStream = img.inputStream
        val metadata = ObjectMetadata()
        metadata.contentLength = img.getSize()
        amazonS3.putObject(BUCKET_NAME, fileName, inputStream, metadata)
        return fileName
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

        return true
    }

    fun save(image: Image) {
        imageRepository.save(image)
    }

    fun getImage(fileName: String): ByteArray? {
        return try {
            val s3Object = amazonS3.getObject(BUCKET_NAME, fileName)
            val inputStream: S3ObjectInputStream = s3Object.objectContent
            StreamUtils.copyToByteArray(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            return null;
        }
    }
}

