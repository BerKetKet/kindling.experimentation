package app.opusreader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.opusreader.engine.AnnotationItem
import app.opusreader.engine.AnnotationType

@Entity(tableName = "annotations")
data class AnnotationEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val location: Int,
    val sectionTitle: String,
    val selectedText: String,
    val note: String? = null,
    val highlightColor: String = "YELLOW",
    val type: String = "HIGHLIGHT",
    val isFavorite: Boolean = false,
    val dateAdded: String = ""
) {
    fun toAnnotationItem(): AnnotationItem = AnnotationItem(
        id = id,
        location = location,
        sectionTitle = sectionTitle,
        selectedText = selectedText,
        note = note,
        highlightColor = highlightColor,
        type = try { AnnotationType.valueOf(type) } catch (_: Exception) { AnnotationType.HIGHLIGHT },
        isFavorite = isFavorite,
        dateAdded = dateAdded
    )

    companion object {
        fun fromAnnotationItem(bookId: String, item: AnnotationItem): AnnotationEntity = AnnotationEntity(
            id = item.id,
            bookId = bookId,
            location = item.location,
            sectionTitle = item.sectionTitle,
            selectedText = item.selectedText,
            note = item.note,
            highlightColor = item.highlightColor,
            type = item.type.name,
            isFavorite = item.isFavorite,
            dateAdded = item.dateAdded
        )
    }
}
