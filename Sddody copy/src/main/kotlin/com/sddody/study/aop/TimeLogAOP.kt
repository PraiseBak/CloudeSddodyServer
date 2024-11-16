package com.sddody.study.aop;


import com.sddody.study.utility.log.LogTrace
import lombok.RequiredArgsConstructor
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
@RequiredArgsConstructor
class TimeLogAOP(
    val logTrace : LogTrace
) {
    private val logger = LoggerFactory.getLogger(this::class.java)


    @Pointcut("execution(* com.sddody.study.service..*(..))")
    fun allService(){

    }

    @Pointcut("execution(* com.sddody.study.controller..*(..))")
    fun allController(){

    }


    @Around("allController() || allService()")
    fun doLog(joinPoint: ProceedingJoinPoint): Any? {
        val traceStatus = logTrace.begin(joinPoint.signature.toShortString())

        try {
            val result = joinPoint.proceed()
            return result
        } catch (e: Exception) {
            logTrace.exception(traceStatus,e)
            throw e
        } finally {
            logTrace.end(traceStatus)
        }
    }
}