package app.opusreader.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.opusreader.engine.ReaderSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.readerDataStore: DataStore<Preferences> by preferencesDataStore(name = "reader_settings")

class ReaderSettingsDataStore(private val context: Context) {

    private object PreferencesKeys {
        val FONT_FAMILY = stringPreferencesKey("font_family")
        val FONT_SIZE = intPreferencesKey("font_size")
        val LINE_SPACING = floatPreferencesKey("line_spacing")
        val MARGIN_PRESET = intPreferencesKey("margin_preset")
        val IS_JUSTIFIED = booleanPreferencesKey("is_justified")
        val PAGE_COLOR = stringPreferencesKey("page_color")
        val UPDATE_WITH_SYSTEM = booleanPreferencesKey("update_with_system")
        val CONTINUOUS_SCROLLING = booleanPreferencesKey("continuous_scrolling")
        val ORIENTATION_LOCK = booleanPreferencesKey("orientation_lock")
        val BRIGHTNESS = floatPreferencesKey("brightness")
        val AUTO_BRIGHTNESS = booleanPreferencesKey("auto_brightness")
        val ASSISTIVE_READER = booleanPreferencesKey("assistive_reader")
        val READING_PROGRESS = stringPreferencesKey("reading_progress")
        val VISIBLE_CLOCK = booleanPreferencesKey("visible_clock")
        val VOLUME_KEYS = booleanPreferencesKey("volume_keys")
        val PAGE_TURN_ANIM = booleanPreferencesKey("page_turn_anim")
    }

    val settingsFlow: Flow<ReaderSettings> = context.readerDataStore.data.map { prefs ->
        ReaderSettings(
            fontFamily = prefs[PreferencesKeys.FONT_FAMILY] ?: "Bookerly",
            fontSize = prefs[PreferencesKeys.FONT_SIZE] ?: 18,
            lineSpacingMultiplier = prefs[PreferencesKeys.LINE_SPACING] ?: 1.3f,
            marginPreset = prefs[PreferencesKeys.MARGIN_PRESET] ?: 1,
            isJustified = prefs[PreferencesKeys.IS_JUSTIFIED] ?: true,
            pageColor = prefs[PreferencesKeys.PAGE_COLOR] ?: "BLACK",
            updatePageWithSystemTheme = prefs[PreferencesKeys.UPDATE_WITH_SYSTEM] ?: true,
            continuousScrolling = prefs[PreferencesKeys.CONTINUOUS_SCROLLING] ?: false,
            orientationLock = prefs[PreferencesKeys.ORIENTATION_LOCK] ?: false,
            brightness = prefs[PreferencesKeys.BRIGHTNESS] ?: 0.8f,
            autoBrightness = prefs[PreferencesKeys.AUTO_BRIGHTNESS] ?: true,
            assistiveReaderEnabled = prefs[PreferencesKeys.ASSISTIVE_READER] ?: false,
            readingProgressDisplay = prefs[PreferencesKeys.READING_PROGRESS] ?: "LOCATION_PERCENT",
            visibleClock = prefs[PreferencesKeys.VISIBLE_CLOCK] ?: true,
            volumeKeysTurnPages = prefs[PreferencesKeys.VOLUME_KEYS] ?: false,
            pageTurnAnimation = prefs[PreferencesKeys.PAGE_TURN_ANIM] ?: true
        )
    }

    suspend fun saveSettings(settings: ReaderSettings) {
        context.readerDataStore.edit { prefs ->
            prefs[PreferencesKeys.FONT_FAMILY] = settings.fontFamily
            prefs[PreferencesKeys.FONT_SIZE] = settings.fontSize
            prefs[PreferencesKeys.LINE_SPACING] = settings.lineSpacingMultiplier
            prefs[PreferencesKeys.MARGIN_PRESET] = settings.marginPreset
            prefs[PreferencesKeys.IS_JUSTIFIED] = settings.isJustified
            prefs[PreferencesKeys.PAGE_COLOR] = settings.pageColor
            prefs[PreferencesKeys.UPDATE_WITH_SYSTEM] = settings.updatePageWithSystemTheme
            prefs[PreferencesKeys.CONTINUOUS_SCROLLING] = settings.continuousScrolling
            prefs[PreferencesKeys.ORIENTATION_LOCK] = settings.orientationLock
            prefs[PreferencesKeys.BRIGHTNESS] = settings.brightness
            prefs[PreferencesKeys.AUTO_BRIGHTNESS] = settings.autoBrightness
            prefs[PreferencesKeys.ASSISTIVE_READER] = settings.assistiveReaderEnabled
            prefs[PreferencesKeys.READING_PROGRESS] = settings.readingProgressDisplay
            prefs[PreferencesKeys.VISIBLE_CLOCK] = settings.visibleClock
            prefs[PreferencesKeys.VOLUME_KEYS] = settings.volumeKeysTurnPages
            prefs[PreferencesKeys.PAGE_TURN_ANIM] = settings.pageTurnAnimation
        }
    }
}
