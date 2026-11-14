package app.opusreader.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object OpusSpacing {
    val xxsmall: Dp = 2.dp
    val xsmall: Dp = 4.dp
    val small: Dp = 8.dp
    val medium: Dp = 12.dp
    val standard: Dp = 16.dp
    val large: Dp = 20.dp
    val xlarge: Dp = 24.dp
    val xxlarge: Dp = 32.dp

    // Measured component dimensions (1080x2340 @ 2.625 density)
    val statusBandHeight: Dp = 58.refPx
    val titleBandHeight: Dp = 52.refPx
    val topBarHeight: Dp = 56.dp
    val bottomNavHeight: Dp = 166.refPx // 63.24 dp (y: 2111-2277)
    val bottomNavDividerHeight: Dp = 3.refPx // 1.14 dp
    val searchBarHeight: Dp = 105.refPx // 40.0 dp (y: 254-358)
    val searchBarRadius: Dp = 52.5.refPx // 20.0 dp
    val searchBarMarginHorizontal: Dp = 42.refPx // 16.0 dp
    val screenMarginHorizontal: Dp = 44.refPx // 16.8 dp
    val minTouchTarget: Dp = 48.dp

    // Cover dimensions
    val gridCoverWidth: Dp = 279.refPx // 106.3 dp
    val gridCoverHeight: Dp = 427.refPx // 162.7 dp
    val gridRowPitch: Dp = 474.refPx // 180.6 dp
    val gridRowGap: Dp = 47.refPx // 17.9 dp
    val gridSideMargin: Dp = 75.refPx // 28.6 dp

    val listCoverWidth: Dp = 168.refPx // 64.0 dp
    val listCoverHeight: Dp = 253.refPx // 96.4 dp
    val listRowPitch: Dp = 318.refPx // 121.1 dp
    val listDividerHeight: Dp = 3.refPx // 1.14 dp

    val floatingCoverWidth: Dp = 210.refPx // 80.0 dp (x: 40-250)
    val floatingCoverHeight: Dp = 349.refPx // 132.95 dp (y: 1894-2243)
    val floatingCoverFloatOffset: Dp = 126.refPx // 48.0 dp above bottom bar
}
