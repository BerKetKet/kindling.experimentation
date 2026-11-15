package app.opusreader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "book_collection_cross_ref", primaryKeys = ["bookId", "collectionId"])
data class BookCollectionCrossRef(
    val bookId: String,
    val collectionId: String
)
