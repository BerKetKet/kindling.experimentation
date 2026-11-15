package app.opusreader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_locations")
data class ReadingLocationEntity(
    @PrimaryKey val bookId: String,
    val currentLocation: Int,
    val totalLocations: Int,
    val chapterTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)
