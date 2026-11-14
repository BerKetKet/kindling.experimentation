package app.opusreader

import app.opusreader.engine.FakeReaderEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeReaderEngineTest {

    private lateinit var engine: FakeReaderEngine

    @Before
    fun setUp() {
        engine = FakeReaderEngine("Gardens of the Moon")
    }

    @Test
    fun testInitialState() {
        assertEquals(91, engine.currentLocation.value.current)
        assertEquals(879, engine.currentLocation.value.total)
        assertFalse(engine.isBookmarked.value)
    }

    @Test
    fun testPagination() {
        assertTrue(engine.nextPage())
        assertTrue(engine.currentLocation.value.current > 91)
        assertTrue(engine.prevPage())
        assertEquals(91, engine.currentLocation.value.current)
    }

    @Test
    fun testBookmarkToggle() {
        val initial = engine.isBookmarked.value
        val toggled = engine.toggleBookmark()
        assertEquals(!initial, toggled)
    }
}
