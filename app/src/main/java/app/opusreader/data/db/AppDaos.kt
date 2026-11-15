package app.opusreader.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY lastReadTimestamp DESC")
    fun getAllBooksFlow(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id LIMIT 1")
    suspend fun getBookById(id: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("UPDATE books SET isRead = :isRead WHERE id = :id")
    suspend fun updateReadState(id: String, isRead: Boolean)

    @Query("UPDATE books SET isDownloaded = :isDownloaded WHERE id = :id")
    suspend fun updateDownloadState(id: String, isDownloaded: Boolean)

    @Query("UPDATE books SET progressPercent = :progress, lastReadTimestamp = :timestamp WHERE id = :id")
    suspend fun updateProgress(id: String, progress: Int, timestamp: Long = System.currentTimeMillis())

    @Delete
    suspend fun deleteBook(book: BookEntity)
}

@Dao
interface ReadingLocationDao {
    @Query("SELECT * FROM reading_locations WHERE bookId = :bookId LIMIT 1")
    suspend fun getLocation(bookId: String): ReadingLocationEntity?

    @Query("SELECT * FROM reading_locations WHERE bookId = :bookId LIMIT 1")
    fun getLocationFlow(bookId: String): Flow<ReadingLocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(location: ReadingLocationEntity)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY location ASC")
    fun getBookmarksFlow(bookId: String): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE bookId = :bookId AND location = :location")
    suspend fun deleteBookmark(bookId: String, location: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE bookId = :bookId AND location = :location)")
    suspend fun hasBookmark(bookId: String, location: Int): Boolean
}

@Dao
interface AnnotationDao {
    @Query("SELECT * FROM annotations WHERE bookId = :bookId ORDER BY location ASC")
    fun getAnnotationsForBookFlow(bookId: String): Flow<List<AnnotationEntity>>

    @Query("SELECT * FROM annotations ORDER BY location ASC")
    fun getAllAnnotationsFlow(): Flow<List<AnnotationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnotation(annotation: AnnotationEntity)

    @Update
    suspend fun updateAnnotation(annotation: AnnotationEntity)

    @Query("DELETE FROM annotations WHERE id = :id")
    suspend fun deleteAnnotationById(id: String)

    @Query("UPDATE annotations SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: String)
}

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections ORDER BY name ASC")
    fun getAllCollectionsFlow(): Flow<List<CollectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Query("UPDATE collections SET name = :newName WHERE id = :id")
    suspend fun renameCollection(id: String, newName: String)

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteCollection(id: String)

    @Query("SELECT * FROM book_collection_cross_ref WHERE collectionId = :collectionId")
    fun getCrossRefsForCollectionFlow(collectionId: String): Flow<List<BookCollectionCrossRef>>

    @Query("SELECT * FROM book_collection_cross_ref")
    fun getAllCrossRefsFlow(): Flow<List<BookCollectionCrossRef>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(ref: BookCollectionCrossRef)

    @Delete
    suspend fun deleteCrossRef(ref: BookCollectionCrossRef)

    @Query("DELETE FROM book_collection_cross_ref WHERE bookId = :bookId AND collectionId = :collectionId")
    suspend fun removeBookFromCollection(bookId: String, collectionId: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotificationsFlow(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: String)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}
