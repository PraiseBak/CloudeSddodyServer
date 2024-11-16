package com.sddody.study.config

import com.sddody.study.common.ExceptionHandlerFilter
import com.sddody.study.common.InternalFilter
import com.sddody.study.utility.security.*
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.util.pattern.PathPatternParser


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfig (private val internalFilter: InternalFilter,private val exceptionHandlerFilter: ExceptionHandlerFilter
)
{

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun pathPatternParser(): PathPatternParser {
        return PathPatternParser()
    }


    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .sessionManagement { sessionManagement: SessionManagementConfigurer<HttpSecurity?> ->
                sessionManagement.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(internalFilter, UsernamePasswordAuthenticationFilter::class.java)

            //회원가입시에는 권한없음
            //유저정보 입력전에는 ANONYMOUS
            //입력후에는 USER
            .authorizeHttpRequests{ req -> req
                .anyRequest().permitAll()
//                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                .requestMatchers("/api/auth/memberInfo").hasAnyRole("ADMIN","USER","ANONYMOUS")
//                .requestMatchers("/api/auth/start").hasAnyRole("ANONYMOUS")
//                .anyRequest().hasAnyRole("USER","ADMIN") // 그 외의 모든 요청은 인증된 사용자(ROLE_USER)에게만 허용함
            }
//            .oauth2Login { oauth2 -> oauth2.userInfoEndpoint{userInfo -> userInfo.userService(oAuth2UserService)}}
        return http.build()
    }



}
