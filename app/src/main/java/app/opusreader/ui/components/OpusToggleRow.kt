package app.opusreader.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

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
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = OpusSpacing.minTouchTarget)
            .clickable(enabled = enabled) {
                if (onClick != null) {
                    onClick()
                } else if (onCheckedChange != null) {
                    onCheckedChange(!checked)
                }
            }
            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = OpusTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(OpusSpacing.xxsmall))
                Text(
                    text = subtitle,
                    color = OpusTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 17.sp
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
                        uncheckedTrackColor = Color(0xFF333333)
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
                        tint = OpusTextPrimary,
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

@Preview(name = "OpusToggleRow Switch Preview", showBackground = true, backgroundColor = 0xFF232323)
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

@Preview(name = "OpusToggleRow Checkmark Preview", showBackground = true, backgroundColor = 0xFF232323)
@Composable
fun OpusToggleRowCheckmarkPreview() {
    OpusReaderTheme {
        OpusToggleRow(
            title = "Most Recent",
            checked = true,
            toggleType = ToggleType.CHECKMARK_ONLY
        )
    }
}
