package app.opusreader.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.R
import app.opusreader.ui.theme.AmazonEmberFontFamily
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.refPx

@Composable
fun AnnotationsShareSheet(
    bookTitle: String = "Gardens of the Moon",
    onCloseClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var emailAddresses by remember { mutableStateOf(listOf("user@example.com")) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(986.refPx)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color(0xFF1A1B1C))
    ) {
        // Drag handle (y=1375..1385, top padding = 21px from sheet top)
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = (21 / 2.625).dp)
                .size(width = (104 / 2.625).dp, height = (11 / 2.625).dp)
                .clip(CircleShape)
                .background(Color(0xFF8E9194))
        )

        // Header (y=1428..1470)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = (31 / 2.625).dp)
        ) {
            Text(
                text = "Share via Email",
                color = Color.White,
                fontFamily = AmazonEmberFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                modifier = Modifier.align(Alignment.Center)
            )

            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = (42 / 2.625).dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bds_close),
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height((43 / 2.625).dp))

        // Divider 1 (y=1523..1525)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((2 / 2.625).dp)
                .background(Color(0xFF3B3C3D))
        )

        // Description text (y=1566..1763)
        Text(
            text = "Share these annotations with friends, coworkers, and\nother personal contacts. People you share this with will\nsee that it was sent by John Doe. Only send\nto others with permission.",
            color = Color(0xFFC5C6C7),
            fontFamily = AmazonEmberFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.2.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            modifier = Modifier.padding(start = (45 / 2.625).dp, top = (33 / 2.625).dp, end = (45 / 2.625).dp)
        )

        Spacer(modifier = Modifier.height((33 / 2.625).dp))

        // Input field ("Add an email address") (y=1796..1942, h=146px = 56dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (45 / 2.625).dp)
                .height((146 / 2.625).dp)
                .border(width = 1.dp, color = Color(0xFF58595B), shape = RoundedCornerShape(4.dp))
                .padding(horizontal = (24 / 2.625).dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Add an email address",
                color = Color(0xFF8E9194),
                fontFamily = AmazonEmberFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add email",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height((33 / 2.625).dp))

        // Address chips (y=1975..2060, h=85px)
        if (emailAddresses.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = (45 / 2.625).dp)
                    .height((85 / 2.625).dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF28292A))
                    .border(width = 1.dp, color = Color(0xFF58595B), shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = (24 / 2.625).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emailAddresses.first(),
                    color = Color.White,
                    fontFamily = AmazonEmberFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                Spacer(modifier = Modifier.width((24 / 2.625).dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove address",
                    tint = Color(0xFF8E9194),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { emailAddresses = emptyList() }
                )
            }
        }

        Spacer(modifier = Modifier.height((46 / 2.625).dp))

        // Send / Export button (y=2106..2231, h=125px = 48dp)
        Button(
            onClick = {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "# Annotations for $bookTitle\n\n- Location 3971: soft knock diverted his attention. “Yes?”"
                    )
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Export Annotations"))
                onCloseClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE6F2FF),
                contentColor = Color(0xFF0F1A26)
            ),
            shape = RoundedCornerShape(9999.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (45 / 2.625).dp)
                .height((125 / 2.625).dp)
        ) {
            Text(
                text = "Send",
                fontFamily = AmazonEmberFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height((43 / 2.625).dp))

        // Bottom divider 2 (y=2274..2276)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((2 / 2.625).dp)
                .background(Color(0xFF3B3C3D))
        )
    }
}

@Preview(name = "16 Annotations Share Sheet Preview", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AnnotationsShareSheetPreview() {
    OpusReaderTheme {
        AnnotationsShareSheet()
    }
}
