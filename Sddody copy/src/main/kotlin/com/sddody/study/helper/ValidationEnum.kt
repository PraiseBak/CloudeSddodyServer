package com.sddody.study.helper

class ValidationEnumClass(val msg: String) {

    companion object {
        const val STUDY_MEMBER_VALIDATION = "스터디 멤버는 2-200명 입니다"
        const val STUDY_CONTENT_VALIDATION = "스터디 내용은 1-1000글자 입니다"
        const val STUDY_TITLE_VALIDATION = "스터디 제목은 2-40글자 입니다"
        const val IMAGE_SIZE_VALIDATION = "이미지는 최대 10개만 보낼 수 있습니다"
        const val LIST_SIZE_VALIDATION = "해당 자료형은 최대 10개만 가능합니다"
        const val CHAT_MSG_SIZE_VALIDATION = "채팅은 1-1000글자 이내로 보낼 수 있습니다"
        const val STUDY_LINK_VALIDATION = "링크가 너무 길어요"
    }
}
