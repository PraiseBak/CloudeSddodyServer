package com.sddody.study.helper


/*
프로그래머스 2023 개발자 설문조사 통계 기반
 */
enum class BeginnerStatisticEnum(override val info: String, override val weight : Int) : FrameworkStatisticEnum {
    SpringBoot("Spring Boot", 25),
    Spring("Spring", 13),
    React("React", 19),
    NodeJS("Node.js", 11),
    NextJS("Next.js", 6),
    ExpressJS("Express.js", 4),
    VueJS("Vue.js", 6),
    Django("Django", 5),
    FastAPI("FastAPI", 4),
    Flask("Flask", 4),
    ETC("기타", 2);
}