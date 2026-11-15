package app.opusreader.data.db

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.opusreader.data.Book
import app.opusreader.data.ContentType

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val series: String? = null,
    val progressPercent: Int = 0,
    val isDownloaded: Boolean = true,
    val fileSize: String = "15 MB",
    val coverColorArgb: Int = 0xFF384F38.toInt(),
    val coverDrawableRes: Int? = null,
    val isRead: Boolean = false,
    val contentType: String = "BOOK",
    val lastReadTimestamp: Long = 0L
) {
    fun toBook(collectionIds: List<String> = emptyList()): Book = Book(
        id = id,
        title = title,
        author = author,
        series = series,
        progressPercent = progressPercent,
        isDownloaded = isDownloaded,
        fileSize = fileSize,
        coverColor = Color(coverColorArgb),
        coverDrawableRes = coverDrawableRes,
        isRead = isRead,
        contentType = try { ContentType.valueOf(contentType) } catch (_: Exception) { ContentType.BOOK },
        collectionIds = collectionIds,
        lastReadTimestamp = lastReadTimestamp
    )

    companion object {
        fun fromBook(book: Book): BookEntity = BookEntity(
            id = book.id,
            title = book.title,
            author = book.author,
            series = book.series,
            progressPercent = book.progressPercent,
            isDownloaded = book.isDownloaded,
            fileSize = book.fileSize,
            coverColorArgb = book.coverColor.toArgb(),
            coverDrawableRes = book.coverDrawableRes,
            isRead = book.isRead,
            contentType = book.contentType.name,
            lastReadTimestamp = book.lastReadTimestamp
        )
    }
}
