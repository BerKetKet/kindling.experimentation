package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.engine.FakeReaderEngine
import app.opusreader.engine.ReaderEngine
import app.opusreader.ui.components.OpusToggleRow
import app.opusreader.ui.components.ToggleType
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurfaceVariant
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.PageColorBlack
import app.opusreader.ui.theme.PageColorGreen
import app.opusreader.ui.theme.PageColorSepia
import app.opusreader.ui.theme.PageColorWhite

@Composable
fun DisplaySettingsLayoutTab(
    engine: ReaderEngine,
    modifier: Modifier = Modifier
) {
    val settings by engine.settings.collectAsState()

    val pageColors = listOf(
        "WHITE" to PageColorWhite,
        "SEPIA" to PageColorSepia,
        "GREEN" to PageColorGreen,
        "BLACK" to PageColorBlack
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = OpusSpacing.medium)
    ) {
        // Page color palette
        Text(
            text = "Page Color",
            color = OpusTextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.xsmall)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.large)
        ) {
            pageColors.forEach { (name, color) ->
                val isSelected = settings.pageColor == name
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) OpusAccentBlue else Color(0xFF555555),
                            shape = CircleShape
                        )
                        .clickable { engine.updateSettings { it.copy(pageColor = name) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.medium))

        OpusToggleRow(
            title = "Update Page Using System Theme",
            checked = settings.updatePageWithSystemTheme,
            toggleType = ToggleType.CHECKBOX,
            onCheckedChange = { chk -> engine.updateSettings { it.copy(updatePageWithSystemTheme = chk) } }
        )

        HorizontalDivider(color = OpusDivider, thickness = 0.5.dp)

        OpusToggleRow(
            title = "Continuous Scrolling",
            subtitle = "Read by scrolling vertically.",
            checked = settings.continuousScrolling,
            toggleType = ToggleType.SWITCH,
            onCheckedChange = { sc -> engine.updateSettings { it.copy(continuousScrolling = sc) } }
        )

        HorizontalDivider(color = OpusDivider, thickness = 0.5.dp)

        // Margins preset (3 presets)
        Text(
            text = "Margins",
            color = OpusTextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
        ) {
            listOf("Narrow", "Normal", "Wide").forEachIndexed { index, label ->
                val isSelected = settings.marginPreset == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) OpusTextPrimary else OpusSurfaceVariant)
                        .clickable { engine.updateSettings { it.copy(marginPreset = index) } }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color(0xFF101112) else OpusTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.medium))

        // Alignment (Justified vs Left)
        Text(
            text = "Alignment",
            color = OpusTextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OpusSpacing.standard),
            horizontalArrangement = Arrangement.spacedBy(OpusSpacing.medium)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (settings.isJustified) OpusTextPrimary else OpusSurfaceVariant)
                    .clickable { engine.updateSettings { it.copy(isJustified = true) } }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FormatAlignJustify,
                        contentDescription = null,
                        tint = if (settings.isJustified) Color(0xFF101112) else OpusTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Justified",
                        color = if (settings.isJustified) Color(0xFF101112) else OpusTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (settings.isJustified) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!settings.isJustified) OpusTextPrimary else OpusSurfaceVariant)
                    .clickable { engine.updateSettings { it.copy(isJustified = false) } }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
                        contentDescription = null,
                        tint = if (!settings.isJustified) Color(0xFF101112) else OpusTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Left",
                        color = if (!settings.isJustified) Color(0xFF101112) else OpusTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (!settings.isJustified) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OpusSpacing.small))

        OpusToggleRow(
            title = "Orientation Lock",
            checked = settings.orientationLock,
            toggleType = ToggleType.SWITCH,
            onCheckedChange = { lock -> engine.updateSettings { it.copy(orientationLock = lock) } }
        )
    }
}

@Preview(name = "18 Display Settings Layout Tab Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun DisplaySettingsLayoutTabPreview() {
    OpusReaderTheme {
        DisplaySettingsLayoutTab(engine = FakeReaderEngine())
    }
}
