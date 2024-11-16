package com.sddody.study.helper


enum class AlarmCategory(val category : String) {
    Study("study"),
    Chat("chat"),
    //커뮤니티로 이동하는 기능 어떻게 구현할지
    Community("community"),
}

enum class AlarmInfoEnum(val infoMsg : String){
    StudyParticipation("스터디에 대한 새로운 참가 요청입니다"),
    StudyParticipationAllow("신청했던 스터디 참가 요청이 수락됐어요"),
    StudyParticipationDenied("신청했던 스터디 참가 요청이 거절됐어요"),
    ChatAdd("채팅방에 새로운 메시지가 도착했어요!")
}