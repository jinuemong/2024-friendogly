package com.happy.friendogly.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.happy.friendogly.local.model.ChatMessageEntity

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatMessageEntity: ChatMessageEntity)

    @Query("SELECT * FROM chat_message")
    suspend fun getAll(): List<ChatMessageEntity>
}
