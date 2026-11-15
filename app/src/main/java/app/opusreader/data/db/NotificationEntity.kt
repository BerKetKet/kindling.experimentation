package app.opusreader.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.opusreader.data.NotificationItem

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val dateText: String,
    val isRead: Boolean = false,
    val targetRoute: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toNotificationItem(): NotificationItem = NotificationItem(
        id = id,
        title = title,
        description = description,
        dateText = dateText,
        isRead = isRead
    )

    companion object {
        fun fromNotificationItem(item: NotificationItem, targetRoute: String? = null): NotificationEntity = NotificationEntity(
            id = item.id,
            title = item.title,
            description = item.description,
            dateText = item.dateText,
            isRead = item.isRead,
            targetRoute = targetRoute
        )
    }
}
