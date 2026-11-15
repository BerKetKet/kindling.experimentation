package app.opusreader.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.NoPaddingPlatformStyle
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.refPx
import androidx.compose.ui.text.TextStyle

enum class ToggleType {
    SWITCH, CHECKBOX, RADIO, CHECKMARK_ONLY, CHEVRON, NONE
}

@Composable
fun OpusToggleRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    checked: Boolean = false,
    toggleType: ToggleType = ToggleType.SWITCH,
    enabled: Boolean = true,
    horizontalPadding: Dp = 56.refPx,
    verticalPadding: Dp = 48.refPx,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) {
                if (onClick != null) {
                    onClick()
                } else if (onCheckedChange != null) {
                    onCheckedChange(!checked)
                }
            }
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = AmazonEmberFontFamily,
                    color = OpusTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    platformStyle = NoPaddingPlatformStyle
                )
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(14.refPx))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontFamily = AmazonEmberFontFamily,
                        color = OpusTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 16.5.sp,
                        fontWeight = FontWeight.Normal,
                        platformStyle = NoPaddingPlatformStyle
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(OpusSpacing.medium))

        when (toggleType) {
            ToggleType.SWITCH -> {
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = OpusAccentBlue,
                        uncheckedThumbColor = OpusTextSecondary,
                        uncheckedTrackColor = Color(0xFF4A4B4D)
                    )
                )
            }
            ToggleType.CHECKBOX -> {
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = OpusAccentBlue,
                        uncheckedColor = OpusTextSecondary,
                        checkmarkColor = Color.White
                    )
                )
            }
            ToggleType.RADIO -> {
                RadioButton(
                    selected = checked,
                    onClick = { onCheckedChange?.invoke(!checked) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = OpusAccentBlue,
                        unselectedColor = OpusTextSecondary
                    )
                )
            }
            ToggleType.CHECKMARK_ONLY -> {
                if (checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = OpusAccentBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            ToggleType.CHEVRON -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Open",
                    tint = OpusTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
            ToggleType.NONE -> {}
        }
    }
}

@Preview(name = "OpusToggleRow Switch Preview", showBackground = true, backgroundColor = 0xFF2C2C2E)
@Composable
fun OpusToggleRowSwitchPreview() {
    OpusReaderTheme {
        OpusToggleRow(
            title = "Continuous Scrolling",
            subtitle = "Read by scrolling vertically.",
            checked = true,
            toggleType = ToggleType.SWITCH
        )
    }
}
