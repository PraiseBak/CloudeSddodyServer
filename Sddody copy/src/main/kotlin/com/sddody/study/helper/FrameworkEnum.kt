package com.sddody.study.helper


/*
할 수 있는 기술 스택
 */
enum class FrameworkEnum(override val info: String) : InfoEnum {
    SpringBoot("SpringBoot"),
    Spring("Spring"),
    React("React"),
    NodeJS("Node.js"),
    NextJS("Next.js"),
    ExpressJS("Express.js"),
    VueJS("Vue.js"),
    Django("Django"),
    FastAPI("FastAPI"),
    Flask("Flask"),
    ETC("기타");
}
