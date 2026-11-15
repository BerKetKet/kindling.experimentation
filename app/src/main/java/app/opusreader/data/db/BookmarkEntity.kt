package app.opusreader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val bookId: String,
    val location: Int,
    val chapterTitle: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
