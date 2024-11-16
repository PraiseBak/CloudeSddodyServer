package com.sddody.study.helper


/*
프로그래머스 2023 개발자 설문조사 통계 기반
 */
enum class ExpertStatisticEnum(override val info: String, override val weight : Int) : FrameworkStatisticEnum {
    SpringBoot("Spring Boot", 23),
    Spring("Spring", 13),
    React("React", 22),
    NodeJS("Node.js", 17),
    NextJS("Next.js", 5),
    ExpressJS("Express.js", 4),
    VueJS("Vue.js", 3),
    Django("Django", 7),
    FastAPI("FastAPI", 1),
    Flask("Flask", 2),
    ETC("기타", 2);
}