package com.sddody.study.repository

import com.sddody.study.entity.Chat
import com.sddody.study.entity.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ChatRepository  : JpaRepository<Chat, Long> {




}
