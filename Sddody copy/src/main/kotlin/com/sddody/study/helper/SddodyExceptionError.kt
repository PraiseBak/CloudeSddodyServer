package com.sddody.study.helper

import org.springframework.http.HttpStatus

enum class SddodyExceptionError (val code : HttpStatus, val msg : String){
    DUPLICATE_USER(HttpStatus.CONFLICT,"이미 가입된 유저입니다"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT,"중복되는 닉네임입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"알 수 없는 에러가 발생하였습니다"),
    KAKAO_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"알 수 없는 에러가 발생하였습니다"),
    CANNOT_FIND_USER(HttpStatus.NOT_FOUND,"찾을 수 없는 유저입니다"),
    CANNOT_FIND_RESOURCE(HttpStatus.NOT_FOUND,"리소스를 찾을 수 없습니다"),
    AUTHORIZATION_FAIL(HttpStatus.BAD_REQUEST,"권한이 부족합니다"),
    TOKEN_INVALIDATE(HttpStatus.UNAUTHORIZED,"토큰이 유효하지 않습니다. 이 현상이 계속 지속되는 경우 관리자에게 문의해주세요"),
    REFRESH_TOKEN_INVALIDATE(HttpStatus.UNAUTHORIZED,"토큰이 유효하지 않거나 만료됐습니다 재로그인 해주세요"),
    BLOCKED_USER(HttpStatus.FORBIDDEN,"차단된 유저입니다 관리자에게 문의해주세요"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다"),
    DUPLICATE_REQUEST(HttpStatus.CONFLICT,"중복된 요청입니다"),
}