package com.est.mungpe.chat.repository;

import com.est.mungpe.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
    SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
    FROM Chat c
    WHERE (c.senderNum = :senderNum AND c.receiverNum = :receiverNum)
       OR (c.senderNum = :receiverNum AND c.receiverNum = :senderNum)
""")
    boolean existsChatBetweenUsers(@Param("senderNum") Long senderNum, @Param("receiverNum") Long receiverNum);

    List<Chat> findAllChatByRoomId(String roomId);

    //DISTINCT는 중복 제거
    @Query("""
    SELECT DISTINCT c.roomId FROM Chat c
    """)
    List<String> findAllRoomIds();
}
