package jp.juggler.testCompose.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import timber.log.Timber

class LoggingLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    override fun onAttachedToWindow() {
        Timber.i("onAttachedToWindow")
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        Timber.i("onDetachedFromWindow")
        super.onDetachedFromWindow()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        val visibilityText = when (visibility) {
            GONE -> "GONE"
            VISIBLE -> "VISIBLE"
            INVISIBLE -> "INVISIBLE"
            else -> "?($visibility)"
        }
        Timber.i("onVisibilityChanged visibility=$visibilityText")
        super.onVisibilityChanged(changedView, visibility)
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        Timber.i("onVisibilityAggregated isVisible=$isVisible")
        super.onVisibilityAggregated(isVisible)
    }
}
