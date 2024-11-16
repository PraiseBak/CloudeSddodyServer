package com.sddody.study.config

import com.sddody.study.utility.log.LogTrace
import com.sddody.study.utility.log.ThreadLocalLogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AppConfigs{
    @Bean
    fun logTrace(): LogTrace {
        return ThreadLocalLogTrace()
    }
}