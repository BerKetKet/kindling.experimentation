package app.opusreader.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.View

object Haptics {
    fun tick(view: View?) {
        view?.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }

    fun click(view: View?) {
        view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    fun longPress(view: View?) {
        view?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    fun tick(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } catch (_: Exception) {
                @Suppress("DEPRECATION")
                vibrator.vibrate(10)
            }
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(10)
        }
    }
}
