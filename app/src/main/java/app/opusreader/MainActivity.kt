package app.opusreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.opusreader.navigation.OpusNavHost
import app.opusreader.ui.theme.OpusReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpusReaderTheme {
                OpusNavHost()
            }
        }
    }
}

@Preview(name = "Main Activity Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun MainActivityPreview() {
    OpusReaderTheme {
        OpusNavHost()
    }
}
