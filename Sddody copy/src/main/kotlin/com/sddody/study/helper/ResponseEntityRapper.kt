package com.sddody.study.helper

import org.springframework.http.ResponseEntity

class ResponseEntityWrapper (){

    companion object{
        fun getResponseEntity(responseEntityEnum: ResponseEntityEnum) : ResponseEntity<String> {
            return ResponseEntity<String>(responseEntityEnum.msg,responseEntityEnum.code)
        }
    }

}