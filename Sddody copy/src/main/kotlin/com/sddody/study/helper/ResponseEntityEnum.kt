package com.sddody.study.helper;

import org.springframework.http.HttpStatus

/*
그냥 항상 OK하고 실패시에는 except날리는게?
 */
enum class ResponseEntityEnum(val msg : String,val code : HttpStatus){
    //chatroom에서 메시지 전송 성공
    SEND_MSG_SUCCESS("메시지 전송에 성공하였습니다.",HttpStatus.OK),
    SUCCESS("요청 수행 완료.",HttpStatus.OK),

}
