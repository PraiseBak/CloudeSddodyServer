package com.sddody.study.utility.security;

import jakarta.servlet.http.HttpServletRequest;


class ClientUtils {
    fun getRemoteIP(request: HttpServletRequest): String? {
        var ip = request.getHeader("X-FORWARDED-FOR")

        //proxy 환경일 경우
        if (ip == null || ip.length == 0) {
            ip = request.getHeader("Proxy-Client-IP")
        }

        //웹로직 서버일 경우
        if (ip == null || ip.length == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.length == 0) {
            ip = request.remoteAddr
        }
        return ip
    }
}
