package com.sddody.study.entity;

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import java.util.HashMap

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 채팅방을 관리하는 엔티티
 *
 * @property id
 * @property study
 * @property chatList
 * @property isChatOver
 */
class ChatRoom(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        @NotNull
        var study : Study,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "chatRoom")
        var chatList : MutableList<Chat> = mutableListOf(),

        //끝난 채팅인지
        var isChatOver : Boolean = false,

        var topic : String,

        //member[id] = readLeftCount
        @ElementCollection
        @CollectionTable(name = "chat_read_map")
        @MapKeyColumn(name = "read_member_id")
        @Column(name = "readCount")
        var chatReadMap : MutableMap<Long,Long> = HashMap()

) {
        fun setChatRoomEnd() {
                isChatOver = true
        }
}
