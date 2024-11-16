package com.sddody.study.helper

import lombok.Getter

enum class DeveloperEnum(override val info: String) : InfoEnum {
    Frontend("Frontend"),
    Backend("Backend"),
    Android("Android"),
    IOS("IOS"),
    Game("게임"),
    Database("Database"),
    AI("AI"),
    Graphics("디자이너"),
    ETC("기타"),

}
