package app.opusreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSearchBarBorder
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary
import app.opusreader.ui.theme.OpusTextTertiary

@Composable
fun OpusSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.small)
            .height(48.dp)
            .background(OpusBackground, shape)
            .border(1.dp, OpusSearchBarBorder, shape)
            .then(
                if (onClick != null && !enabled) Modifier.clickable { onClick() }
                else Modifier
            )
            .padding(horizontal = OpusSpacing.medium),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = OpusTextSecondary,
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = OpusSpacing.small)
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = OpusTextTertiary,
                        fontSize = 15.sp
                    )
                }

                if (enabled) {
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = OpusTextPrimary,
                            fontSize = 15.sp
                        ),
                        cursorBrush = SolidColor(OpusTextPrimary),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (query.isNotEmpty() && enabled) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(OpusSpacing.minTouchTarget)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                        tint = OpusTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "OpusSearchBar Empty", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusSearchBarPreview() {
    OpusReaderTheme {
        OpusSearchBar(
            query = "",
            onQueryChange = {}
        )
    }
}

@Preview(name = "OpusSearchBar With Query", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun OpusSearchBarQueryPreview() {
    OpusReaderTheme {
        OpusSearchBar(
            query = "Malazan",
            onQueryChange = {}
        )
    }
}
